package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class CollectionDepositModel extends CrudFormModel { 

    @Service("CollectionDepositService")
    def depositSvc;
    
    String title = "Cash Deposit";

    def handler;
    def mode;
    
    def create() {
        mode = "create";
        entity = depositSvc.init(); 
        return 'default'; 
    } 

    
    void post() {
        boolean pass = false;
        def signature = null; 
        try {
            def h = { sig->
               pass = true;
               signature = sig; 
            }
            if ( com.rameses.rcp.sigid.SigIdDeviceManager.getProvider()?.test()) {
                def msg = "You are about to post this transaction. Please ensure the entries are correct";
                Modal.show("verify_submit_with_signature", [handler: h, message: msg ]);
            }
        } catch(Throwable t) {
            pass = MsgBox.confirm("You are about to post this transaction. Proceed?");
        } 
        
        if( pass ) {
            entity = depositSvc.post( entity ); 
            MsgBox.alert("Posting successful"); 
            mode = "read"; 
            if ( handler ) handler(); 
        }         
    }
    
    def delete() {
        if ( MsgBox.confirm("You are about to delete this transaction. Proceed?")) {
            depositSvc.delete([ objid: entity.objid ]); 
            return '_close'; 
        }
        return null; 
    }

    def checkModel = [
         fetchList: { o->
             return entity.checks;
         }
     ] as BasicListModel;

     def itemModel = [
        fetchList: { o->
            return entity.items;
        }
     ] as BasicListModel;
} 