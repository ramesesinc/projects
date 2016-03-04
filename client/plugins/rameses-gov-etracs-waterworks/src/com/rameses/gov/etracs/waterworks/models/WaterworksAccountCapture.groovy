package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksAccountCapture extends CrudFormModel {


    @Service('WaterworksClassificationService')
    def classSvc;

    @Service('DateService')
    def dateSvc;

    @Service("EntityService")
    def entitySvc;

    String schemaName = "waterworks_account";
    
    def classifications;
    
    def months = [
        [id: 1, name: "JANUARY"],
        [id: 2, name: "FEBRUARY"],
        [id: 3, name: "MARCH"],
        [id: 4, name: "APRIL"],
        [id: 5, name: "MAY"],
        [id: 6, name: "JUNE"],
        [id: 7, name: "JULY"],
        [id: 8, name: "AUGUST"],
        [id: 9, name: "SEPTEMBER"],
        [id: 10, name: "OCTOBER"],
        [id: 11, name: "NOVEMBER"],
        [id: 12, name: "DECEMBER"]
    ];

    void afterInit() {
        //title = 'Capture Account';
        classifications = classSvc.getList();
    }
    
    void afterCreate() {
        entity = [address:[:]];
        entity.state = 'ACTIVE';
        entity.lastreadingyear = dateSvc.getServerYear();
    }

    void beforeSave( def mode ) {
        if(entity.lastreading < entity.prevreading) 
            throw new Exception('Last Reading must be greater than Prev. Reading!');
    }
    
    /*
    void edit(){
        title = entity.acctno + " (" + entity.acctname + ")";
    }
    */

    @PropertyChangeListener
    def listener = [
        "entity.owner": { o->
            entity.acctname = o.name;
            def owner = entitySvc.open(o);
            entity.phoneno = owner.phoneno;
            entity.mobileno = owner.mobileno;
            entity.email = owner.email;
            binding.refresh("entity.*");
        }
    ]

    def getLookupMeter(){
        def h = {o ->
            entity.meterid = o.objid;
            entity.meter = o;
            binding.refresh("entity.*");
        }
        return Inv.lookupOpener("meter:lookup",[onselect:h]);
   }

    
}