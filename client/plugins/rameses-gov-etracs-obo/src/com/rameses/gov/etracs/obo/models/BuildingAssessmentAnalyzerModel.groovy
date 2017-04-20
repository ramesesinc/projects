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
    
    @Service("OboInfoService")
    def infoSvc;

    String title = "Building Assessment Analyzer";
    def mode = "default";
    
    def entity = [:];
    def ruleExecutor;
    def permits = [];
    def bldginfos = [];
    
    def buildInfo() {
        def info = [:];
        info.buildinguse = [
            kind: entity.buildingkind.objid,
            id: entity.buildingkind.use.objid, 
            classificationid: entity.buildingkind.use.classificationid, 				
            groupid:entity.buildingkind.use.groupid,				
            division:entity.buildingkind.use.division,
        ];    
        info.application = [
            floorarea: entity.floorarea,
            worktype: entity.worktype,
            numunits: entity.numunits,
            height: entity.height,
            apptype: entity.apptype    
        ];
        return info;
    }
    
    def assess() {
        def p = buildInfo();
        p.permits = [];
        p.infos = [];
        if(permits) {
            permits.each {
                p.permits << [permittype: it.permitType];
                p.infos.addAll( it.infos );
            }
        };
        if(bldginfos) {
            bldginfos.each {
                p.infos << it;
            }
        }
        def r = assmtSvc.assess( p );
        entity.fees = r.fees;
        entity.constructioncost = r.constructioncost;
        r.infos?.each {
            if(it.amount ) println it;
        }
        //entity.infos = r.infos;
        itemListModel.reload();
        mode = "result";
        return mode;
    }
    
    def assessInspection() {
        
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
    
    def getPermit(String type) {
        def permit = permits.find{ it.permitType == type };
        if(!permit) {
            permit = [permitType: type, infos: [] ];
            permits << permit;
        }
        return permit;
    }
    
    def getElectricalPermit() { return getPermit( "ELECTRICAL" ).infos; }
    def getMechanicalPermit() { return getPermit( "MECHANICAL" ).infos; }
    def getPlumbingPermit() { return getPermit( "PLUMBING" ).infos; }
    def getElectronicPermit() { return getPermit( "ELECTRONIC" ).infos; }
    
    def getBuildingInfos() { 
        return bldginfos;
    }

}