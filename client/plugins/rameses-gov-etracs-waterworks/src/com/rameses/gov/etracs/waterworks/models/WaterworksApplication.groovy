package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksApplication extends WorkflowTaskModel {
    
    @Script("AddressUtil")
    def addressUtil;
    
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
    
    void refresh() {
        feeList.reload();
        requirementList.reload();
    }
    
    public boolean isAllowAssignStubout() {
        return true;
    }
    
    public boolean isAllowAssignMeter() {
        return true;
    }
    
    void assignStubout() {
        boolean pass = false;
        def stuboutid;
        def h = {o->
            stuboutid = o.objid;
            pass = true;
        }
        Modal.show("waterworks_stubout:lookup", [onselect: h] );
        if( !pass) return;
        pass = false;
        h = { o->
            if( o.application?.appno ) throw new Exception("There is already an account assigned. Choose another");
            def m = [_schemaname: schemaName, objid: entity.objid, stuboutnodeid: o.objid];
            persistenceService.update( m );
            entity.stuboutnode = o;
            binding.refresh();
        }
        Modal.show("waterworks_stubout_node_unassigned_application:lookup", [onselect: h, stuboutid: stuboutid] );
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

    def editMeterInfo() {
        def m = [:];
        m.handler = { o->
            o.address.text = addressUtil.format(o.address);
            entity.address = o.address;
            //binding.refresh();
        }
        //m.fields = "acctname,meter.serialno,address.street,address.barangay.name";
        m.fields = "address.*";
        m.entity = entity;
        m.schema = schema;
        Modal.show("selected_field:entry",m);
    }
    
    def feeList = [
        fetchList: {o-> 
            def  m = [_schemaname:'waterworks_application_fee'];
            m.findBy = [parentid: entity.objid];
            def feeList = queryService.getList(m);
            entity.total = feeList.sum{ it.amount };
            return feeList; 
        }
    ] as BasicListModel;    

    def requirementList = [
        fetchList: {o-> 
            def  m = [_schemaname:'waterworks_application_requirement'];
            m.findBy = [parentid: entity.objid];
            return queryService.getList(m);
        }
    ] as BasicListModel;   
    
    
}