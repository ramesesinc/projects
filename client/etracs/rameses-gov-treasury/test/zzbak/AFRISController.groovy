package com.rameses.gov.treasury.controllers;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import java.rmi.server.*;
com.rameses.gov.treasury.controllers
public class AFRISController extends RISController {

    @Service("RISService")
    def service;
       
    @Invoker
    def invoker;
  
    boolean showConfirmOnSave = true;
            
    public String getTxnType() {
        return invoker.properties.txntype;
    }

    public def getLookupItems() {
        return InvokerUtil.lookupOpener("af:lookup", [
            onselect:{ o->
                if( entity.items.findAll{ it.item.objid ==  o.objid })
                    throw new Exception("Item is already selected");
                selectedItem.item = o;
                selectedItem.unit = o.unit;
            }
        ]);
    }

    public List getExtActions() {
        String s = "ris-close:" + entity.txntype;
        return InvokerUtil.lookupActions( s, [ris: entity ] );
    }

}