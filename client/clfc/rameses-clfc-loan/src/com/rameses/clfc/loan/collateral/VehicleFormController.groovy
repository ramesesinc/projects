package com.rameses.clfc.loan.collateral;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osirsi2.client.*;
import com.rameses.clfc.util.*;
import com.rameses.clfc.loan.controller.*;

class VehicleFormController extends PopupMasterController
{
    def kinds = LoanUtil.vehicleKinds;
    def uses = LoanUtil.vehicleUses;
    def orcr = true

    @PropertyChangeListener
    def listener = [
        "orcr": {o->
            if(o == false) entity.orcr = [:];
        }
    ]

    public void afterCreate(data) {
        if (entity.orcr == null) entity.orcr = [:];
    }
            
    public def open() {
        if (entity.orcr == null) entity.orcr = [:];
        if (!entity.orcr.crno) orcr = false;
        return null;
    }
}
