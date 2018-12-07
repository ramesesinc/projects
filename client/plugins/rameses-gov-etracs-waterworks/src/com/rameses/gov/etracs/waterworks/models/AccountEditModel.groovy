package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

class AccountEditModel extends ChangeInfoModel {

    @Service("QueryService")
    def qrySvc;
    
    def meterStates =  ["ACTIVE","DISCONNECTED","DEFECTIVE"];
    def acctStates = ["ACTIVE", "INACTIVE"];
    
    public def getEntity() {
        if( schemaName == "waterworks_meter") {
            return caller.entity.meter;
        }
        else {
            return caller.entity;
        }
    }
    
    def getClassificationList() {
        def m = [_schemaname: 'waterworks_classification'];
        m.select = "objid";
        m.where = ["1=1"]
        return qrySvc.getList( m )*.objid;
    }
    
    def getFormFields() {
        def zfields = [];
        if( tag == "state" ) {
            zfields << [name:"state", caption:"Acct Status", datatype: "combo", items:"acctStates"];
        } 
        else if( tag == "address") {
            zfields << [name:'address', caption:'Address', datatype: 'address_editor', preferredSize:"0,50"];
        }
        else if( tag == "owner") {
            zfields << [name:"owner", caption:"Owner", datatype: "lookup", handler:"entity:lookup", expression:"#{data.owner.name}"  ];
        }
        else if( tag == "classification") {
            zfields << [name:"classificationid", caption:"Classification", datatype: "combo", items: "classificationList" ];
        }
        else if( tag == "stuboutnode" ) {
            zfields << [name:"stuboutnode", caption:"Stubout Node", datatype: "lookup", 
                handler:"waterworks_stuboutnode_unassigned:lookup", 
                expression:"#{ (data.stuboutnode.objid==null) ? '' : 'Index No: '+data.stuboutnode.indexno+', Zone:'+ data.stuboutnode.zone.code+ ', Sector:'+data.stuboutnode.sector.code }" ];            
        }
        else if( tag == "meter" ) {
            zfields << [name:"meter", caption:"Meter", datatype: "lookup", 
                        handler:"waterworks_meter_wo_account:lookup", 
                        expression:"#{data.meter.serialno}"];
        }
        else if( tag == "meter_state") {
            zfields << [name:"state", caption:"Status", datatype: "combo", items:"meterStates"];      
        }
        else if( tag == "remarks") {
            zfields << [name:"remarks", caption:"Remarks", datatype: "textarea", showCaption: false, preferredSize:"400,150"];             
        }
        else {
            return null;
        }
       return zfields;
    }

    void beforeInit() {
        handler = {
            caller.reloadEntity();
        }
    }
    

}