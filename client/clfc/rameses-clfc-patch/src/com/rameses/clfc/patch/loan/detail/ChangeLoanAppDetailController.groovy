package com.rameses.clfc.patch.loan.detail;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class ChangeLoanAppDetailController
{
    @Service("ChangeLoanAppDetailService")
    def service;

    def entity;
    def mode;
    def borrowerLookupHandler = Inv.lookupOpener("loanapp_capture:lookup", [:]);
    def routeLookupHandler = InvokerUtil.lookupOpener('route:lookup', [:]);
    def productTypeLookup = Inv.lookupOpener("product_type:lookup", [:]);
    def data;

    def init() {
        entity = [:];
        mode = 'init';
        return "default"
    }

    def close() {
        return "_close";
    }

    def next() {
        mode = 'update';
        data = service.open(entity);
        //data = [:];
        //println 'entity ' + entity;
        /*
        entity.each{k, v->
            data[k] = v;
            if (v instanceof Map) {
                def m = [:];
                v.each{k2, v2->
                    m[k2] = v2;
                }  
                data[k] = m;
            }
        }
        */
        return "main"
    }

    def back() {
        mode = 'init';
        return "default";
    }

    def save() {
        String msg = "<html>Changes made will affect the corresponding ledger for Application <b>" + data.appno + "</b>. Do you want to continue?</html>";
        if (MsgBox.confirm(msg)) {
            entity = service.save(data);
            msg = "<html>Application <b>" + data.appno + "</b> updated successfully.";
            MsgBox.alert(msg);
            return init();
        }
    }
}