package com.rameses.gov.etracs.rpt.report.td;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rpt.common.*;

public class TDPlainCopyController extends com.rameses.gov.etracs.rpt.report.certification.AbstractCertificationController
{
    @Service('RPTCertificationService')
    def svc;
    
    @Service('TDReportService')
    def tdSvc;
    
    boolean certbytd = true
    boolean officialuse = true
   
    def init() {
        super.init();
        setOfficialuse( officialuse )
        return 'default'
    }

    def getService(){
        return svc;
    }
    
    def getReportData(){
        return tdSvc.buildTaxDec(entity.faasid);
    }
    
    def reportPath = 'com/rameses/gov/etracs/rpt/report/td/';
    String reportName = reportPath + 'tdreport.jasper';

    SubReport[] getSubReports() {
        return [
            new SubReport( 'TDReportLand',  reportPath + 'tdreportland.jasper'),
            new SubReport( 'TDReportBldg',  reportPath + 'tdreportbldg.jasper'),
            new SubReport( 'TDReportMach',  reportPath + 'tdreportimprovement.jasper'),
            new SubReport( 'TDReportPlant', reportPath + 'tdreportimprovement.jasper'),
            new SubReport( 'TDReportMisc',  reportPath + 'tdreportimprovement.jasper'),
            new SubReport( 'TDReportAnnotation', reportPath + 'tdreportannotation.jasper'),
        ] as SubReport[]
    }
    
    Map getParameters(){
        def info = [:];
        entity.each{ k, v -> info[ k.toUpperCase() ] = v };
        info.CERTIFICATIONORAMOUNT = entity.oramount;
        info.CERTIFICATIONORDATE   = entity.ordate;
        info.CERTIFICATIONORNO     = entity.orno;
        info.PLAINTRUECOPY         = true;
        return info;
    }

}