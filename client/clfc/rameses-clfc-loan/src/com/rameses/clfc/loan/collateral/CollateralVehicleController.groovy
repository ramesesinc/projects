package com.rameses.clfc.loan.collateral;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

class CollateralVehicleController
{
    @Binding
    def binding;
    def loanappid, collateral, mode, beforeSaveHandlers;
    
    def htmlbuilder=new CollateralHtmlBuilder();
    def selectedVehicle;
    def vehicleHandler = [
        fetchList: {o->
            if( !collateral?.vehicles ) collateral.vehicles = [];
            collateral.vehicles.each{ it._filetype = "vehicle" }
            return collateral.vehicles;
        },
        onRemoveItem: {o->
            return removeVehicleImpl(o); 
        },
        getOpenerParams: {o->
            return [mode: mode, caller:this];
        }
    ] as EditorListModel;
    
    def addVehicle() {
        def handler = {vehicle->
            vehicle.parentid = loanappid;
            collateral.vehicles.add(vehicle);
            vehicleHandler.reload();
        }
        return InvokerUtil.lookupOpener("vehicle:create", [handler:handler]);
    }
    
    void removeVehicle() {
        removeVehicleImpl(selectedVehicle); 
    }
    
    boolean removeVehicleImpl(o) {
        if (mode == 'read') return false;
        if (MsgBox.confirm("You are about to remove this vehicle. Continue?")) {
            collateral.vehicles.remove(o);
            return true;
        } else { 
            return false; 
        } 
    }
    
    def getHtmlview() {
        return htmlbuilder.buildVehicle(selectedVehicle);
    }
}