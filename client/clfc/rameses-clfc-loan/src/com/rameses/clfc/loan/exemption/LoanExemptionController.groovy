package com.rameses.clfc.loan.exemption;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

class LoanExemptionController
{
    @Binding
    def binding;
    
    @Service('LoanExemptionService')
    def svc;
    
    def batchpaymentid;
    def txndate;
    def data = [ reason: '' ];
    def mode = 'read';
    void init() {
        mode = 'create';
        data.forexemptions = svc.getForExemptions([batchpaymentid: batchpaymentid, txndate: txndate]);        
    }
    
    def close() {
        return "_close";
    }
    
    def save() {
        data = svc.createMultiple(data);
        mode = 'read';
        binding.refresh();
    }
    
    def forexemptionsHandler = [
        fetchList: { return data.forexemptions; }
    ] as EditorListModel;
}