package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;


public class WaterworksAccountCapture {

    @Service('WaterworksAccountService')
    def acctSvc;

    @Service('WaterworksClassificationService')
    def classSvc;

    @Service('DateService')
    def dateSvc;

    @Binding
    def binding;
    
    def title;
    def entity;
    def classifications;
    def months;

    void init(){
        entity = [:];
        entity.state = 'ACTIVE';
        entity.lastreadingyear = dateSvc.getServerYear();
        title = 'Capture Account';
        classifications = classSvc.getList();
        months = [
            [id: 1, name: "JAN"],
            [id: 2, name: "FEB"],
            [id: 3, name: "MAR"],
            [id: 4, name: "APR"],
            [id: 5, name: "MAY"],
            [id: 6, name: "JUN"],
            [id: 7, name: "JUL"],
            [id: 8, name: "AUG"],
            [id: 9, name: "SEP"],
            [id: 10, name: "OCT"],
            [id: 11, name: "NOV"],
            [id: 12, name: "DEC"]
        ];
    }

    def save(){
        if(!MsgBox.confirm("You are about to create this record. Continue?")) return;
        entity.classificationid = entity.classification.objid;
        entity.areaid = entity.area.objid;
        entity.lastreadingmonth = entity.month.id;
        if(entity.lastreading < entity.prevreading) throw new Exception('Last Reading must be greater than Prev. Reading!');
        acctSvc.create(entity);
        return '_close';
    }

    def getLookupEntity(){
        def h = {o ->
            entity.owner = o;
            binding.refresh("entity.*");
        }
        return Inv.lookupOpener("entity:lookup",[onselect:h]);
   }

    def getLookupMeter(){
        def h = {o ->
            entity.meterid = o.objid;
            entity.meter = o;
            binding.refresh("entity.*");
        }
        return Inv.lookupOpener("meter:lookup",[onselect:h]);
   }

    def editAddress(){
        def handler = {o ->
            def text = "";
            if(o.unitno) text = text + o.unitno + ", ";
            if(o.bldgno) text = text + o.bldgno + ", ";
            if(o.bldgname) text = text + o.bldgname + ", ";
            if(o.subdivision) text = text + o.subdivision + ", ";
            if(o.street) text = text + o.street + ", \n";
            if(o.barangay.name) text = text + o.barangay.name + ", ";
            if(o.municipality) text = text + o.municipality + "";
            entity.address = o;
            entity.address.barangay = o.barangay.name;
            entity.address.text = text;
            binding.refresh("entity.*");
        }
        return Inv.lookupOpener("address:lookup",[entity:entity.address,handler:handler]);
    }
    
}