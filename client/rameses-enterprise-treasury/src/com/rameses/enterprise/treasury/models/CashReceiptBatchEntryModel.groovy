package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import java.text.*;

class CashReceiptBatchEntryModel  {

    @Binding 
    def binding;    

    @Service("BatchCaptureCollectionService")
    def svc;

    @Service("DateService")
    def dtsvc

    def mode;
    def entity;
    def selectedItem;
    def currentdate;
    def defaultdate;
    def status;
    
    //date
    
    def copyprevinfo
    def onPost; //handler

    String getTitle(){
        def suffix = ''
        if (entity.state)
            suffix = '(' + entity.state + ')'
        return 'Batch Capture ' + suffix 
    }

    def df = new SimpleDateFormat("yyyy-MM-dd");
    def formatDate = { o-> 
        if ( o == null ) return null; 
        if ( o instanceof java.util.Date ) {
            return df.parse( df.format( o ));
        } 
        return df.parse( o );     
    }

    String formatSeries( series ) {
        def p = (entity.prefix)?entity.prefix:'';
        def s = (entity.suffix)?entity.suffix:'';
        return p + series.toString().padLeft(entity.serieslength, '0') + s;
    }

    /********************************************************
      FormActions 
    ********************************************************/
    void create() {
        entity.sstartseries = formatSeries(entity.startseries);
        entity.sendseries = formatSeries(entity.endseries);
        currentdate = formatDate( dtsvc.getServerDate() );
        defaultdate = formatDate( entity.defaultreceiptdate );               
        entity = svc.initBatchCapture(entity); 
        copyprevinfo = true; 
    }
    
    void open() {
        entity = svc.open(entity);
        if ( entity.state == 'DRAFT' ) { 
            mode = 'create';
            currentdate = formatDate(dtsvc.getServerDate());
            defaultdate = formatDate( entity.defaultreceiptdate );               
        }
        entity.sstartseries = formatSeries(entity.startseries); 
        entity.sendseries = formatSeries(entity.endseries); 
    } 

    def getLookupAccount() {
        return InvokerUtil.lookupOpener("cashreceiptitem:lookup", [ 
            "query.txntype" : "cashreceipt", 
            "query.collectorid": entity.collector.objid,
            "query.collectiontype": selectedItem.collectiontype,
            "query.fund" : selectedItem.collectiontype?.fund,                
            onselect: { o->
                if(selectedItem.items == null ) selectedItem.items = [];
                
                selectedItem.items.clear();
                selectedItem.acctinfo = o.title;                 
                selectedItem.items << [ 
                    item: o, fund:o.fund, 
                    valuetype: o.valuetype, 
                    amount: o.defaultvalue 
                ]; 
                selectedItem.amount = o.defaultvalue; 
                selectedItem.totalcash = o.defaultvalue; 
                selectedItem.totalnoncash = 0.0; 
            }
        ]);
    }

    def listModel = [
        fetchList: { o->
            return entity.batchitems;
        },
        createItem: { colname-> 
            def maxseries = entity.batchitems.max{ it.series }?.series; 
            if ( maxseries==null ) { 
                maxseries = entity.startseries; 
            } else { 
                maxseries += 1; 
            } 

            if ( maxseries != entity.currentseries ) 
                throw new Exception('The current series is no longer in sync. Please reload your transaction.'); 
            if ( entity.currentseries > entity.endseries ) 
                throw new Exception("Current Series already exceeds the end series.  ");

            if ( colname == 'amount' ) 
                throw new Exception("Please specify an account first  ");
                
            def m  = [:];
            m.objid = "BCCE" + new java.rmi.server.UID();
            m.parentid = entity.objid; 
            m.series = entity.currentseries; 
            m.receiptno = formatSeries(m.series); 

            //validate the receiptno to prevent duplication 
            if ( entity.batchitems?.find{ it.receiptno==m.receiptno } ) { 
                throw new Exception('Batch items are no longer synced. Please reload the your screen.'); 
            } 
            
            m._filetype = "batchcapture:misc";
            m.receiptdate = entity.defaultreceiptdate;
            m.collectiontype = entity.collectiontype;
            m.collector = entity.collector;
            m.paymentitems = []
            m.totalnoncash = 0.0;
            m.totalcash = 0.0;
            m.amount = 0.0;
            m.voided = 0;
            m.newitem = true ;
            if( copyprevinfo ) {
                def lastitem = (entity.batchitems? entity.batchitems.last() : null); 
                if ( lastitem ) {
                    if ( lastitem.items && lastitem.items.size() == 1 ) {
                        def nfo = lastitem.items[0].clone(); 
                        nfo.item = nfo.item.clone(); 
                        nfo.fund = nfo.fund.clone(); 
                        m.items = [ nfo ]; 
                        m.amount = m.totalcash = nfo.amount; 
                    } 
                    m.acctinfo = lastitem.acctinfo; 
                    m.receiptdate = lastitem.receiptdate;
                    m.paidbyaddress = lastitem.paidbyaddress;
                    m.paidby = lastitem.paidby; 
                } 
            } 
            return m;
        },
        isAllowOpen: {
            return (entity.state == 'DRAFT'); 
        }, 
        getOpenerParams: {o-> 
            return [ 
                fund: entity.fund, 
                callerListModel: listModel, 
                calculateHandler: {  en, mode ->    
                    if ( mode == 'edit' ) {
                        def result = svc.addUpdateItem(entity, en); 
                        if (result.item?.amount) en.amount = result.item.amount; 

                        updateHeader( result.header );

                    } 
                } 
            ]; 
        },
        addItem: { o-> 
            validateEntry( o );
            entity.batchitems << o; 
        },
        isColumnEditable:{item, colname-> 
            if ( colname != 'amount' ) return true; 
            if ( !item.items ) return false; 
            if ( item.items.size() > 1 ) return false; 
            
            def valuetype = item.items[0].valuetype.toString().toUpperCase();
            return ( valuetype == 'FIXED'? false : true ); 
        },
        onColumnUpdate: {item, colname-> 
            if( colname == 'receiptdate' ) {
                 def rdate = formatDate(item.receiptdate); 
                 if ( !rdate ) 
                    throw new Exception("Please enter a correct receipt date ");
                 if ( rdate.before(defaultdate) )
                    throw new Exception("Receipt date must not be less than the default date.");
                     
                 int rowIndex = status.index;
                 if ( rowIndex == 0 ) {
                    if(!rdate.equals(defaultdate))
                        throw new Exception("Receipt date must be equal to default date");
                 }  
                 else {
                    def prevRow = entity.batchitems[rowIndex-1];
                    def nextRow = null;
                    if( entity.batchitems.size() >= (rowIndex+1)) {
                        nextRow = entity.batchitems[rowIndex+1];
                    }
                    if( formatDate(prevRow.receiptdate).after( rdate )) {
                        throw new Exception("Receipt date must not be lesser than the previous entry date");
                    }
                    if(nextRow && formatDate(nextRow.receiptdate).before( rdate )) {
                        throw new Exception("Receipt date must not be greater than succeeding entry date");
                    }
                 }            
            }

            if (colname == 'amount') {
                item.items[0].amount = item[colname]; 
                item.totalcash = item.amount;
                item.totalnoncash = 0.0;
            }
            if ( colname == 'voided') { 
                def result = svc.addUpdateItem(entity, item); 
                if (result.item?.amount) item.amount = result.item.amount; 

                updateHeader( result.header ); 
            }
        },
        commitItem: { o-> 
            def result = svc.addUpdateItem(entity, o); 
            if (result.item?.amount) o.amount = result.item.amount; 

            updateHeader( result.header );
        },
        onRemoveItem: { o-> 
            if (!(entity.state == 'DRAFT')) return false;
             
            if( status.index != (entity.batchitems.size()-1) ) { 
                throw new Exception("Only the last item can be removed" );
            }
            
            if(! MsgBox.confirm('Remove item? ')) return false;

            def header = svc.removeItem(o, entity); 
            updateHeader( header ); 
            entity.batchitems.remove(o); 
            return true; 
        } 
    ] as EditorListModel;

    void updateHeader( header ) {
        entity.currentseries = header.currentseries; 
        entity.totalamount = header.totalamount;
        entity.totalnoncash = header.totalnoncash;
        entity.totalcash = header.totalcash; 
        binding.refresh("entity.totalcash|entity.totalnoncash|entity.totalamount"); 
    }  

    void validateEntry( o ) { 
        if ( !o.receiptdate ) throw new Exception("Receipt Date is required.   ")
        if ( !o.paidby ) throw new Exception("Paid By is required.   ")
        if ( !o.paidbyaddress ) throw new Exception("Address is required.   ")
        if ( !o.acctinfo ) throw new Exception("Account is required.   ")
        if ( o.amount == null ) throw new Exception("Amount is required.   ")
        if ( o.amount <= 0.0 ) throw new Exception("Amount must not be less than or equal to zero.   ")
    }

    def delete(){
        if (MsgBox.confirm('Delete record?')){
            svc.removeBatchCapture([objid:entity.objid]);
            return '_close';
        }
        return null;
    }

    void submitForPosting() {
        if( entity.batchitems.size() == 0) 
            throw new Exception("Please add at least one item");
        if (MsgBox.confirm('Submit captured receipts for posting?')){

            //build checksum data
            def checklist = [entity.objid, entity.totalamount]; 
            checklist << entity.batchitems.size();
            entity.batchitems.each{ checklist << it.series }

            def params = [ objid: entity.objid ];
            params.checksum = com.rameses.util.Encoder.MD5.encode(checklist.join(',')); 

            def result = svc.submitForPosting( params );
            entity.state = result.state; 
            listModel.reload();
        }
    }

    void disapprove(){ 
        if( MsgBox.confirm("You are about to disapprove this transaction. Continue? ") ) {
            def result = svc.disapproved([ objid: entity.objid ]); 
            entity.state = result.state; 
            listModel.reload(); 
        } 
    } 

    void post() {
        if( MsgBox.confirm("You are about to post this captured collection. Continue? ") ) {
            def result = svc.post([ objid: entity.objid ]);
            entity.state = result.state; 
            if ( onPost ) onPost();
        }
    }

    void submitForOnlineRemittance() {
        if( MsgBox.confirm("You are about to submit this captured collection for online remittance. Continue? ")) {
            def result = svc.submitForOnlineRemittance([ objid: entity.objid ]); 
            entity.state = result.state; 
            if ( onPost ) onPost(); 
        } 
    } 
    
    boolean isAllowRevertPosting() { 
        if ( entity.remitted ) return false; 
        return ( entity.state=='POSTED'); 
    }
    void revertPosting() { 
        if( MsgBox.confirm("You are about to revert this transaction. Continue? ")) {
            def o = svc.revertPosting([ objid: entity.objid ]); 
            if ( o?.state ) entity.state = o.state; 
        } 
    } 
}  