package com.rameses.gov.etracs.rpt.faas.change.ui;
        

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.faas.change.ui.*;


public class ChangeFAASAndRPUInfoController extends ChangeFaasInfoController
{
    @Service('PropertyClassificationService')
    def pcSvc;
        
    String title = 'Modify FAAS Information';
    
    
    public def getModifiedEntity(){
        return [
            tdno        : entity.tdno,
            titleno	: entity.titleno,
            titledate 	: entity.titledate,
            restrictionid 	: entity.restrictionid,
            effectivityyear	: entity.effectivityyear,
            effectivityqtr 	: entity.effectivityqtr,
            memoranda       : entity.memoranda,
            txntype         : entity.txntype,
            classification  : classifications.find{it.objid == entity.rpu.classification?.objid},
            rputype         : entity.rpu.rputype,
            publicland      : entity.rpu.publicland,
        ]
    }
    
    public void updateEntityInfo(newinfo){
        entity.tdno             =  newinfo.tdno;
        entity.titleno          =  newinfo.titleno;
        entity.titledate 	=  newinfo.titledate;
        entity.restrictionid 	=  newinfo.restrictionid;
        entity.effectivityyear	=  newinfo.effectivityyear;
        entity.effectivityqtr 	=  newinfo.effectivityqtr;
        entity.memoranda        =  newinfo.memoranda;
        entity.txntype          = newinfo.txntype;
    }
    
     List getRestrictions(){
         return LOV.RPT_FAAS_RESTRICTIONS*.key
     }
     
          
     List getQuarters() { 
         return [1,2,3,4];
     }

     List getClassifications(){
        return pcSvc.getList([:]);
     }

     
}
       