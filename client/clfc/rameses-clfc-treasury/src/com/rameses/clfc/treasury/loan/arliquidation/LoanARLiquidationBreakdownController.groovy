package com.rameses.clfc.treasury.loan.arliquidation

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LoanARLiquidationBreakdownController 
{
    def data, handler;
    def mode = 'read';
    
    String title = "AR Liquidation Breakdown";
    
    void init() {
        data = [objid: 'LARB' + new UID()];
    }
    
    def doOk() {
        if (handler) handler(data);
        return '_close';
    }
    
    def doCancel() {
        return '_close';
    }
}

