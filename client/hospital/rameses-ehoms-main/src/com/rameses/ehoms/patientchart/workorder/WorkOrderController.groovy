package com.rameses.ehoms.patientchart.workorder;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;

public abstract class WorkOrderController {

     @Service("PatientChartWorkOrderService")
     def workOrderSvc;
            
     @Binding
     def binding;
        
     def entity;
     def parent;
     def mode;
     def saveHandler;
     def submitHandler;
     def billitems = [];
     def selectedBillItem;
     def billTotal;
     def state;

     public abstract String getActivityType();
     
     
     //default for info is list. with the exception of drug medication
     public def createInfo() {
        return [];
     }

     //if true, this will auto close the activity.
     public boolean getAutoCloseActivity() {
        return false;
     }

     public void beforeSave() {;}
     public void afterCreate() {;}
     public void afterOpen() {;}

     def create() {
        parent = entity;
        entity = [objid: 'WOLAB'+new UID()];
        entity.info = createInfo();
        entity.activitytype = getActivityType();
        entity.section = parent.section;
        entity.parentid = parent.objid;
        entity.notifysms = 1;
        mode = 'create';
        afterCreate();
        return mode;
     }

    def open() {
       entity = workOrderSvc.open( entity );
       parent = entity.remove("parent");
       mode = 'read';
       state = entity.state?.toLowerCase();
       afterOpen();
       return mode;
    }

    def save() {
        beforeSave();
        workOrderSvc.create( entity  );
        if(saveHandler) saveHandler(entity);
        MsgBox.alert('Work Order successully created!');
        return "_exit";
    }

    def accept() {
        def r = workOrderSvc.createBillItems(entity);
        entity.billitems = r.billitems;
        entity.total = r.total;
        mode = 'accept';
        billItemModel.reload();
        return mode;
    }

    def submit() {
        if( MsgBox.confirm('You are about to accept this order and activate the task. Proceed?')) {
            entity.closeActivity = getAutoCloseActivity();
            workOrderSvc.activate( entity );
            if(submitHandler) submitHandler(entity);
            MsgBox.alert('Work Order activated');
            return "_close";
        }
    }
            
    def billItemModel = [
        fetchList: { o->
            return entity.billitems;
        }
    ] as BasicListModel;


}