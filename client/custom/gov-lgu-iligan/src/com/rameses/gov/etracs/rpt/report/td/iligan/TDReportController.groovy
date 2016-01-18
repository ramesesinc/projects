package com.rameses.gov.etracs.rpt.report.td;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;


class TDReportController
{
    @Service('TDReportService') 
    def svc;
    
    @Service('ReportParameterService')
    def paramSvc;
    
    String title = 'Tax Declaration Report'
    
    
    def entity 
    def reportPath = 'com/rameses/gov/etracs/rpt/report/td/'
    
    
    def initPreview(){
        report.viewReport();
        return 'preview'
    }
    
    def report = [
        getReportName : { return reportPath + 'tdreport.jasper'},
        getSubReports : { 
            return [
                new SubReport( 'TDReportLand',  reportPath + 'tdreportland.jasper'),
                new SubReport( 'TDReportBldg',  reportPath + 'tdreportbldg.jasper'),
                new SubReport( 'TDReportMach',  reportPath + 'tdreportimprovement.jasper'),
                new SubReport( 'TDReportPlant', reportPath + 'tdreportimprovement.jasper'),
                new SubReport( 'TDReportMisc',  reportPath + 'tdreportimprovement.jasper'),
                new SubReport( 'TDReportAnnotation', reportPath + 'tdreportannotation.jasper'),
            ] as SubReport[]
        },
        getReportData : { return svc.buildTaxDec(entity.objid) },
        getParameters : { paramSvc.getStandardParameter() },
    ] as ReportModel
    
}    