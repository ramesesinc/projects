package com.rameses.gov.etracs.rptis.tasks;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;
import com.rameses.util.MapBeanUtils;


public class SubmitToProvinceResectionTask implements Runnable{
    def svc;
    def entity;
    def oncomplete;
    def onerror;
    def showinfo;
    
    public void run(){
        try{
            showinfo('Initializing');
            svc.initSubmitToProvince(entity);
            showinfo(' .... Done\n');
            
            showinfo('Assign new Tax Declaration No.');
            svc.assignNewTdNos(entity);
            showinfo(' .... Done\n');
            
            showinfo('Submit to province');
            entity.putAll(svc.submitToProvince(entity));
            showinfo(' .... Done\n');
            
            oncomplete();
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
 