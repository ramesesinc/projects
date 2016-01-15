package com.rameses.clfc.audit.tag

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

abstract class AbstractPaymentTagController
{
    @Binding
    def binding;
    
    abstract String getServiceName();
    /*String getServiceName() {
        return "AmnestyService";
    }*/
    
    def getService() {        
        String name = getServiceName();
        if ((name == null) || (name.trim().length() == 0)) {
          throw new NullPointerException("Please specify a serviceName");
        }
        return InvokerProxy.getInstance().create(name);
    }
    
    def ledgerLookup = Inv.lookupOpener('ledgertag:lookup', [
        onselect: { o->
            ledger = o;
            binding?.refresh();
            paymentsHandler?.reload();
        }
    ]);
    
    def selectedPayment, ledger, refno, txndate;
    def paymentsHandler = [
        getColumns: { o->
            return getService().getColumns(o);
        },
        fetchList: { o->
            if (refno) o.refno = refno;
            if (txndate) o.txndate = txndate;
            if (ledger) o.ledgerid = ledger.objid;
            return getService().getPayments(o);
        }
    ] as BasicListModel;
    
    void refreshPayments() {
        paymentsHandler?.reload();
    }
    
    protected def addTag() {
        if (!selectedPayment) throw new Exception("Please select a payment.");
        
        getService().addTag(selectedPayment);
        paymentsHandler?.reload();
        return null;
    }
    
    protected def removeTag() {
        if (!selectedPayment) thrwo new Exception("Please select a payment.");
        
        getService().removeTag(selectedPayment);
        paymentsHandler?.reload();
        return null;
    }
    
    def close() {
        return "_close";
    }
}

