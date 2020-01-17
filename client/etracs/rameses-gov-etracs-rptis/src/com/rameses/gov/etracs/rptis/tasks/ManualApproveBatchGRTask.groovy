package com.rameses.gov.etracs.rptis.tasks;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;
import com.rameses.util.MapBeanUtils;


public class ManualApproveBatchGRTask implements Runnable{
    def svc;
    def entity;
    def oncomplete;
    def onerror;
    def showinfo;
    
    public void run(){
        try{
            showinfo('Initializing...Done\n');

            svc.getItems(entity).each {
                if (it.newfaasid) {
                    showinfo('Approve revised FAAS ' + it.newfaas.fullpin);
                    svc.approveFaas(entity, it);
                    showinfo(' .... Done\n');
                }
            }
            
            showinfo('Approve batch revision')
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
 