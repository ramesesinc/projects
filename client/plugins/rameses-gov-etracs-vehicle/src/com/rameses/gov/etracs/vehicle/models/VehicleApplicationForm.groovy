package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.vehicle.models.*;
import com.rameses.enterprise.models.*;

public class VehicleApplicationForm extends WorkflowTaskModel {
    

    boolean viewReportAllowed = false; 
    
    String getFormName() {
        return getSchemaName() + ":form";
    }
    
    String getBarcodeFieldname() {
        return "appno";
    }
    
    public String getTitle() {
        String state = task?.title;
        if( !state ) state = "";
        else state = " [" + task?.title + "]";
        return entity.appno + state ;
    }
    
    public String getWindowTitle() {
        return entity.appno;
    }
    
    public String getFormId() {
        return entity.objid;
    }
    
    def feeListModel = [
        fetchList: { o->
            return entity.fees;
        }
    ] as BasicListModel;
    
    def infoListModel = [
        fetchList: { o->
            return entity.infos;
        }
    ] as BasicListModel;

    def paymentListModel = [
        fetchList : {
            return entity.payments;
        },
        onOpenItem: { o->
            return Inv.lookupOpener( "cashreceiptinfo:open", [entity: [objid:o.refid] ] );
        }
    ] as BasicListModel;
    

    void viewTrackingno() {
        Modal.show( "show_vehicle_trackingno", [appno: entity.appno] );
    }
 
    def printPermit() { 
        String vehicletype = getSchemaName().toString();
        int idx = vehicletype.lastIndexOf('_'); 
        if ( idx > 0 ) { 
            vehicletype = vehicletype.substring(idx+1);
        }
        
        def opener = Inv.lookupOpener('vehicle_application_permit:print', [vehicletype: vehicletype, entity: entity]);
        opener.target = 'self'; 
        return opener;
    }
}