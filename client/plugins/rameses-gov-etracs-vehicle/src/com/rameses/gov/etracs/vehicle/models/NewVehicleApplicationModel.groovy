package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class NewVehicleApplicationModel extends CrudPageFlowModel {
    
    @Service("VehicleApplicationSearchService")
    def searchSvc;

    @Service("DateService")
    def dateSvc;

    @Invoker
    def invoker;

    def vehicletype;

    //lookup Types
    def lookupType = "controlno";
    def owner;
    def controlno;
    def appno;

    def resultList;
    def selectedItem;

    def appTypes = ["NEW", "RENEW" ];
    def apptype;

    @PropertyChangeListener
    def listener = [
        "apptype" : { o->
            entity.apptype = o;
        }
    ];

    def startApp() {
        def props = invoker.properties;
        super.create();

        entity.vehicletypeid = vehicletype.objid;
        entity.vehicletype = [objid: vehicletype.objid];
        entity.apptype = props.apptype;
        entity.txnmode = props.txnmode;
        if( !entity.txnmode ) entity.txnmode = "ONLINE";
        if(entity.txnmode != "CAPTURE") {
            entity.appyear = dateSvc.getServerYear();
        }    
        entity.appdate = dateSvc.getBasicServerDate();
        entity.state = "ACTIVE";
        def path = "search";
        if( entity.txnmode == "CAPTURE" || entity.apptype.matches("NEW")  ) {
            path = "entry";
        }
        return super.start( path );
    }
    
    @FormId
    public String getFormId() {
        return vehicletype.objid + ":" +entity.apptype +":" + entity.txnmode + ":new"; 
    }

    void loadList() {
        def m = [:];
        m.vehicletype = vehicletype;
        m.apptype = entity.apptype;
        m.lookuptype = lookupType;
        m.owner = owner;
        m.appyear = entity.appyear;
        m.controlno = controlno;
        m.appno = appno;
        resultList = searchSvc.getList( m );
        if(!resultList)
            throw new Exception("No records found");
        listModel.reload();
    }

    def viewInfo() {
        if( !selectedItem ) throw new Exception("Please select an item");
        def op = Inv.lookupOpener( "vehicle_application:open", [entity: [objid: selectedItem.objid ]] );
        op.target = "popup";
        return op;
    }

    def listModel = [
        fetchList: { o->
            return resultList;
        }
    ] as BasicListModel;

    

    void saveCopy() {
        if(!selectedItem) throw new Exception("Please select an item");
        
        if ( selectedItem.taskstate == 'payment' ) throw new Exception('This transaction is not fully paid. Please settle payment first');  
        if ( selectedItem.taskstate != 'end' ) throw new Exception('This transaction is not yet completed. Please verify');  
        
        if ( entity.apptype == 'RENEW' ) {
            int yr = entity.appyear-1;
            if ( selectedItem.appyear < yr ) 
                throw new Exception('This transaction is delinquent. Please settle deliquency first'); 
            if ( selectedItem.appyear > yr ) 
                throw new Exception('Application is already current.'); 
        }
        else if ( entity.apptype == 'LATE_RENEWAL' ) {
            if (selectedItem.appyear > (entity.appyear-2))
                throw new Exception('Invalid application year'); 
        }
        else {
            if ( entity.appyear != selectedItem.appyear )
                throw new Exception('Application year must be current'); 
        }
        
        if( !MsgBox.confirm("You are now about to create the application. Please ensure data is correct. Proceed?") ) {
            throw new BreakException();
        }
        entity.owner = selectedItem.owner;
        entity.primaryappid = selectedItem.primaryappid;
        entity.rootappid = selectedItem.rootappid;
        entity.particulars = selectedItem.particulars;
        entity.lastrenewal = selectedItem.appyear; 
        if( selectedItem.controlno ) entity.controlno = selectedItem.controlno;
        saveCreate();
    }

    def prevOwner;
    void checkBeforeChange() {
        if(!selectedItem)
            throw new Exception("Please select an item");
            
        if ( selectedItem.taskstate == 'payment' ) throw new Exception('This transaction is not fully paid. Please settle payment first');  
        if ( selectedItem.taskstate != 'end' ) throw new Exception('This transaction is not yet completed. Please verify');  
        
        if ( entity.appyear != selectedItem.appyear )
            throw new Exception('Application year must be current');             
            
        prevOwner = selectedItem.owner;
        entity.primaryappid = selectedItem.primaryappid;
        entity.rootappid = selectedItem.rootappid;
        entity.particulars = selectedItem.particulars;
        entity.appyear = selectedItem.appyear;
        if( selectedItem.controlno ) entity.controlno = selectedItem.controlno;
    }

    void saveChange() {
        if( prevOwner != null && prevOwner.objid == entity.owner?.objid  ) {
            throw new Exception("Please use another owner. It must not be the same with the previous owner");
        }
        saveCreate();
    }

    public Object onComplete() {
        return Inv.lookupOpener( "vehicle_application:open", [ entity: [objid: entity.objid] ] );
    }

    
}