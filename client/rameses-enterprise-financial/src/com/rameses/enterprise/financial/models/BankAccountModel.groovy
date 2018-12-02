package com.rameses.enterprise.financial.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

class BankAccountModel extends CrudFormModel { 
    
    def acctTypes = ["CHECKING", "SAVINGS", "TIME DEPOSIT", "HYSA"];
    def currencyTypes = [ "PHP", "USD" ];
    
    
    @PropertyChangeListener
    def listener = [
        "entity.account" : { o->
            entity.acctid = o.objid;
        }
    ]
    
    def getLookupCashInBankAccount() {
        if(!entity.fund?.objid) {
            def ex = new Exception("Fund is required");
            MsgBox.err( ex );
            throw new BreakException();
        }
        return Inv.lookupOpener( "cashinbankaccount:lookup", [fundid: entity.fund.objid ] );
    }
} 