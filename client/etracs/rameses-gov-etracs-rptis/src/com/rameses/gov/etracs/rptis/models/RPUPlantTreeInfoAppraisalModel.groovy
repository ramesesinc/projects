package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPUPlantTreeInfoAppraisalModel extends SubPageModel
{
    def rpuSvc;
    
    def getClassifications(){
        return rpuSvc.getClassifications();
    }        
    
        
    void init(){
        entity.rpu.classification = classifications.find{it.objid == entity.rpu.classification?.objid}
    }    
    
    void calculateAssessment(){
        super.calculateAssessment();
        listHandler.load();
        binding.refresh('.*');
    }
                
                
    def selectedItem;
    
    def getLookupParams(){
        return [
            lguid:entity.lguid, barangayid:entity.rp.barangayid, ry:entity.rpu.ry
        ]
    }
    
    def getLookupTreeUnitValue(){
        def params = lookupParams
        params.onselect = {
            selectedItem.planttreeunitvalue = it;
            selectedItem.planttree = it.planttree;
            selectedItem.unitvalue = it.unitvalue;
        }
        return InvokerUtil.lookupOpener('planttreeunitvalue:lookup', params);
    }
    
    def getLookupPlantTreeAssessLevel(){
        return InvokerUtil.lookupOpener('planttreeassesslevel:lookup', lookupParams);
    }
     
     def listHandler = [
         getRows : { return 25 },
         
         fetchList : { return entity.rpu.planttrees },
             
         createItem : { return createPlantTree() },
                 
         validate : { li -> 
             def pt = li.item 
             RPTUtil.required('Plant/Tree', pt.planttreeunitvalue);
             RPTUtil.required('Actual Use', pt.actualuse);
             RPTUtil.required('Productive', pt.productive);
             RPTUtil.required('Non-Productive', pt.nonproductive);
             if (pt.objid) {
                calculateAssessment();
             }
         },
                 
         onAddItem : { pt ->
             pt.objid = RPTUtil.generateId('PTD');
             entity.rpu.planttrees.add(pt);
             calculateAssessment();
         },
                 
         onRemoveItem : {pt ->
             if (MsgBox.confirm('Delete selected item?')) {
                entity.rpu.planttrees.remove(pt);
                if (!entity.rpu._planttrees) entity.rpu._planttrees = [];
                entity.rpu._planttrees.add(pt);
                calculateAssessment();
                return true;
             }
             return false;
         }
                 
     ] as EditorListModel
                 
                 
                 
     def createPlantTree(){
        return [
            planttreerpuid  : entity.rpu.objid,
            landrpuid       :  null,
            productive      : 0.0,
            nonproductive   : 0.0, 
            nonproductiveage  : 0.0,
            unitvalue       :  0.0,
            areacovered     : 0.0,
            basemarketvalue : 0.0,
            adjustment      : 0.0,
            adjustmentrate  : 0.0,
            marketvalue     : 0.0,
            assesslevel     : 0.0,
            assessedvalue   : 0.0,
        ]
     }                
   
}    
    