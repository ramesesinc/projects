package com.rameses.gov.etracs.rpt.consolidation.task;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.util.MapBeanUtils;


public class SubmitToProvinceConsolidationTask implements Runnable{
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
            
            showinfo('Consolidation Approval')
            entity.putAll(svc.submitToProvinceConsolidation(entity));
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