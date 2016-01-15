package com.rameses.clfc.loan.business;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.clfc.util.LoanUtil;
import java.rmi.server.UID;
import com.rameses.clfc.loan.controller.*;

class BusinessFormController extends PopupMasterController
{
    def kindTypes = LoanUtil.kindTypes;
    def occupancyTypes = LoanUtil.occupancyTypes;
    def rentTypes = LoanUtil.rentTypes;
    def ownershipTypes = LoanUtil.ownershipTypes;
    
    @PropertyChangeListener
    def listener = [
        "entity.occupancy.type": {o->
            if( o != 'RENTED' ) {
                entity.occupancy.renttype = null;
                entity.occupancy.rentamount = null;
            }
        }
    ];
    
    public def createEntity() {
        return [ objid: 'BUSS'+new UID(), occupancy:[:] ]
    }
}
