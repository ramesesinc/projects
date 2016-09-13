package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;
        
public class PaymentOrderTypeModel extends CrudFormModel {
    
    @Caller
    def caller;
    
    def handlerList;
    
    void afterInit() {
        handlerList = [];
        try {
            handlerList =  Inv.lookupOpeners("paymentordertype:handler")*.properties.name;
        }
        catch(e){;}
        
    }
}  