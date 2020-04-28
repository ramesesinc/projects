package com.rameses.gov.etracs.bpls.reports.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

public class BPTaxFeeTopListingReportModel extends AsyncReportController {
    
    @Service('BPTaxFeeTopListingReportService')
    def svc;

    @Service('BusinessPermitTypeService')
    def permitTypeSvc;            
    
    String title = "Top Business Listing (Tax/Fee)";
    String reportpath = 'com/rameses/gov/etracs/bpls/reports/';
    String reportName = reportpath + 'BPTaxFeeTopListing.jasper';
    
    def formControl = [ 
        getControlList: { 
            return [
                [type:'integer', caption:'Top', name:'entity.topsize', required:true, preferredSize:'100,20', captionWidth:130 ], 
                [type:'integer', caption:'Year', name:'entity.year', required:true, preferredSize:'100,20', captionWidth:130 ], 
                [type:'checklist', caption:'App Type', name:'entity.apptypes', immediate:true, preferredSize:'0,25', items:'appTypeList', itemExpression:'#{item.title}', selectionMode:'MULTIPLE', itemCount:3, captionWidth:130 ],
                [type:'checklist', caption:'App Status', name:'entity.appstates', immediate:true, preferredSize:'0,25', items:'appStatusList', itemExpression:'#{item.title}', selectionMode:'MULTIPLE', itemCount:3, captionWidth:130 ],
                [type:'combo', caption:'Permit Type', name:'entity.permittype', required:true, preferredSize:'0,20', captionWidth:130, items:'permitTypes',  expression:'#{item.title}'],
                
                [type:'label', caption:'', expression:' ', preferredSize:'0,10', showCaption:false],
                [type:'checkbox', caption:'',  text:'With Payments Only', name:'entity.withpayment', immediate:true, preferredSize:'0,20', captionWidth:130 ] 
            ]; 
        } 
    ] as FormPanelModel; 
    
    def appTypeList = [
        [objid: 'NEW', title:'NEW'],
        [objid: 'RENEW', title:'RENEW'],
        [objid: 'ADDITIONAL', title:'ADDITIONAL'] 
    ];
    def appStatusList = [
        [objid: 'COMPLETED', title: 'COMPLETED'], 
        [objid: 'RELEASE', title: 'RELEASE'],
        [objid: 'PAYMENT', title: 'PAYMENT']
    ]; 
    
    def data;

    def permitTypes;

    def initReport() {
        def outcome = super.initReport(); 
        permitTypes = permitTypeSvc.getList(); 
        entity.permittype = (permitTypes? permitTypes[0] : null); 
        entity.year = new java.sql.Date( System.currentTimeMillis() ).toString().split("-")[0]; 
        entity.apptypes = appTypeList.findAll{ it.objid.matches('NEW|RENEW')}
        entity.appstates = appStatusList;
        entity.topsize = 10;   
        return outcome; 
    } 

    void buildReportData(entity, asyncHandler) { 
        if ( !entity.apptypes ) throw new Exception("Please specify at least 1 application type"); 
        if ( !entity.appstates ) throw new Exception("Please specify at least 1 application status"); 
        
        svc.getReport( entity, asyncHandler );
    }
    
    void buildResult( result ) { 
        data = result; 
        
        if ( !data.reportdata ) throw new Exception('No available record(s) that matches your criteria.');
    } 

    Map getParameters(){
        def map = [
            TITLE : data.title.toString(), 
            APPYEAR : entity.year, 
            CLASSIFICATION : "ALL" 
        ];
        map.APPTYPE = entity.apptypes.collect{ it.objid }.join(', '); 
        map.STATE = entity.appstates.collect{ it.objid }.join(', ');
        return map; 
    }
}