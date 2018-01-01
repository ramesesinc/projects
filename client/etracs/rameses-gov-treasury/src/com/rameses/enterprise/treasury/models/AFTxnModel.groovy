package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

class AFTxnModel extends CrudPageFlowModel {

    @Service("AFTxnService")
    def svc;
    
    @Service("DateService")
    def dateSvc;
    
    boolean withrequest = false;
    def afrequest;
    
    def formTypes;
    def reqtype = null;
    def selectedItem;
    
    def itemTxnTypes;
    
    void create() {
        entity = [:];
        entity.state = 'DRAFT';
        entity.items = [];
        afrequest = null;
        itemListHandler.reload();
        entity.dtfiled = dateSvc.getBasicServerDate();
        
        def m = [_schemaname:'aftxn_type'];
        m.select = "formtype";
        m.where = ["1=1"];
        m.orderBy = "sortorder";
        formTypes = queryService.getList(m)*.formtype.unique();
    }
    
    void initNew() {
        if(afrequest!=null) {
            def req = persistenceService.read( [_schemaname:'afrequest', objid: afrequest.objid ] );
            entity.request = [objid:req.objid , reqno: req.reqno];
            entity.issueto = req.requester;
            entity.respcenter = req.respcenter;
            req.items.each {
                def m = [:];
                m.item = it.item;
                m.unit = it.unit;
                m.qtyrequested = it.qty;
                m.qty = it.qty;
                m.qtyserved = 0;
                m.cost = it.afunit.saleprice;
                m.txntype = req.reqtype;
                m.afunit = it.afunit;
                computeLineTotal(m);
                entity.items << m;
            }
            itemTxnTypes = [ req.reqtype ];
        }
        else {
            def m = [_schemaname:"aftxn_type"];
            m.findBy = [formtype: entity.txntype];
            m.orderBy = "sortorder";
            itemTxnTypes = queryService.getList( m )*.txntype;
            /*
            if( entity.txntype.matches('ISSUE|RETURN|TRANSFER') ) itemTxnTypes = ["COLLECTION", "SALE"];
            else if(entity.txntype == 'PURCHASE_RECEIPT' ) itemTxnTypes = ['PURCHASE'];
            else if(entity.txntype == 'BEGIN_BALANCE' ) itemTxnTypes = ['BEGIN'];
            else if( entity.txntype == 'FORWARD' ) itemTxnTypes = ['FORWARD']; 
            else throw new Exception("Unrecognized txntype " + entity.txntype );
            */
        }
    }

    //the only lookup here is for the request because the other request os for purchase (af receipt)
    public def getLookupRequest() {
        if( entity.txntype == "ISSUE" ) {
            return Inv.lookupOpener( "afrequest_collection:lookup", [:] );
        }
        else {
            return Inv.lookupOpener( "afrequest_purchase:lookup", [:] );
        }
    }
    
    def itemListHandler = [
        fetchList : { o->
            return entity.items;
        },
        onAddItem: { o-> 
            //if ( !o.qtyrequested ) o.qtyrequested = o.qtyissued;
            entity.items << o;
        },
        onRemoveItem: { o->
            entity.items.remove(o);
        },
        onColumnUpdate: { o,colName->
            if ( colName == "item" ) {
                o.item.objid = o.item.itemid;
                o.unit = o.item.unit; 
                o.cost = o.item.saleprice; 
                computeLineTotal( o ); 
            }
            else if(colName=="qty") {
                if ( afrequest && o.qty > o.qtyrequested ) 
                    throw new Exception("Qty must be less than qty requested");
                computeLineTotal( o ); 
            } 
            else if(colName=="cost") {
                computeLineTotal( o ); 
            } 
        }
    ] as EditorListModel;
    
    private void computeLineTotal( o ) {
        o.linetotal = (o.qty ? o.qty : 0) * (o.cost ? o.cost : 0.0);
    }
    
    public def getInfo() {
        return TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/views/AFTxnViewPage.gtpl", [entity:entity] );
    }
    
    void saveCreate() {
        def b = MsgBox.confirm( "You are about to save the draft document. Proceed?" );
        if(!b) throw new BreakException();
        super.saveCreate();
    }
    
    void open() {
        def e = [_schemaname:'aftxn', objid: entity.objid];
        entity = persistenceService.read( e );
    }
    
    def startOpen() {
        open();
        String state = (entity.state == 'DRAFT') ? 'view' : 'posted';
        return super.start( state );
    }
    
    void post() {
        svc.post([objid:entity.objid ]);
        reloadEntity();
    }

    void reloadEntity() {
        open();
        binding.refresh();
    }
    
    //**************************************************************************
    //PURCHASE RECEIPT 
    //**************************************************************************/
    def addBatch( def o ) {
        def item = entity.items.find{ it.objid == o.refitemid };
        return Inv.lookupOpener( "af_control:addbatch", [ 
            refitem:item, handler:{ vv-> reloadEntity(); } 
        ]);
    }
    
    void removeBatch(def o) {
        if( !MsgBox.confirm('You are about to remove the entered accountable forms. Proceed?') ) return;
        o.refid = entity.objid;
        o.txntype = entity.txntype;
        try {
            svc.removeBatch(o);
            reloadEntity();
        }
        catch(e) {
            MsgBox.err(e);
        }
    }
    
    void revertBatch(def o) {
        if( !MsgBox.confirm('You are about to revert the entries. Proceed?') ) return;
        o.refid = entity.objid;
        o.txntype = entity.txntype;
        try {
            svc.revertBatch(o);
            reloadEntity();
        }
        catch(e) {
            MsgBox.err(e);
        }
    }

    def editBatch( def o ) {
        def item = entity.items.find{ it.objid == o.refitemid };
        def e= [:];
        e.afid = item.item.objid;
        e.unit = item.unit;
        e.afunit = item.afunit;
        return Inv.lookupOpener("af_control:editbatch", ['refitemid': o.refitemid, entity: e, txntype:entity.txntype ]);
    }
    
    def issueBatch( def o ) {
        try {
            svc.issueBatch( [refitemid:o.refitemid] );
            reloadEntity();
        }
        catch(e) {
            MsgBox.err(e);
        }
    }
    
    def selectBatch( def o ) {
        def item = entity.items.find{ it.objid == o.refitemid };
        item.parent = entity;
        def strname = "af_control_batch:" + item.afunit.formtype + ":lookup"
        return Inv.lookupOpener(strname, [refitem: item ] );
    }
    
}    