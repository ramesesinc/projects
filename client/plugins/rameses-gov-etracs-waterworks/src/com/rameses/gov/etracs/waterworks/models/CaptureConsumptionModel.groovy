package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.functions.*;

public class CaptureConsumptionModel  {
    
    @Script("ListTypes")
    def listTypes;
    
    @Service("WaterworksComputationService")
    def compSvc;
    
    @Service("WaterworksBillingCycleService")
    def billdateSvc;
    
    @Service("WaterworksAccountService")
    def acctSvc;

    @Binding
    def binding;
    
    def entity;
    def handler;
    def info = [prevreading:0, reading:0, volume: 0, amtpaid: 0, postledger:true];
    def billCycle;
    
    @PropertyChangeListener
    def listener = [
        "info.(reading|prevreading)" : { o->
            info.volume = info.reading - info.prevreading;
        },
        "info.(volume|reading)" : { o->
            info.prevreading = info.reading - info.volume;
            if(info.prevreading<0) info.prevreading = 0;
        }
    ];

    void computeAmount() {
        if( !info.month ) 
            throw new Exception("Period Month is required!");
        def m = [:];
        m.objid = entity.objid;
        m.volume = info.volume;
        def r = compSvc.compute(m);
        info.item = r.item;
        info.amount = r.amount;
        info.remarks = "for month of " + listTypes.months[info.month-1].name + " " + info.year;
    }
    
    void computeBillingCycle() {
        def h = { o->
            def df = new SimpleDateFormat("yyyy-MM-dd");
            def dt = df.parse(o);
            //def dt = DateFunc.getDayAdd( df.parse(o), -1);
            
            def sector = entity.stubout?.zone?.sector?.objid;
            billCycle = billdateSvc.computeBillingDates( [sector: sector, billdate:dt] );
            
            //run this first because we dont want it formatted yet.
            info.year = DateFunc.getYear( billCycle.fromperiod );
            info.month = DateFunc.getMonth( billCycle.fromperiod );
            
            
            billCycle.fromperiod = df.format(billCycle.fromperiod);
            billCycle.toperiod = df.format(billCycle.toperiod);
            billCycle.readingdate = df.format(billCycle.readingdate);
            billCycle.duedate = df.format(billCycle.duedate);
            billCycle.disconnectiondate = df.format(billCycle.disconnectiondate);

            info.dtreading = billCycle.readingdate;
            info.duedate = billCycle.duedate;
            
            binding.refresh();
        }
        Modal.show("date:prompt",[handler:h]);
    }
    
    def doOk() {
        if( !info.duedate ) throw new Exception("Please run 'Enter Ending Period'");
        if( info.prevreading > info.reading) 
            throw new Exception("Prev reading must be less than current reading");
        if( info.prevreading <0 || info.reading < 0 || info.volume <0) 
            throw new Exception("Reading,prevreading,volume must be greater than 0");
        info.account = [objid:entity.objid];
        acctSvc.postConsumption( info );
        if(handler) handler();
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
}