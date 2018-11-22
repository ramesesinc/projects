package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.functions.*;
import com.rameses.seti2.models.*;

public class CaptureConsumptionModel extends CrudFormModel {
    
    @Service("WaterworksComputationService")
    def compSvc;
    
    @Service("DateService")
    def dateSvc;
    
    def dateFormatter = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
    def parent;
    
    def handler;
    def hasErrs;
    
    def txnModes = ["BEGIN_BALANCE", "CAPTURE" ];
    
    @FormTitle
    String title = "Monthly Consumption";
    
    void afterCreate() {
        entity.txnmode = "CAPTURE";
        entity.acctid = masterEntity.objid;
        entity.account = masterEntity;
        entity.meterid = masterEntity.meter?.objid;
        entity.meter = masterEntity.meter;
        entity.volume = 0;
    }
    
    public def getMasterEntity() {
        return caller.masterEntity;
    }
    
    void afterSave() {
        if(handler) 
            handler();
        else 
            caller.reload();
    }
    
    void calculate() {
        def meter = masterEntity.meter; 
        if( meter?.objid ) {
            def f = [:];
            f.handler = { o->
                def z = [acct: masterEntity ];
                z.putAll(o);
                entity.putAll(o);
                def res = compSvc.compute( z );
                entity.putAll( res );
                binding.refresh();
            }
            f.data = [ prevreading:entity.prevreading, reading:entity.reading,  volume: 0]
            f.fields = [];
            f.fields << [caption:'Previous Reading', name:'prevreading', type:'integer', required:true];
            f.fields << [caption:'Current Reading', name:'reading', type:'integer', required:true];
            
            //display volume if meter state is defective
            if(meter.state == "DEFECTIVE") {
                f.fields << [caption:'Volume', name:'volume', type:'integer', required:true];                
            }
            Modal.show("dynamic:form", f, [title:'Calculate Amount']);
        }
        else {
            def z = [acct: masterEntity ];
            def res = compSvc.compute( z );
            entity.putAll(res);
            binding.refresh();
        }
    }

   
   
    
}