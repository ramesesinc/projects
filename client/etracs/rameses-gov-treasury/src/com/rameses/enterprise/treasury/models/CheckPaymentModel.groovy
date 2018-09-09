package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*; 

class CheckPaymentModel extends CrudFormModel { 

    boolean external = false;
    def handler;
    
    void beforeSave(def saveType) {
        if( external ) {
            if(!entity.depositvoucherid ) 
                throw new Exception("Deposit voucher is required in external checks");
            entity.amtused = entity.amount;
        }
        if( handler ) handler( entity, saveType );
    }
    
} 