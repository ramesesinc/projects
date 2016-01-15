package com.rameses.clfc.ledger.adjustment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.clfc.util.*;

class xLoanLedgerAdjustmentController
{
    @Service("LoanLedgerAdjustmentService")
    def adjustmentSvc;
    
    String title = "Ledger Adjustment";
    def entity;
    def paymentTypes = LoanUtil.paymentTypes;
    def payments;
    
    void init() {
        payments = adjustmentSvc.getPayments(entity)
    }
    
    def paymentHandler = [
        fetchList: {o->
            if(payments == null) payments = [];
            return payments;
        }, 
        onColumnUpdate: {item, colName->
            item.adjusted = true;
        }
    ] as EditorListModel
            
    def save() {
        adjustmentSvc.create([ledgerid: entity.ledgerid, payments: payments]);
        MsgBox.alert("Successfully adjusted payments!");
        return close();
    }
    
    def close() {
        return '_close';
    }
}
