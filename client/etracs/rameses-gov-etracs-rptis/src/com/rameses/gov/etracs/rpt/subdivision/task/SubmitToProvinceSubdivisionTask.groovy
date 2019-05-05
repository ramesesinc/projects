package com.rameses.gov.etracs.rpt.subdivision.task;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;
import com.rameses.util.MapBeanUtils;


public class SubmitToProvinceSubdivisionTask implements Runnable{
    def svc;
    def entity;
    def oncomplete;
    def onerror;
    def showinfo;
    
    public void run(){
        try{
            showinfo('Submitting to Province. Initializing');
            svc.initApproveSubdivision(entity);
            showinfo(' .... Done\n');
            
            // showinfo('Assigning new TD No. to Subdivided Lands and Affected RPUs');
            // svc.assignNewTdNos(entity);
            // showinfo(' .... Done\n');
            
            showinfo('Subdivision Approval');
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
 