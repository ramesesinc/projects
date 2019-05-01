package com.rameses.gov.etracs.waterworks.report.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class WaterworksReportSummaryOfBillingModel extends ReportController {

    @Service('WaterworksReportSummaryOfBillingService')
    def svc; 
    
    @Script('ReportPeriod') 
    def periodUtil; 
    
    @Binding
    def binding;

    String title = "Summary of Billing";
    String reportpath =  "com/rameses/gov/etracs/waterworks/report/forms/"; 
    String reportName = reportpath + 'summaryofbilling.jasper'; 
    
    def periodTypes;
    def barangays;
    def zones;
    
    def initReport() { 
        def outcome = super.initReport(); 
        def res = svc.initReport( [:] ); 
        barangays = res.barangays; 
        zones = res.zones;
        
        periodTypes = periodUtil.types; 
        periodTypes = periodTypes.findAll{ it.type.toString().matches('yearly|quarterly|monthly') } 
        entity.period = periodTypes.find{ it.type == 'monthly' } 
        
        def arr = res.serverdate.toString().split('-'); 
        def month = arr[1].toInteger(); 
        entity.year = arr[0].toInteger(); 
        entity.month = periodUtil.months.find{ it.index == month }
        return outcome; 
    } 
    
    def getReportData() { 
        def param = [:]; 
        param.putAll( entity ); 
        return svc.getReport( param ); 
    }
    
    SubReport[] getSubReports() {
       return null; 
    } 
    
    Map getParameters() { 
        def params = [:];
        if ( entity.period?.type == 'quarterly' ) {
            def vals = [];
            if ( entity.qtr == 1 ) vals << "JANUARY - MARCH"; 
            else if ( entity.qtr == 2 ) vals << "APRIL - JUNE";
            else if ( entity.qtr == 3 ) vals << "JULY - SEPTEMBER";
            else if ( entity.qtr == 4 ) vals << "OCTOBER - DECEMBER";

            vals << entity.year; 
            params.PERIOD = vals.join(' '); 
        } 
        else if ( entity.period?.type == 'monthly' ) {
            params.PERIOD = 'FOR THE MONTH OF '+ entity.month.title +' '+ entity.year; 
        } 
        else if ( entity.period?.type == 'yearly' ) {
            params.PERIOD = 'FOR THE YEAR '+ entity.year; 
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
                
                [type:'combo', caption:'Barangay', name:'entity.barangay', items:'barangays', expression:' #{item.name}', preferredSize:'0,20', captionWidth:100],
                [type:'combo', caption:'Zone', name:'entity.zone', items:'barangayZones', expression:' #{item.code}', depends:'entity.barangay', dynamic:true, preferredSize:'0,20', captionWidth:100],
            ]; 
        } 
    ] as FormPanelModel; 

    def getBarangayZones() {
        if ( !entity.barangay?.objid ) return zones; 
        
        return zones.findAll{( it.barangay?.objid == entity.barangay.objid )} 
    }
}
