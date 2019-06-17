package com.rameses.gov.etracs.waterworks.report.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class WaterworksReportConnectionByMeterStatusModel extends ReportController {

    @Service('WaterworksReportMeterStatusService')
    def svc; 
    
    @Script('ReportPeriod') 
    def periodUtil; 
    
    @Binding
    def binding;

    String title = "No. of Connections by Meter Status";
    String reportpath =  "com/rameses/gov/etracs/waterworks/report/forms/"; 
    String reportName = reportpath + 'meter_connection_status.jasper'; 
    
    def periodTypes;
    def barangays;
    def zones;
    
    def reportheader; 
    
    def initReport() { 
        def outcome = super.initReport(); 
        def res = svc.initReport( [:] ); 
        barangays = res.barangays; 
        zones = res.zones;
        
        periodTypes = []; 
        periodTypes << [type:'asoftoday', title:'As of Today']; 
        entity.period = periodTypes.first(); 
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
            new SubReport("ReportItem", reportpath + "meter_connection_status_item.jasper"),                
        ] as SubReport[]; 
    } 
    
    Map getParameters() { 
        def params = [:];
        if ( reportheader.serverdate ) {
            def YMD = new java.text.SimpleDateFormat('MMMMM dd, yyyy'); 
            params.PERIOD = "As of "+ YMD.format( reportheader.serverdate ); 
        }
        return params;
    } 
    
    
    def formControl = [
        getControlList: { 
            return [
                [type:'combo', caption:'Period', name:'entity.period', required:true, items:'periodTypes',  expression:'#{item.title}', preferredSize:'100,20', captionWidth:100],
                [type:'combo', caption:'Barangay', name:'entity.barangay', items:'barangays', expression:' #{item.name}', preferredSize:'0,20', captionWidth:100] 
            ]; 
        } 
    ] as FormPanelModel; 
}
