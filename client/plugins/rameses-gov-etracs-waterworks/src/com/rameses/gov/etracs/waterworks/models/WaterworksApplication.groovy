package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksApplication extends WorkflowTaskModel {
    
    def tabList;
    def formName = 'waterworks_application:form';
    def selectedRequirement;
    
    def getBarcodeFieldname() {
        return "appno";
    }
    
    public String getTitle() {
        return entity.appno + " - " + task?.title;
    }
    
    public String getWindowTitle() {
        return entity.appno;
    }
    
    public String getFormId() {
        return entity.objid;
    }
    
    void afterOpen() {
        tabList = [];
        def m = [schemaname:getSchemaName(), refid:entity.objid];
        tabList.add( Inv.lookupOpener( "waterworks_application_fee:list", [entity:entity] ));
        tabList.add( Inv.lookupOpener( "requirements:list", m ));
    }
    
    void refresh() {
        tabList.each { op->
            try {
                op.code.refresh();    
            }catch(e){println e.message;}
        }
        binding.refresh();
    }
    
    public boolean isAllowAssignStubout() {
        return true;
    }
    
    public boolean isAllowAssignMeter() {
        return true;
    }
    
    void assignStubout() {
        def h = { o->
            def info = [_schemaname : "waterworks_application"];
            info.objid = entity.objid;
            info.stubout = o;
            persistenceSvc.update( info ); 
            entity.stubout = o;
            binding.refresh();
        };
        Modal.show("waterworks_stubout:lookup", [onselect: h] );        
    }
    
    void assignMeter() {
        def h = {
            info.objid = e.objid;
            info.meter = e.meter;
            info.installer = e.installer;
            info.dtinstalled = e.dtinstalled;
            info.initialreading = e.initialreading;    
        }
        Modal.show("waterworks_meter:lookup", [onselect: h] );        
    }
    
    public void afterSignal() {
        if(task.acctno) {
            MsgBox.alert("Account created " + task.acctno);
        }
    }
    
}