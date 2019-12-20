package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class StatementOfShareReportModel extends com.rameses.gov.etracs.landtax.report.LandTaxReportController
{
    @Invoker
    def inv 

    @Service('LandTaxReportStatementOfShareService')
    def reportSvc; 

    def reportPath = 'com/rameses/gov/etracs/landtax/reports/';

    def data;

    public String getTitle() {
        def title = inv?.properties.reportTitle;
        if (!title) {
            title = 'Statement of Share';
        }
        return title;
    }

    public String getReportName() {
        def name = inv?.properties.reportName;
        def appendOrgClass = inv?.properties.appendOrgClass.toString().toLowerCase().matches('true|t|yes|y|1')
        if (!name) throw new Exception('invoker reportName is required');
        if (name.indexOf('/') >= 0) {
            return name;
        }
        if (appendOrgClass) {
            name += '_' + OsirisContext.env?.ORGCLASS.toLowerCase();
        }
        if (name.indexOf('.jasper') < 0) {
            name += '.jasper';
        }
        return reportPath + name;
    }

    public def getReportData(params) {
        if (!inv?.properties.tag) throw new Exception('invoker tag is required');
        if (!inv?.properties.reportTitle) throw new Exception('invoker reportTitle is required');
        params._tag = inv.properties.tag 

        data = reportSvc.getReportData(params);
        if (!data.shares) throw new Exception('Report data shares is not specified.');
        return data.shares;
    }

    def getReportParameters(){
        return data?.params;
    }
    
    def getPeriods(){
        return ["Monthly"];
    }

}