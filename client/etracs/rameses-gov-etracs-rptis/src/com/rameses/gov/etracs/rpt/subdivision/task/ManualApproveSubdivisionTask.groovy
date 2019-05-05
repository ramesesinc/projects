package com.rameses.gov.etracs.rpt.subdivision.task;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;
import com.rameses.util.MapBeanUtils;


public class ManualApproveSubdivisionTask implements Runnable{
    def svc;
    def entity;
    def oncomplete;
    def onerror;
    def showinfo;
    
    public void run(){
        try{
            showinfo('Initializing...Done\n');
            
            showinfo('Assigning new TD No. to Subdivided Lands and Affected RPUs');
            svc.assignNewTdNos(entity);
            showinfo(' .... Done\n');

            showinfo('Processing Subdivided Lands\n');
            svc.getSubdividedLandsForApproval(entity.objid).each{ land ->
                showinfo('Approving Subdivided Land FAAS for PIN ' + land.newpin );
                svc.approveSubdividedLandFaasRecord(entity, land);
                showinfo(' .... Done\n');
            }
            
            showinfo('Processing Affected RPUs\n');
            svc.getAffectedRpusForApproval(entity.objid).each{ arpu ->
                showinfo('Approving new Affected RPU FAAS for PIN ' + arpu.newpin);
                svc.approveAffectedRpuFaasRecord(entity, arpu);
                showinfo(' .... Done\n');
            }
            
            showinfo('Processing Cancelled Improvements\n');
            svc.getCancelledImprovements(entity.objid).each{ ci ->
                showinfo('Cancelling Improvement ' + ci.fullpin);
                svc.approveCancelledImprovement(entity, ci);
                showinfo(' .... Done\n');
            }            
            
            showinfo('Subdivision Approval')
            entity.putAll(svc.approve(entity));
            showinfo(' .... Done\n');
            
            oncomplete()
        }
        catch(e){
            entity.state = 'DRAFT';
            e.printStackTrace();
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
 