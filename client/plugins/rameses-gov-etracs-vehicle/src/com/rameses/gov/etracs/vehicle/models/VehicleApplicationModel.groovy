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
    
    @Service("SchemaService")
    def schemaSvc;

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

    def fields;
    void afterOpen() {
        fields = [];
        if( !entity.vehicletype.allowedfields ) throw new Exception("Error in opening application form. vehicletype allowed fields must not be null");
        schemaSvc.getSchema( [name:"vehicle_application_unit" ] )?.fields.collect{
            if( it.name.matches("franchise.controlno")) {
                fields.add(0, it);
                return;
            }
            if(!it.included) return;
            def n = it.name;
            if(n.contains("_")) n = it.name.split("_")[0];
            if( n.matches(entity.vehicletype.allowedfields)) {
                fields << it;
            }
        };
        loadInfos();
    }
    
    def selectedUnit;
    def unitListModel = [
        getColumnList: {
            return fields;
        },
        fetchList : { o->
            def m = [_schemaname: "vehicle_application_unit" ];
            m.findBy = [ appid: entity.objid ];
            m.orderBy = "franchise.controlno";
            return queryService.getList( m );
        }

    ] as BasicListModel;

    def addUnit() {
        def p = [:];
        p.handler = { o->
            o._schemaname = 'vehicle_application_unit';
            persistenceService.create( o );
            unitListModel.reload();
        };
        p.vehicletype = entity.vehicletype;
        p.app = entity;
        return Inv.lookupOpener("vehicle_application_unit:create", p );
    }

    def editUnit() {
        if(!selectedUnit) throw new Exception("Please select a unit");
        def p = [:];
        p.handler = { o->
            o._schemaname = 'vehicle_application_unit';
            o.findBy = [objid: o.objid];
            persistenceService.update( o );
            unitListModel.reload();
        };
        p.vehicletype = entity.vehicletype;
        p.entity = selectedUnit;
        return Inv.lookupOpener("vehicle_application_unit:edit", p );
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
            def m = [_schemaname: "vehicle_application_fee"];
            m.findBy = [parentid: entity.objid ];
            m.orderBy = "appyear,sortorder";
            return queryService.getList(m);
        }
    ] as BasicListModel;
    
    def infos = [];
    void loadInfos() {
        def m = [_schemaname: "vehicle_application_info"];
        m.findBy = [parentid: entity.objid ];
        m.orderBy = "sortorder";
        infos = queryService.getList(m);
        infos.each {
            it.putAll( it.remove("variable") );
            if( it.stringvalue ) it.value = it.stringvalue;
            else if( it.intvalue ) it.value = it.intvalue;
            else if( it.decimalvalue ) it.value = it.decimalvalue;
            else if( it.booleanvalue ) it.value = it.booleanvalue;
        }
    }
    
    def infoListModel = [
        fetchList: { o->
            return infos;
        }
    ] as BasicListModel;

    /*
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
    */

    void viewTrackingno() {
        Modal.show( "show_vehicle_trackingno", [appno: entity.appno] );
    }
 
    

    void assessLateRenewal() {
        boolean bstop = false;
        boolean infoloaded = false;
        int startYr = entity.lastrenewal + 1;
        int endYr = entity.appyear - 1;
        def localInfos = null;
        ( startYr .. endYr ).each {
            if( bstop ) return;
            def app = [:];
            app.putAll( entity );
            app.appyear = it;
            def p = [:];
            p.rulename = "vehicleassessment"
            p.params = [application: app ];
            p.include_items = true;
            p.include_billitems = false;
            if(infos) {
                p.defaultInfos = infos;
                p.infos = localInfos;
            }
            p.handler = { result->
                if( result == null ) {
                    bstop = true;
                    return;
                }
                if(!infoloaded) {
                    infoloaded = true;
                    loadInfos();
                    localInfos = infos;
                }
            }
            try {
                Modal.show( "billing_rule", p );
            }
            catch(BreakException be) {;}
        }
        if( bstop ) return;
        reload();
        feeListModel.reload(); 
        infoListModel.reload(); 
    }
    
    void assessBasic() {
        def p = [:];
        p.rulename = "vehicleassessment"
        p.params = [application: entity ];
        p.include_items = true;
        p.include_billitems = false;
        if(infos) p.defaultInfos = infos;
        p.handler = { result->
            if( result !=null ) { 
                reload();
                loadInfos();
                feeListModel.reload(); 
                infoListModel.reload(); 
            }
        }
        Modal.show( "billing_rule", p );
    }

    void assess() {
        if( entity.apptype == 'LATE_RENEWAL') {
            assessLateRenewal();
        }
        else {
            assessBasic();
        }
    }
    
    //PRINTOUTS
    def printApplication() {
        
    }
    def printBill() {
        
    }
    def printPermit() { 
        def opener = Inv.lookupOpener('vehicle_application_permit:print', [vehicletype: vehicletype, entity: entity]);
        opener.target = 'self'; 
        return opener;
    }

}