package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class RemittanceInitialModel extends CrudListModel {

    @Service("RemittanceService")
    def remSvc;    
    
    def amount = 0; 
    def totalvoid = 0;
    
    def summaryList;
    def voidList = [];
        
    void init() {
        //build summaryList
        def m = [_schemaname: 'cashreceipt_af_summary' ];
        m.where = ["collector.objid =:collectorid AND remittanceid IS NULL", [collectorid: user.objid ] ];
        summaryList = queryService.getList( m );
        if(!summaryList)
            throw new Exception("No cash receipts to remit")
        amount = summaryList.sum{ it.amount };
        
        //list of voided
        m = [_schemaname: 'cashreceipt' ];
        m.where = ["collector.objid =:collectorid AND remittanceid IS NULL AND NOT(void.objid IS NULL)", [collectorid: user.objid ] ];
        voidList = queryService.getList( m );
        if( voidList ) {
            totalvoid = voidList.sum{ it.amount };
        }
    }
    
    def getFormattedAmount() {
        if ( amount == null ) amount = 0.0; 
        return new java.text.DecimalFormat("#,##0.00").format( amount ); 
    }
    def getFormattedTotalVoid() {
        if ( totalvoid == null ) totalvoid = 0.0; 
        return new java.text.DecimalFormat("#,##0.00").format( totalvoid ); 
    }
    
    def afSummaryHandler = [
        fetchList: { o->
            return summaryList;
        },
        onOpenItem: {o,col->
            def p = [:];
            p.put( "query.afcontrolid", o.afcontrolid );
            p.put( "query.fromseries", o.fromseries );
            p.put( "query.toseries", o.toseries );
            return Inv.lookupOpener("cashreceipt_list:afseries", p );
        }
    ] as BasicListModel;
    
    def voidReceiptHandler = [
        fetchList: { o->
            return voidList;
        },
        onOpenItem: {o,col->
            return Inv.lookupOpener("cashreceipt:open", [entity: [objid: o.objid ]] );
        }
    ] as BasicListModel;

    
    def remittanceid; 
    
    void submitForRemittance() { 
        def p = [:];
        if ( remittanceid ) {
            p.objid = remittanceid; 
            
        } else  {
            p.items = summaryList; 
            p = remSvc.create( p ); 
            remittanceid = p.objid; 
        } 
        
        def op = Inv.lookupOpener("remittance:open", [ entity: p ]); 
        Inv.invoke( op );  
        binding.fireNavigation('_close'); 
    } 
}    