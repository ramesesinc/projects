package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

class AccountEditModel extends DataEditorModel {

    void fetchData( zfields ) {
        if( tag == "address") {
            setFieldToEdit("address");
            zfields << [name:'address', caption:'Address', datatype: 'address_editor', preferredSize:"0,50"];
        }
        else if( tag == "owner") {
            setFieldToEdit("owner");
            zfields << [name:"owner", caption:"Owner", datatype: "lookup", handler:"entity:lookup", expression:"#{data.owner.name}"  ];
        }
        else if( tag == "units" ) {
            setFieldToEdit("units");
            zfields << [name:"units", caption:"Units", datatype: "integer" ];            
        }
        else if( tag == "stuboutnode" ) {
            setFieldToEdit("stuboutnode");
            zfields << [name:"stuboutnode", caption:"Stubout Node", datatype: "lookup", 
                handler:"waterworks_stuboutnode_unassigned:lookup", 
                expression:"Index No: #{data.stuboutnode.indexno}, Zone: #{data.stuboutnode.zone.code}, Sector: #{data.stuboutnode.sector.code}" ];            
        }
        else if( tag == "reading" ) {
            setFieldToEdit("meter.lastreading");
            setFieldToEdit("meter.lastreadingdate");
            zfields << [name:"meter.lastreading", caption:"Last Reading", datatype: "integer"]            
            zfields << [name:"meter.lastreadingdate", caption:"Last Reading Date", datatype: "date" ];
        }
        else if( tag.matches("change_meter|attach_meter") ) {
            setFieldToEdit("meter");
            message = "You are about to " + tag.replace("_", " ") + ". Specify meter, enter remarks and proceed";
            zfields << [name:"meter", caption:"Meter", datatype: "lookup", 
                        handler:"waterworks_meter_wo_account:lookup", required: true,
                        expression:"#{data.meter.serialno}"]            
        }
        else {
            message = "You are about to " + tag.replace("_", " ") + " this meter. Enter remarks and click OK to proceed";
        }
    }

    void beforeInit() {
        handler = {
            caller.reloadEntity();
        }
    }
    
    void setDataForUpdate() {
        data.objid = entity.objid; //set the primary key
        if( tag == "detach" ) {
            data.meterid =  null;
        }
        else if( tag=="reading") {
            data._schemaname = "waterworks_meter";
            data.objid = entity.meterid;
        }
        else if( tag.matches("disconnect|reconnect|set_as_defective") ) {
            if( tag == "disconnect" ) data.state = "DISCONNECTED";
            else if( tag == "set_as_defective" ) data.state = "DEFECTIVE";
            else data.state = "ACTIVE";
            data._schemaname = "waterworks_meter";
            data.objid = entity.meterid;
        }
        else {
            if( data.stuboutnode?.objid )data.stuboutnodeid = data.stuboutnode.objid;
            if( data.meter?.objid  ) data.meterid = data.meter.objid;
        }
    }

}