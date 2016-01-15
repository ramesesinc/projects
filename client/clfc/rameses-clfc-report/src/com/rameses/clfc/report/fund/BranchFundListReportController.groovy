package com.rameses.clfc.report.fund;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import java.text.SimpleDateFormat;

class BranchFundListReportController extends ReportModel
{
    @Service("FundRequestReportService")
    def service;

    String title = "Branch Fund Listing";

    def mode = 'init'
    def startdate;
    def enddate;
    
    def close() {
        return "_close";
    }
    
    def preview() {
        viewReport();
        mode = 'preview';        
        return 'preview';
    }
    
    def back() {
        mode = 'init';
        return 'default';
    }

    public Map getParameters() {
        return [:];
    }

    public Object getReportData() {
        def rptdata = service.getReportData([startdate:startdate, enddate:enddate]);
        if (!rptdata.list) { 
            throw new Exception('No matching record(s) found'); 
        } 
        
        rptdata.remove('branch')?.each{k,v-> rptdata['branch_'+k]=v } 
        if (startdate == enddate) {
            rptdata.date_period = formatDate(startdate,'yyyy-MMM-dd'); 
        } else {
            rptdata.date_period = formatDate(startdate,'yyyy-MMM-dd') + ' TO ' + formatDate(enddate,'yyyy-MMM-dd');
        }         
        println rptdata.date_period;
        return rptdata;
    }
    
    public String getReportName() {
        return 'com/rameses/clfc/report/fund/BranchFundListReport.jasper';
    }

    public SubReport[] getSubReports() {
        return [
            new SubReport('ITEM', 'com/rameses/clfc/report/fund/BranchFundListItemReport.jasper') 
        ];
    }
    
    private String formatDate(def date, String pattern) {
        if (!date) return null;
        
        def formatter = new SimpleDateFormat(pattern);
        if (date instanceof Date) {
            return formatter.format(date); 
        } else if (date instanceof String) {
            return formatter.format(java.sql.Date.valueOf(date)); 
        } else {
            return null; 
        }
    }
}