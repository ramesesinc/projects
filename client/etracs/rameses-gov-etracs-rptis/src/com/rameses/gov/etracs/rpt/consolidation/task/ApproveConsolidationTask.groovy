package com.rameses.gov.etracs.rpt.consolidation.task;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;


public class ApproveConsolidationTask implements Runnable{
    def svc;
    def entity;
    def oncomplete;
    def onerror;
    def showinfo;

    public void run(){
        try{
            showinfo('Initializing');
            svc.initApproveConsolidation(entity);
            showinfo(' .... Done\n');
        
        
            showinfo('Assigning new TD No. to Consolidated Land and Affected RPUs');
            entity.putAll( svc.assignNewTdNos(entity) );
            showinfo(' .... Done\n');            
            
            showinfo('Processing Consolidated Land ');
            showinfo('Creating new Consolidated Land FAAS for TD No. ' + entity.newtdno );
            entity.putAll( svc.approveConsolidatedLandFaas(entity))
            showinfo(' .... Done\n\n');
                
            showinfo('Processing Affected RPUs\n');
            svc.getAffectedRpusForApproval(entity.objid).each{ arpu ->
                showinfo('Creating new Affected RPU FAAS for TD No. ' + arpu.newtdno );
                svc.approveAffectedRpuFaasRecord(entity, arpu);
                showinfo(' .... Done\n');
            }
            
            showinfo('Consolidation Approval')
            svc.approveConsolidation(entity);
            entity.state = 'APPROVED';
            showinfo(' .... Done\n');
            
            oncomplete()
        }
        catch(e){
            onerror('\n\n' + e.message )
        }
    }
    
    void doSleep(){
        try{
            Thread.sleep(1000);
        }
        catch(e){
            ;
        }
    }
}