package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPUMachInfoActualUseModel extends SubPageModel
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
    
    
    /*-------------------------------------------------------------
     *
     * MAIN PAGE
     *
     ------------------------------------------------------------- */
     def selectedItem;
     
     def getLookupActualUse(){
         return InvokerUtil.lookupOpener('machassesslevel:lookup', [
             lguid    : entity.lguid,
             barangayid : entity.rp.barangayid,
             ry       : entity.rpu.ry,
         ])
     }
     
     def listHandler = [
         getRows   : { return 25 },
             
         fetchList : { return entity.rpu.machuses },
                 
         createItem : { return [
            machrpuid		: entity.rpu.objid,
            basemarketvalue	: 0.0,
            marketvalue		: 0.0,
            assesslevel		: 0.0,
            assessedvalue	: 0.0,
            machines            : [],
         ]},
                 
         validate  : { li ->
             def mu = li.item;
             RPTUtil.required('Actual Use', mu.actualuse)
             if (mu.objid){
                 calculateAssessment()
             }
         },
                 
         onAddItem : { mu ->
            mu.objid = RPTUtil.generateId('MU');
            entity.rpu.machuses.add(mu);
         },
         
         onRemoveItem :{ mu ->
            if (MsgBox.confirm('Delete selected item?')){
                entity.rpu.machuses.remove(mu);
                if (!entity.rpu._machuses) 
                    entity.rpu._machuses = [];
                entity.rpu._machuses.add(mu)
                calculateAssessment();
                return true;
            }
            return false;
         },
         
     ] as EditorListModel 
     
    
}    
