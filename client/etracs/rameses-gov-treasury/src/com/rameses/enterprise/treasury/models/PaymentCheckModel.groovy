package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*; 

class PaymentCheckModel extends CrudFormModel { 

    boolean external = false;
    def handler;
    
    void beforeSave(def saveType) {
        if( handler ) handler( entity, saveType );
    }
    
} 