package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.rcp.framework.ValidatorException;


class AFTxnHandlerIssue extends AFTxnHandler {

    def itemTxnTypes = ["COLLECTION", "SALE"];
    
    def itemListHandler = [
        fetchList : { o->
            return entity.items;
        },
        createItem: {
           return [:]; 
        }, 
        onAddItem: { o-> 
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
        def allowedit = (invoker.properties.tag == 'AFO');
        return TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/views/AFTxnViewIssue.gtpl", [ entity: entity, allowEdit: allowedit ]);
    }
    
    def editBatch( def o ) {
        def item = entity.items.find{ it.objid == o.aftxnitemid };
        def e= [:];
        e.afid = item.item.objid;
        e.unit = item.unit;
        e.afunit = item.afunit;
        return Inv.lookupOpener("af_control:editbatch", ['aftxnitemid': o.aftxnitemid, entity: e, txntype:entity.txntype ]);
    }
    
    def issueBatch( def o ) {       
        def itm = entity.items.find{( it.objid == o.aftxnitemid )}
        def selformno = itm?.item?.objid; 
        def selqtyrequested = itm?.qty; 
        
        int qty;
        def pp = [fields:[]];
        pp.fields << [name:'formno', caption:'Form No. ', datatype:'label', expression: (selformno ? selformno : "") ];
        pp.fields << [name:'qtyrequested', caption:'Qty Requested ', datatype:'label', expression: ""+(selqtyrequested ? selqtyrequested : 0)];
        pp.fields << [name:'qty',caption:'Qty to Issue', datatype:'integer', required: true];
        pp.data = [qty:o.qty];
        pp.formTitle = "<html><br><b>Please enter qty to issue</b><br><br></html>";
        pp.handler = { iq->
            qty = iq.qty;
        }
        Modal.show("dynamic:form", pp, [title:'Enter Qty', height:250]);
        if(!qty) return null;
        try {
            svc.issueBatch([ aftxnitemid: o.aftxnitemid, qty: qty ]);
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

    def saveCreate() {
        if(mode!='create') throw new Exception("Save only applicable for create");
        def b = MsgBox.confirm( "You are about to save the draft document. Proceed?" );
        if ( !b ) throw new BreakException();
        entity._schemaname = "aftxn";
        def e = persistenceService.create(entity);
        entity.objid = e.objid;
        reloadEntity();
        mode = "open";
        return "open";
    }
    
}    