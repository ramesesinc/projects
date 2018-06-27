package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.vehicle.models.*;
import com.rameses.enterprise.models.*;

public class VehicleApplicationModel extends WorkflowTaskModel {
    

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
    
    def fieldMatch = [ 
        motorized : "franchise.controlno|plateno|engineno|bodyno|sidecarno|make|model|color|chassisno|sidecarcolor|crname", 
        manual: "franchise.controlno|plateno|driver.name"
    ]

    def fields = [
        [ name:'franchise.controlno', caption: 'Franchise No'],
        [ name:'plateno', caption: 'Plate No'],
        [ name:"engineno", caption:"Engine No"],
        [ name:"bodyno", caption:"Body No"],
        [ name:"sidecarno", caption:"Sidecar No"],
        [ name:"make", caption:"Make"],
        [ name:"model", caption:"Model"],
        [ name:"color", caption:"Color"],
        [ name:"chassisno", caption:"Chassis No"],
        [ name:"sidecarcolor", caption:"Sidecar Color"],
        [ name:"crname", caption:"CR Name"],
        [ name:"driver.name", caption:"Driver"],
    ];

    def selectedUnit;
    def unitListModel = [
        getColumnList: {
            def s = fieldMatch.get( entity.vehicletype.guihandler );
            return fields.findAll{ it.name.matches(s) };
        },
        fetchList : { o->
            def m = [_schemaname: "vehicle_application_unit" ];
            m.findBy = [ appid: entity.objid ];
            return queryService.getList( m );
        }

    ] as BasicListModel;

    def addUnit() {
        def s = entity.vehicletype.guihandler;
        def p = [:];
        p.handler = { o->
            o._schemaname = 'vehicle_application_unit';
            persistenceService.create( o );
            unitListModel.reload();
        };
        p.vehicletype = entity.vehicletype;
        p.app = entity;
        return Inv.lookupOpener("vehicle_application_unit:" + s + ":create", p );
    }

    def editUnit() {
        if(!selectedUnit) throw new Exception("Please select a unit");
        def s = entity.vehicletype.guihandler;
        def p = [:];
        p.handler = { o->
            o._schemaname = 'vehicle_application_unit';
            o.findBy = [objid: o.objid];
            persistenceService.update( o );
            unitListModel.reload();
        };
        p.vehicletype = entity.vehicletype;
        p.entity = selectedUnit;
        return Inv.lookupOpener("vehicle_application_unit:" + s + ":edit", p );
    }
    
    def removeUnit() {
        if(!selectedUnit) throw new Exception("Please select a unit");
        if(!MsgBox.confirm("You are about to remove this entry. Proceed?")) return null;
        def o = [:];
        selectedUnit._schemaname = 'vehicle_application_unit';
        persistenceService.removeEntity( selectedUnit );
        unitListModel.reload();
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
        onOpenItem: { o,colName ->
            def op = Inv.lookupOpener( "cashreceiptinfo:open", [entity: [objid:o.refid] ] );
            op.target = 'popup';
            return op;
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

    void assess() {
        def p = [:];
        p.rulename = "vehiclebilling"
        p.params = [billdate:entity.receiptdate];
        p.handler = { result->
            MsgBox.alert("finished rule");
        }
        Modal.show( "billing_rule", p );
    }

}