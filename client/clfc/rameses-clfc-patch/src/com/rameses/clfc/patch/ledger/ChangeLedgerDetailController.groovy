package com.rameses.clfc.patch.loan.detail;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class ChangeLedgerDetailController
{
    @Binding
    def binding;
    
    @Service("ChangeLedgerDetailService")
    def service;

    def entity;
    def mode;
    def borrowerLookupHandler = Inv.lookupOpener("ledger_capture:lookup", [:]);
    //def routeLookupHandler = InvokerUtil.lookupOpener('route:lookup', [:]);
    def data;
    def paymentMethodList = [
        [value: 'schedule', caption: 'Schedule/Regular'],
        [value: 'over', caption: 'Overpayment']
    ]
    def paymentmethod;
    
    @PropertyChangeListener
    def listener = [
        "paymentmethod": { o->
            if (o.value == 'schedule') {
                data.overpaymentamount = 0;
                binding.refresh('data.overpaymentamount');
            }
        }
    ]

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
        data = [:];
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
        paymentmethod = paymentMethodList.find{ it.value == data.paymentmethod }
        return "main";
    }

    def back() {
        mode = 'init';
        return "default";
    }

    def save() {
        String msg = "<html>Changes made will affect the corresponding ledger for Application <b>" + data.appno + "</b>. Do you want to continue?</html>";
        if (MsgBox.confirm(msg)) {
            data.ledgerid = data.objid;
            data.paymentmethod = paymentmethod?.value;
            entity = service.save(data);
            msg = "<html>Application <b>" + data.appno + "</b> updated successfully.";
            MsgBox.alert(msg);
            return init();
        }
    }
}