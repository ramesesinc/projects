package com.rameses.clfc.treasury.depositslip;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class DepositTellerController
{
    @Binding
    def binding;

    def mode = 'create'; 
    def entity;
    def handler;
    def teller;
    def tellerLookup = Inv.lookupOpener("teller:lookup", [
        state: 'ACTIVE',
        onselect: { o->
            entity.teller = o;
            binding.refresh('teller');
        }
    ]);

    @FormTitle
    def getFormTitle() {
        return 'Deposit Slip Information'; 
    }
    
    void create() {
        mode = 'create'; 
        entity.teller = null;
    }
    
    void open() { 
        mode = 'read'; 
    }    
    
    def doCancel() {
        return '_close'; 
    }
    
    def doOk() {
        if (MsgBox.confirm("You are about to deposit this record. Continue?")) {
            if (handler) {
                EventQueue.invokeLater(handler, entity);
            }
            return '_close'; 
        }        
    }
}