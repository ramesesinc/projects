package com.rameses.gov.etracs.obo.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.enterprise.models.*;

class BuildingAssessmentAnalyzerModel  {

    @Binding
    def binding;

    @Service("BuildingAssessmentService")
    def assmtSvc;
    
    String title = "Building Assessment Analyzer";
    def mode = "default";
    
    def entity = [:];
    
    def assess() {
        def info = [:];
        info.buildinguse = [
            id: entity.buildinguse.objid, 
            classificationid: entity.buildinguse.classificationid, 				
            groupid:entity.buildinguse.groupid,				
            division:entity.buildinguse.division
        ];    
        info.application = [
            floorarea: entity.floorarea,
            worktype: entity.worktype,
            numunits: entity.numunits
        ];
        def r = assmtSvc.assess( info );
        entity.fees = r.fees;
        entity.constructioncost = r.constructioncost;
        itemListModel.reload();
        mode = "result";
        return mode;
    }
    
    def reset() {
        mode = "default";
        return mode;
    }
    
    def itemListModel = [
        fetchList: { o->
            return entity.fees;
        }
    ] as BasicListModel;
    
    
    
}