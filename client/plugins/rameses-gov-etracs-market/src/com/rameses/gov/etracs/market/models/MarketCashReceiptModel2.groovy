package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.enterprise.treasury.cashreceipt.*

public class  MarketCashReceiptModel extends BasicCashReceipt {

    @Service("MarketCashReceiptService")
    def cashRctSvc;

    @Service("MarketAccountService")
    def acctSvc;

    //we specify this so print detail will appear.
    String entityName = "misc_cashreceipt";
    String title = "Market Collection";

    def barcodeid;
    def selectedItem;

    void init() {
        super.init();
        boolean pass = false;
        def m = [:];
        m.onselect = { o->
            loadInfo( [objid:o.objid] );
            loadBilling( [objid: entity.acctid, payment: [option:'FULL']] );
            pass = true;
        }
        Modal.show( Inv.lookupOpener( "cashreceipt:market:lookup", m ) );
        if(!pass) throw new BreakException();
    }

    void loadBarcode() {
        loadInfo( [acctno: barcodeid] ); 
        loadBilling( [objid: entity.acctid, payment: [option:'FULL']] );
        super.init();
    }

    void loadInfo( def e ) {
        def m = [:];
        if(e.objid) {
            m = acctSvc.open( [objid: e.objid] ); 
        }
        else {
            m = acctSvc.findByAcctno( [acctno: e.acctno] ); 
        }
        entity.payer = [objid: m.owner.objid, name: m.owner.name ];
        entity.paidby = m.owner.name;
        entity.acctno = m.acctno;
        entity.acctid = m.objid;
        entity.paidbyaddress = m.rentalunit?.name + "," + m.rentalunit?.location;
    } 

    def billingListModel = [
        fetchList: { o->
            return entity.billitems;
        },
        onColumnUpdate: { o,col->
            int j = entity.billitems.indexOf( o );
            entity.amount = 0;
            entity.billitems[0..j].each { it.selected = true; it.amtpaid = it.total; entity.amount+=it.amtpaid; };
            if( j < (entity.billitems.size()-1) ) {
                entity.billitems[(j+1)..(entity.billitems.size()-1)].each { it.selected = false; it.amtpaid = 0; };
            }
            billingListModel.reload();
            super.updateBalances();
        }
    ] as EditorListModel;


    def months = ['JAN','FEB','MAR','APR','MAY','JUN','JUL','AUG','SEP','OCT','NOV','DEC'];

    def showPaymentOption() {
        def m = [:];
        m.handler = { pmt-> 
            loadBilling([ objid:entity.acctid, payment: pmt ]);
        };
        //send only months with surcharge + the next due date
        m.months = [];
        for( e in entity.billitems ) {
            if( e.surcharge > 0 ) {
                m.months << [ month:e.month, year: e.year, smonth: months[e.month-1], runbal:e.runbal ];
            }
            else {
                m.months << [ month:e.month, year: e.year,  smonth: months[e.month-1], runbal:e.runbal ];
                break;
            }
        }
        return Inv.lookupOpener("cashreceipt:market:payoption", m );
    }

    def loadBilling = { m ->
        def r = [:];
        r.objid = entity.acctid; 
        r.billdate = entity.receiptdate;
        r.totalpaid = 0;
        if( m.payment?.option == 'PARTIAL' ) {
            r.totalpaid = new BigDecimal( m.payment.amount+"");
        }
        else if( m.payment?.option == 'MONTH' ) {
            r.totalpaid = m.payment.month.runbal;
        }
        def z = cashRctSvc.getBillItems( r );
        entity.particulars = z.particulars; 
        entity.items = z.items;
        entity.billitems = z.billitems;
        entity.amount = entity.billitems.sum{it.total};

        //calc run balance
        def runbal = 0;
        entity.billitems.each {
            runbal += it.total;
            it.runbal = runbal;
        }

        super.updateBalances();
        billingListModel.reload();
        receiptListModel.reload();
    }

    /*reserve the ff. code for later
    def selectAll() {
        int j = entity.billitems.size()-1;
        entity.amount = 0;
        entity.billitems.each { it.selected = true; it.amtpaid = it.total; entity.amount+=it.amtpaid; };
        billingListModel.reload();
        super.updateBalances();        
    }

    def unselectAll() {
        int j = entity.billitems.size()-1;
        entity.amount = 0;
        entity.billitems[0..j].each { it.selected = false; it.amtpaid = 0; };
        billingListModel.reload();
        super.updateBalances();        
    }
    */

} 