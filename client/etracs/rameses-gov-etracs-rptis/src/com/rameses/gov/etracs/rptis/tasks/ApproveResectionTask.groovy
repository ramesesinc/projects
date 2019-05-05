package com.rameses.gov.etracs.rptis.tasks;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;

public class ApproveResectionTask implements Runnable{
    def svc;
    def entity;
    def oncomplete;
    def onerror;
    def showinfo;
    
    public void run(){
        try{
            showinfo('Initializing...');
            def resection = [:]
            resection.putAll(entity);
            resection.remove('items');
            showinfo(' .... Done\n');
            
            showinfo('Assign new Tax Declaration No.');
            svc.assignNewTdNos(entity);
            showinfo(' .... Done\n');
            
            entity.items.each{
                if (it.newfaas.objid) {
                    showinfo('Approve resectioned FAAS ' + it.newfaas.fullpin);
                    svc.approveResectionedFaas(resection, it);
                    showinfo(' .... Done\n');
                }
            }
            
            showinfo('Approve resection')
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
 