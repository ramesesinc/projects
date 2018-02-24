package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPUBldgInfoGeneralModel extends SubPageModel
{
    @Service('Var')
    def varSvc;
    
    def rpuSvc;
    
    
    @PropertyChangeListener
    def listener = [
        'entity.rpu.(hasswornamount|swornamount|useswornamount|effectiveage|dtoccupied|dtcompleted|depreciation|cdurating)':{
            if (!entity.rpu.hasswornamount){
                entity.rpu.swornamount = 0.0;
                entity.rpu.useswornamount = false;
            }
            calculateAssessment();
        },
    ]    

        
    void init(){
        entity.rpu.classification = classifications.find{it.objid == entity.rpu.classification?.objid}
    }
    
    def getClassifications(){
        return rpuSvc.getClassifications();
    }    
    
    def getClassList(){
        return [
            'CLASS A', 'CLASS B', 'CLASS C', 'CLASS D'
        ]
    }
    
    
    def getAutoDepreciate(){
        def autodepreciate = varSvc.get('bldg_rpu_auto_depreciate')
        if (autodepreciate == null) autodepreciate = true;
        return RPTUtil.isTrue(autodepreciate);
    }
    
    def getCduRatings(){
        LOV.BLDG_CDU_RATING*.key
    }
   
}    
    
