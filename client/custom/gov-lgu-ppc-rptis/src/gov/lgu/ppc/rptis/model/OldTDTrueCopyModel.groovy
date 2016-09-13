package gov.lgu.ppc.rptis.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;
import com.rameses.gov.etracs.rpt.util.*;

class OldTDTrueCopyModel extends com.rameses.gov.etracs.rpt.report.certification.AbstractCertificationController
{
    @Service('RPTCertificationService')
    def svc;
    
    @Service('PPC_TDReportService')
    def tdSvc;
    
    @Service('ReportParameterService')
    def paramSvc;
    
    boolean certbytd = true
            
    def getService(){
        return svc;
    }
    
    def getReportData(){
        def e = tdSvc.buildReport(entity.faasid);
        e.putAll(entity);
        return e;
    }
    
    def reportPath = 'gov/lgu/ppc/rptis/report/td/';
    String reportName = reportPath + 'TDOldReport.jasper';

    SubReport[] getSubReports() {
        return [
            new SubReport( 'Land1Report',  reportPath + 'Land1Report.jasper'),
            new SubReport( 'TreeRPUReport',  reportPath + 'TreeRPUReport.jasper'),
            new SubReport( 'Land2Report',  reportPath + 'Land2Report.jasper'),
            new SubReport( 'MachRPUReport', reportPath + 'MachRPUReport.jasper'),
            new SubReport( 'TDAssessmentReport',  reportPath + 'TDAssessmentReport.jasper'),
        ] as SubReport[]
    }
    
    Map getParameters(){
        def info = [:];
        entity.each{ k, v -> info[ k.toUpperCase() ] = v };
        info.CERTIFICATIONORAMOUNT = entity.oramount;
        info.CERTIFICATIONORDATE   = entity.ordate;
        info.CERTIFICATIONORNO     = entity.orno;
        info.PLAINTRUECOPY         = false;
        info.LOGOLGU = EtracsReportUtil.getInputStream("lgu-logo.png")
        info.BACKGROUND = EtracsReportUtil.getInputStream("background.png")
        info.LOGOASSESSOR = EtracsReportUtil.getInputStream("assessor-logo.png")
        info.LOGOBLGF = EtracsReportUtil.getInputStream("lgu-blgf.png")
        return info;
    }
    
    
    def getCertificationTypes(){
        return [
            [type:'byfaas', caption:'By FAAS'],
        ]
    }     
}