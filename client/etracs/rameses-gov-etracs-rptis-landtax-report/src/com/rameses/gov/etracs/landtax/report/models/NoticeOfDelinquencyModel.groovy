package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;

class NoticeOfDelinquencyModel
{
    @Service('LandTaxReportNoticeOfDelinquencyService')
    def svc 

    @Service("ReportParameterService")
    def reportSvc;
    
    @Service('RPTBillingService')
    def billingSvc 
    
    @Service('DateService')
    def dtSvc;

    def taxpayer;
    def address;
    def entity;
    def data = [:]
    def dtcomputed;
    def includecy = false;
    
    String title = 'Notice of Delinquency'
    
    def mode
    
    def init(){
        dtcomputed = dtSvc.getServerDate();
        mode = 'init';
        return 'default';
    }
    
    def initPreview(){
        init();
        mode = 'initpreview';
        return 'default'
    }
    
    
    void buildReportInfo(){
        def params = [
            taxpayer    : (taxpayer ? taxpayer : entity?.taxpayer),
            rptledgerid : entity?.objid,
            billdate    : dtcomputed,
            includecy   : includecy, 
        ]
        
        if (!params.taxpayer) throw new Exception('Please specify taxpayer'); 
        
        data = svc.generateNoticeOfDelinquency(params);
        report.viewReport();
    }
    
    void printNotice() {
        buildReportInfo()
        ReportUtil.print( report.report, true )
    }
    
    def previewNotice() {
        buildReportInfo()
        mode = 'preview'
        return 'preview'
    }
      
    def reportpath = 'com/rameses/gov/etracs/landtax/reports/'
            
    def report = [
        getReportName : { return reportpath + 'noticeofdelinquency.jasper' },
        getSubReports  : {
            return [
                new SubReport('NODLedger', reportpath + 'noticeofdelinquencyledger.jasper'),
            ] as SubReport[]
        },
        getReportData : { data },
        getParameters : { reportSvc.getStandardParameter() }
    ] as ReportModel
    

}
