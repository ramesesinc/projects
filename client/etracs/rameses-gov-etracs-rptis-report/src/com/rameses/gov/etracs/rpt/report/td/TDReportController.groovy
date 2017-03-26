package com.rameses.gov.etracs.rpt.report.td;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;
import com.rameses.gov.etracs.rptis.util.*;


class TDReportController
{
    @Service('TDReportService') 
    def svc;
    
    @Service('ReportParameterService')
    def paramSvc;
    
    String title = 'Tax Declaration Report'
    
    
    def entity 
    def reportPath = 'com/rameses/gov/etracs/rpt/report/td/'
    
    def reportdata;
    
    def initPreview(){
        reportdata = svc.buildTaxDec(entity.objid);
        saveSignatures(reportdata);
        report.viewReport();
        return 'preview'
    }
    
    void saveSignatures(reportdata){
        reportdata.signatories.each{ k, v ->
            def objid = v.objid + '-' + v.state 
            if (v.signature?.image)
                v.signatureis = DBImageUtil.getInstance().saveImageToFile(objid, v.signature.image)
        }
    }
            
    def report = [
        getReportName : { return reportPath + 'tdreport.jasper'},
        getSubReports : { 
            return [
                new SubReport( 'TDReportLand',  reportPath + 'tdreportland.jasper'),
                new SubReport( 'TDReportLandESig',  reportPath + 'tdreportlandesigned.jasper'),
                new SubReport( 'TDReportBldg',  reportPath + 'tdreportbldg.jasper'),
                new SubReport( 'TDReportMach',  reportPath + 'tdreportimprovement.jasper'),
                new SubReport( 'TDReportPlant', reportPath + 'tdreportimprovement.jasper'),
                new SubReport( 'TDReportMisc',  reportPath + 'tdreportimprovement.jasper'),
                new SubReport( 'TDReportAnnotation', reportPath + 'tdreportannotation.jasper'),
            ] as SubReport[]
        },
        getReportData : { return reportdata },
        getParameters : {
            def params = paramSvc.getStandardParameter()
            params.LOGOLGU = EtracsReportUtil.getInputStream("lgu-logo.png")
            params.BACKGROUND = EtracsReportUtil.getInputStream("background.png")
            params.LOGOASSESSOR = EtracsReportUtil.getInputStream("lgu-assessor.png")
            params.LOGOBLGF = EtracsReportUtil.getInputStream("lgu-blgf.png")
            return params 
        },
    ] as ReportModel
    
    
    
}    