package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class RealPropertyAssessmentReportModel extends AsyncReportModel
{
    @Service('RPTReportRPAService') 
    def svc 
    
    @Service('QueryService')
    def querySvc;    
    
    @Binding 
    def binding;
    
    String title = 'Report on Real Property Assessment'
    boolean showClassification = true;
    boolean showRate = false;
    
    String path = 'com/rameses/gov/etracs/rptis/reports/';
    
            
    def initReport(){
        showRate = true;
        entity.classifications = getClassifications()
        entity.basicrate = 1.0
        entity.sefrate = 1.0 
        return 'default';
    }
    
    String getReportName(){
        return path + entity.reporttype.reportname
    }
    
    void buildReportData(entity, asyncHandler){
        svc.generateReportOnRPA(entity, asyncHandler); 
    }
    
   def periodtypes = [
        [type:'quarterly', caption:'QUARTERLY'],
        [type:'monthly', caption:'MONTHLY'],
   ]
   
    def taxabilities = [
        'TAXABLE', 'EXEMPT',
    ]
 
    
    def reporttypes = [
        [type:'standard', caption:'STANDARD', reportname:'reportonrpa.jasper'],
        [type:'lift', caption:'LIFT', reportname:'reportonrpa_lift.jasper'],
        [type:'lift_restriction', caption:'LIFT - RESTRICTION', reportname:'reportonrpa_lift_restriction.jasper'],
        [type:'lift_idleland', caption:'LIFT - IDLE LAND', reportname:'reportonrpa_lift_idleland.jasper'],
    ]

    def getQuarters() {
        if (entity.periodtype.type == 'quarterly')
            return [1,2,3,4]
        return [];
    }
    
    def getMonths(){
        if (entity.periodtype.type == 'monthly')
            return dtSvc.getMonths();
        return [];
    }
    
    def listHandler = [
        getRows  : { entity.classifications.size()},
        fetchList : {entity.classifications},
    ] as EditorListModel
    
    def getClassifications(){
        def q = [_schemaname:'propertyclassification']
        q.select = 'objid, name'
        q.where = ["state = 'APPROVED'"]
        q.orderBy = 'orderno'
        return querySvc.getList(q).each{
            it.basicrate = 1.0
            it.sefrate = 1.0 
        }
    }

}
