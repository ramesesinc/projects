package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class BuildRPTDelinquencyModel 
{
    @Binding
    def binding;
    
    @Service('LandTaxReportDelinquencyService') 
    def svc;
    
    @Service('DateService')
    def dtSvc;
    
    @Service('LGUService')
    def lguSvc
        
    String title = 'Build Realty Tax Delinquency';
    
    def task;
    
    def msg;
    def mode;
    def entity = [:];
    
    void init(){
        entity.dtcomputed = dtSvc.getServerDate();
        msg = 'Click Build to rebuild the realty tax delinquency listing.';
        mode = 'init';
    }
   
    def oncomplete = {
        mode = 'init';
        task = null;
        msg = 'Delinquency listing has been successfully generated.';
        binding.refresh('.*');
    }
    
    def updateStatus = {o ->
        msg = o; 
        binding.refresh('msg');
    }
    
    def oncancel = {
        init(); 
        binding.refresh(); 
    } 
    
       
    void doBuild() {
        svc.validateDateComputed(entity.dtcomputed);
        
        task = new RPTDelinquencyTask([
            svc          : svc,
            currentdate  : entity.dtcomputed,
            oncomplete   : oncomplete,
            oncancel     : oncancel, 
            updateStatus : updateStatus,
            params       : entity 
        ]);
        task.start();
        mode = 'processing';
    }

    void doCancel(){
        task?.cancel(); 
        init();
    }

    def getBarangays(){
        if (entity.barangaylist == null) {
            entity.barangaylist = svc.getBarangayList(); 
        } 
        return entity.barangaylist; 
    } 
}
