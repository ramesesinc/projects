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
        resultList = searchSvc.getList( m );
        if(!resultList)
            throw new Exception("No records found");
        listModel.reload();
    }

    def viewInfo() {
        if( !selectedItem ) throw new Exception("Please select an item");
        def op = Inv.lookupOpener( "vehicle_franchise:open", [entity: [objid: selectedItem.objid ]] );
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
        
        if( !MsgBox.confirm("You are now about to create the application. Please ensure data is correct. Proceed?") ) {
            throw new BreakException();
        }
        entity.franchise = [objid: selectedItem.objid, controlno: selectedItem.controlno];
        entity.owner = selectedItem.owner;
        entity.prevappyear = selectedItem.appyear;
        entity.particulars = selectedItem.particulars;
        if( entity.apptype != 'RENEW') {
            entity.appyear = selectedItem.appyear;
        }
        saveCreate();
    }

    def prevOwner;
    void checkBeforeChange() {
        if(!selectedItem)
            throw new Exception("Please select an item");
            
        if ( selectedItem.taskstate == 'payment' ) throw new Exception('This transaction is not fully paid. Please settle payment first');  
        if ( selectedItem.taskstate != 'end' ) throw new Exception('This transaction is not yet completed. Please verify');  
        
        prevOwner = selectedItem.owner;
        entity.franchise = [objid: selectedItem.objid,controlno: selectedItem.controlno];
        entity.owner = null;
        entity.particulars = selectedItem.particulars;
        entity.appyear = selectedItem.appyear;
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

    def getLookupFranchise() {
        def p = [:];
        p.onselect = { o->
            entity.franchise = o;
            binding.refresh("entity.franchise");
        };
        p.query = [ vehicletypeid: vehicletype.objid ];    
        return Inv.lookupOpener("vehicle_franchise:available:lookup", p );
    }
    
}