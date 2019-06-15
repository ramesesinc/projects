package com.rameses.gov.etracs.waterworks.report.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class WaterworksReportNewAccountListModel extends ReportController {

    @Service('WaterworksReportNewAccountService')
    def svc; 
    
    @Script('ReportPeriod') 
    def periodUtil; 
    
    @Binding
    def binding;

    String title = "List of New Accounts";
    String reportpath =  "com/rameses/gov/etracs/waterworks/report/forms/"; 
    String reportName = reportpath + 'new_account_list.jasper'; 
    
    def periodTypes;
    def barangays;
    def zones;
    
    def reportheader; 
    
    def initReport() { 
        def outcome = super.initReport(); 
        def res = svc.initReport( [:] ); 
        barangays = res.barangays; 
        zones = res.zones;
        
        periodTypes = periodUtil.types; 
        periodTypes = periodTypes.findAll{ it.type.toString().matches('monthly|yearly') } 
        entity.period = periodTypes.find{ it.type == 'monthly' } 
        return outcome; 
    } 
    
    def getReportData() { 
        def param = [:]; 
        param.putAll( entity ); 
        
        def res = svc.getReport( param ); 
        reportheader = res.header;
        return res; 
    }
    
    SubReport[] getSubReports() {
        return [
            new SubReport("ReportItem", reportpath + "new_account_listitem.jasper"),                
        ] as SubReport[]; 
    } 
    
    Map getParameters() { 
        def params = [:];
        if ( reportheader?.PERIOD ) {
            params.PERIOD = reportheader.PERIOD; 
        }
        else if ( reportheader.serverdate ) {
            def YMD = new java.text.SimpleDateFormat('MMMMM dd, yyyy'); 
            params.PERIOD = "As of "+ YMD.format( reportheader.serverdate ); 
        }
        return params;
    } 
    
    
    def formControl = [
        getControlList: { 
            return [
                [type:'combo', caption:'Period', name:'entity.period', required:true, items:'periodTypes',  expression:'#{item.title}', preferredSize:'100,20', captionWidth:100],
                [type:'integer', caption:'Year', name:'entity.year', required:true, depends:'entity.period', visibleWhen:'#{ entity.period.type.matches("yearly|quarterly|monthly")==true }', preferredSize:'100,20', captionWidth:100 ],
                [type:'combo', caption:'Quarter', name:'entity.qtr', required:true, items:'periodUtil.quarters', depends:'entity.period', visibleWhen:'#{ entity.period.type=="quarterly" }', preferredSize:'100,20', captionWidth:100],
                [type:'combo', caption:'Month', name:'entity.month', required:true, items:'periodUtil.months', expression:'#{item.title}', depends:'entity.period', visibleWhen:'#{ entity.period.type=="monthly" }', preferredSize:'100,20', captionWidth:100],
                
                [type:'combo', caption:'Barangay', name:'entity.barangay', items:'barangays', expression:' #{item.name}', preferredSize:'0,20', captionWidth:100] 
            ]; 
        } 
    ] as FormPanelModel; 
}
