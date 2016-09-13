package gov.lgu.ppc.rptis.model;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;
import com.rameses.gov.etracs.rpt.util.*;

public class OldTaxDeclarationModel 
{
    @Service('PPC_TDReportService')
    def svc;
    
    @Service('ReportParameterService')
    def paramSvc;
        
    
    def entity;
    def reportdata;
    
    def reportPath = 'gov/lgu/ppc/rptis/report/td/'
    
    def initPreview(){
        reportdata = svc.buildReport(entity.objid);
        report.viewReport();
        return 'preview'
    }
    
    def report = [
        getReportName : { return reportPath + 'TDOldReport.jasper'},
        getSubReports : { 
            return [
                new SubReport( 'Land1Report',  reportPath + 'Land1Report.jasper'),
                new SubReport( 'TreeRPUReport',  reportPath + 'TreeRPUReport.jasper'),
                new SubReport( 'Land2Report',  reportPath + 'Land2Report.jasper'),
                new SubReport( 'MachRPUReport', reportPath + 'MachRPUReport.jasper'),
                new SubReport( 'TDAssessmentReport',  reportPath + 'TDAssessmentReport.jasper'),
            ] as SubReport[]
        },
        getReportData : { return reportdata },
        getParameters : { return paramSvc.getStandardParameter() },
    ] as ReportModel
    
}

