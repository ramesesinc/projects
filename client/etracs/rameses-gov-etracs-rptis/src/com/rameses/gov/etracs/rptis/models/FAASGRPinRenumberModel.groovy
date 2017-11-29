package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class FAASGRPinRenumberModel
{
    @Binding
    def binding;
    
    @Service('LGUService')
    def lguSvc;
    
    @Service('GeneralRevisionPinRenumberService')
    def svc
    
    String title = 'Re-PIN General Revision FAAS';
    
    def entity;
    def mode;
    
    def MODE_INIT = 'init';
    def MODE_PROCESSING = 'processing';
    def task;
    def logs;
    
    def numberingOptions = [
        [code:'RESET', title:'Renumber PIN consecutively starting from one'],
        [code:'NEWPIN', title:'Modify PIN based on NEW Format']
    ]
    
    void init(){
        entity = [approvefaas:false];
        entity.ry = svc.getCurrentRevisionYear();
        logs = '';
        mode = MODE_INIT;
    }
    
    void process(){
        if (MsgBox.confirm('Renumber PIN for this Section?')){
            logs = '';
            task = new RepinTask();
            task.svc = svc;
            task.entity = entity;
            task.logStatus = logStatus;
            task.oncomplete = oncomplete;
            Thread t = new Thread(task);
            t.start();
            mode = MODE_PROCESSING;
        }
    }
    
    void cancelProcess(){
        task.cancelled = true;
        logs = '';
        mode = MODE_INIT;
        binding?.refresh();
    }
    
    def oncomplete = {
        mode = MODE_INIT;
        binding.refresh();
    }
    
    def logStatus = {
        logs += it;
        binding.refresh('logs');
    }
    
    
    def getLgus(){
        return lguSvc.getLgus();
    }

    def getBarangays(){
        if (! entity.lgu)
        return [];
        return lguSvc.lookupBarangaysByRootId(entity.lgu?.objid);
    }
    
}

class RepinTask implements Runnable {
    def svc;
    def entity;
    def logStatus;
    def oncomplete;
    boolean cancelled;
    boolean error;
        
    public void run(){
        cancelled = false;
        error = false;
        
        try{
            renumberPin();
            approveFaases();
        }
        catch(e){
            error = true;
            logStatus('\n[ERROR] ' + e.message);
        }

        if (cancelled){
            logStatus('Processing is cancelled.')
        }
        else if (!error){
            if (entity.approvefaas == false)
                logStatus('\nPIN Renumbering completed successfully.');
            else 
                logStatus('\nPIN Renumbering and FAAS Approval completed successfully.');
        }
            
        oncomplete();
                
    }
    
    void renumberPin(){
        logStatus('Fetching lands to renumber ... ');
        def landfaases = svc.getLandsForRepinning(entity);
        logStatus('done\n');
            
        logStatus('=========================================\n');
        logStatus('Renumbering PIN\n');
        logStatus('=========================================\n');
        for(int i=0; i<landfaases.size(); i++){
            if (cancelled) break;
            def landfaas = landfaases[i];
            landfaas.idx = i+1;
            landfaas.numberingoption = entity.numberingoption

            try{
                logStatus('FAAS PIN # ' + landfaas.pin + ' ... ')
                landfaas.barangay = entity.barangay;
                svc.repinLand(landfaas);
                logStatus('Done\n');
            }
            catch(e){
                throw new Exception('FAAS PIN # ' + landfaas.pin + ' : ' + e.message)
            }
        }
    }
    
    
    void approveFaases(){
        if (entity.approvefaas == false)
            return;
        
        logStatus('\n\n=========================================\n');
        logStatus('Approving FAAS\n');
        logStatus('=========================================\n');
        
        def faases = svc.getFaasForApproval(entity);
        
        for(int i=0; i<faases.size(); i++){
            if (cancelled) break;
            def faas = faases[i];
            
            try{
                logStatus('Approving FAAS ' + faas.fullpin + ' ... ')
                if ('interim'.equalsIgnoreCase(faas.state))
                    throw new Exception('FAAS is not yet submitted for Approval.')
                  
                if ('forapproval'.equalsIgnoreCase(faas.state)){
                    svc.approveFaas(faas);
                    logStatus('Done\n');
                }
                else if ('current'.equalsIgnoreCase(faas.state)){
                    logStatus('Done\n');
                }
                else{
                    throw new Exception('Invalid FAAS State ' + faas.state + '.')
                }
            }
            catch(e){
                throw new Exception('FAAS ' + faas.fullpin + ' : ' + e.message)
            }
        }
    }
}