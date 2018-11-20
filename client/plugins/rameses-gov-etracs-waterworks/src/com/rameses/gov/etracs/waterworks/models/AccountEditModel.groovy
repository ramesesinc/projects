package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

class AccountEditModel extends DataEditorModel {

    def getEntity() {
        if(schemaName == "waterworks_account") {
            return super.getEntity();
        }
        else if( schemaName == "waterworks_meter") {
            return super.getEntity().meter;
        }
    }
    
    def getFormFields() {
        def zfields = [];
        if( tag == "address") {
            zfields << [name:'address', caption:'Address', datatype: 'address_editor', preferredSize:"0,50"];
        }
        else if( tag == "owner") {
            zfields << [name:"owner", caption:"Owner", datatype: "lookup", handler:"entity:lookup", expression:"#{data.owner.name}"  ];
        }
        else if( tag == "stuboutnode" ) {
            zfields << [name:"stuboutnode", caption:"Stubout Node", datatype: "lookup", 
                handler:"waterworks_stuboutnode_unassigned:lookup", 
                expression:"#{ (data.stuboutnode.objid==null) ? '' : 'Index No: '+data.stuboutnode.indexno+', Zone:'+ data.stuboutnode.zone.code+ ', Sector:'+data.stuboutnode.sector.code }" ];            
        }
        else if( tag == "meter" ) {
            zfields << [name:"meter", caption:"Meter", datatype: "lookup", 
                        handler:"waterworks_meter_wo_account:lookup", 
                        expression:"#{data.meter.serialno}"]            
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