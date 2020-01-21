package com.rameses.gov.etracs.landtax.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;

class RptarModel
{
    @Service('RealtyTaxRegistryReportService')
    def svc 

    @Service("ReportParameterService")
    def reportSvc;
    
    def entity;
    def data;
    def mode;
    
    String title = 'Real Property Tax Account Registry';
    
    
    def reportName = 'com/rameses/gov/etracs/landtax/reports/rptar.jasper'
    
    
    def preview() {
        buildReportInfo()
        mode = 'preview'
        return 'preview'
    }
    
    void buildReportInfo(){
        data = svc.getRptar([objid:entity.objid])
        report.viewReport()
    }

    def report = [
        getReportName : { reportName },
        getReportData : { data },
        getParameters : { reportSvc.getStandardParameter() }
    ] as ReportModel

}
