package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class AFControlEditBatchModel  {

    @Service("QueryService")
    def qrySvc;

    @Service("AFTxnService")
    def service;
    
    def list;
    
    //this is the parent;
    def entity;
    
    //the document txntype
    def txntype;
    
    def refitemid;
    
    void init() {
        def m = [_schemaname:'vw_af_control_detail'];
        m.findBy = [refitemid: refitemid];
        m.orderBy = "stubno";
        m.select = "objid,stubno,batchno,controlid,startseries,endseries,currentseries,qtybalance,qtyissued"
        list = qrySvc.getList(m); 
    }
    
    def listHandler = [
        fetchList: { o->
            return list;
        },
        onColumnUpdate: { item, colName->
            if( colName.matches("currentseries|endseries")) {
                item.qtybalance = item.endseries - item.currentseries + 1;
                item.qtyissued = item.currentseries - item.startseries;
            }
            if( colName.matches("qtyissued")) {
                item.currentseries = item.qtyissued + 1;
                item.qtybalance = item.endseries - item.currentseries + 1;
            }
        },
        beforeColumnUpdate : { item, colName, newValue ->
            try {
                if( colName == "currentseries" ) {
                    int maxQty = item.endseries - item.startseries + 1;
                    if( (item.endseries - newValue + 1) <= 0 ) throw new Exception("Qty balance must be greater than 0");
                    if( (item.endseries - newValue + 1) > maxQty ) throw new Exception("Qty balance must be less than pr equal to " + maxQty );
                }
                item._edited = true;
                return true;
            }
            catch(e) {
                MsgBox.err(e);
                return false;
            }
        }
    ] as EditorListModel;
    
    def doOk() {
        def v = list.findAll{  it._edited };
        service.editBatch(v);
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
}    