package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import java.text.*;

class BatchCaptureCollectionModel  {

    @Binding 
    def binding; 

    @Caller 
    def caller;
    
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
        return 'Batch Capture Collection ' + suffix 
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
    
    void open() {
        entity = svc.open(entity);
        if ( entity.state == 'DRAFT' ) { 
            mode = 'create';
            currentdate = formatDate( dtsvc.getServerDate());
            defaultdate = formatDate( entity.defaultreceiptdate );               
        }
        entity.sstartseries = formatSeries(entity.currentseries); 
        entity.sendseries = formatSeries(entity.endseries); 
        rebuildTotals();
    } 

    def getLookupAccount() {
        return Inv.lookupOpener("cashreceiptitem:lookup", [ 
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
        fetchList: {
            return entity.batchitems;
        }, 
        isAllowOpen: { 
            return (entity.state == 'DRAFT'); 
        }, 
        onOpenItem: { item, colname-> 
            return openItemImpl( item ); 
        }
    ] as BasicListModel;
    
    
    def addItem() {
        def p = [ fund: entity.fund ]; 
        def op = Inv.lookupOpener("batchcapture_collection_entry:create", p );
        op.target = 'popup'; 
        return op;
    }
    
    def openItem() {
        return openItemImpl( selectedItem ); 
    }
    
    def openItemImpl( item ) {
        if ( !item?.objid ) return null;
        
        def p = [ entity: [objid: item.objid]]; 
        if ( entity.state == 'DRAFT' ) {
            p.openForEditing = true; 
        }
        
        def op = Inv.lookupOpener("batchcapture_collection_entry:open", p );
        op.target = 'popup'; 
        return op;         
    }
    
    void removeItem() {
        if ( !selectedItem ) return; 

        def lastitem = entity.batchitems.last(); 
        if ( !lastitem.equals( selectedItem )) 
            throw new Exception('You are only allowed to remove the last item'); 
            
        def resp = svc.removeItem( selectedItem );  
        if ( resp.currentseries ) { 
            entity.currentseries = resp.currentseries; 
        }
        entity.batchitems.remove( selectedItem ); 
        
        rebuildTotals();         
        listModel.reload(); 
    } 
    
    def createItem() {
        def m  = [:];
        m.objid = "BCCE" + new java.rmi.server.UID();
        m.parentid = entity.objid; 
        m.series = entity.currentseries; 
        m.receiptno = formatSeries(m.series); 
        m.receiptdate = entity.defaultreceiptdate;
        m.collectiontype = entity.collectiontype;
        m.collector = entity.collector;
        m.paymentitems = []
        m.totalnoncash = 0.0;
        m.totalcash = 0.0;
        m.amount = 0.0;
        m.voided = 0;
        m.mode = 'create';
        
        def lastitem = (entity.batchitems? entity.batchitems.last() : null); 
        if ( lastitem?.receiptdate ) m.receiptdate = lastitem.receiptdate; 

        if( copyprevinfo ) {
            println 'lastitem-> '+ lastitem;
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
    }
    
    void updateBatchItem( item ) {
        if ( item.items.size() > 1 ) { 
            item.acctinfo = '( Multiple Accounts )'; 
        } else { 
            item.acctinfo = item.items[0].item.title;
        } 
        
        if ( item.mode == 'create' ) {
            entity.batchitems << item; 
            item.remove('mode'); 
            listModel.reload(); 
        } else {
            selectedItem.clear();
            selectedItem.putAll( item ); 
            listModel.refreshSelectedItem(); 
        } 
        
        if ( item.currentseries ) 
            entity.currentseries = item.currentseries; 
            
        rebuildTotals(); 
    } 
    
    void rebuildTotals() {
        def totalnoncash = 0.0; 
        def totalamount = 0.0;
        entity.batchitems.each{
            if ( it.voided.toString() == '1' ) {
                //do nothing 
            } else {
                totalnoncash += (it.totalnoncash ? it.totalnoncash : 0.0); 
                totalamount += (it.amount ? it.amount : 0.0); 
            }
        }
        entity.totalnoncash = totalnoncash;
        entity.totalamount = totalamount;
        entity.totalcash = totalamount - totalnoncash;
        if ( binding ) binding.notifyDepends('totals'); 
    }

    def delete() { 
        if ( MsgBox.confirm('You are about to delete this transaction. Continue? ')) {
            svc.removeEntity([ objid: entity.objid ]); 
            try {
                if ( caller ) caller.reload(); 
            } catch(Throwable t) {;} 
            
            return '_close'; 
        } 
        return null;
    } 
    
    void submitForPosting() {
        if( entity.batchitems.size() == 0) 
            throw new Exception("Please add at least one item");
            
        if (MsgBox.confirm('Submit captured receipts for posting?')){

            //build checksum data
            def numformat = new java.text.DecimalFormat('0.00'); 
            def checklist = [entity.objid]; 
            checklist << numformat.format(entity.totalamount); 
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
            if ( result ) entity.state = result.state; 
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