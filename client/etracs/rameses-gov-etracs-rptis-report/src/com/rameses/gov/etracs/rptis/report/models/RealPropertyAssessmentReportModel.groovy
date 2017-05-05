package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class RealPropertyAssessmentReportModel extends com.rameses.gov.etracs.rpt.report.AsyncReportController
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
    
    String path = 'com/rameses/gov/etracs/rpt/report/';
    
            
    def initReport(){
        showRate = true;
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
    
    def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [caption:'LGU', name:'entity.lgu', items:'lgus', expression:'#{item.name}', emptyText:'ALL']),
                new FormControl( "combo", [caption:'Barangay', name:'entity.barangay', items:'barangays', expression:'#{item.name}', depends:'entity.lgu', dynamic:true, preferredSize:'0,21', emptyText:'ALL']),
                new FormControl( "combo", [caption:'Report Format', name:'entity.reporttype', items:'reporttypes', expression:'#{item.caption}', required:true, allowNull:false]),
                new FormControl( "combo", [caption:'Period Type', name:'entity.periodtype', items:'periodtypes', expression:'#{item.caption}', required:true, allowNull:false]),
                new FormControl( "integer", [caption:'Year', name:'entity.year', required:true, preferredSize:'100,19', depends:'entity.periodtype', visibleWhen:"#{entity.periodtype.type != 'asofdate'}"]),
                new FormControl( "combo", [caption:'Quarter', name:'entity.qtr', items:'quarters', required:true, preferredSize:'100,19',depends:'entity.periodtype', visibleWhen:"#{entity.periodtype.type == 'quarterly'}", dynamic:true, allowNull:false]),
                new FormControl( "combo", [caption:'Month', name:'entity.month', items:'months', expression:'#{item.name}', preferredSize:'100,19', depends:'entity.periodtype', visibleWhen:"#{entity.periodtype.type == 'monthly'}", dynamic:true, allowNull:false]),
                new FormControl( "date", [caption:'As of Date', name:'entity.asofdate', preferredSize:'100,19', depends:'entity.periodtype', visibleWhen:"#{entity.periodtype.type == 'asofdate'}", required:true]),
                new FormControl( "separator", [preferredSize:'0,21']),
                new FormControl( "decimal", [caption:'Basic Rate', name:'entity.basicrate', required:true]),
                new FormControl( "decimal", [caption:'SEF Rate', name:'entity.sefrate', required:true]),
            ]
        },
   ] as FormPanelModel;
   
   def periodtypes = [
        [type:'quarterly', caption:'QUARTERLY'],
        [type:'monthly', caption:'MONTHLY'],
        [type:'asofdate', caption:'AS OF DATE'],
   ]
 
    
    def reporttypes = [
        [type:'standard', caption:'STANDARD', reportname:'reportonrpa.jasper'],
        [type:'lift', caption:'LIFT', reportname:'reportonrpa_lift.jasper'],
    ]

    List getQuarters() {
        if (entity.periodtype.type == 'quarterly')
            return [1,2,3,4]
        return [];
    }
    
    List getMonths(){
        if (entity.periodtype.type == 'monthly')
            return dtSvc.getMonths();
        return [];
    }
    

}
