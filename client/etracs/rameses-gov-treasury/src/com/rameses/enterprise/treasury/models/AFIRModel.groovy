package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

class AFIssueModel extends CrudPageFlowModel {

    @Service("AFIRService")
    def svc;
    
    @Service("DateService")
    def dateSvc;
    
    boolean withrequest = false;
    def afrequest;
    
    def txnTypes;
    def reqtype = null;
    def selectedItem;
    
    def itemTxnTypes;
    
    void create() {
        entity = [:];
        entity.items = [];
        afrequest = null;
        itemListHandler.reload();
        entity.dtfiled = dateSvc.getBasicServerDate();
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
            if( entity.txntype == 'ISSUE' ) itemTxnTypes = ["COLLECTION", "SALE"]
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
        return TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/templates/AFIRItemDetail.gtpl", [entity:entity] );
    }
    
    void saveCreate() {
        def b = MsgBox.confirm( "You are about to save the draft document. Proceed?" );
        if(!b) throw new BreakException();
        super.saveCreate();
    }
    
    void open() {
        def e = [_schemaname:'afir', objid: entity.objid];
        entity = persistenceService.read( e );
    }
    
    def startOpen() {
        return super.start( "open" );
    }
    
    void post() { 
        svc.post( entity ); 
    }
    
    //details
    void assignAvailableStock() {
        MsgBox.alert( "entity is " + entity.objid );
    }
    
    void receiveStock(def p) {
        MsgBox.alert( "entity is " + p );
    }
    
}    