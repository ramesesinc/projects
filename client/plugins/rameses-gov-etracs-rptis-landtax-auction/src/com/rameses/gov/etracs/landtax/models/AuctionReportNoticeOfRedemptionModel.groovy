package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class AuctionReportNoticeOfRedemptionModel extends ReportController
{
    @Caller
    def caller;
    
    @Service('PropertyAuctionReportNoticeOfRedemptionService')
    def svc;
    
    public String getReportName(){
        return 'com/rameses/gov/etracs/landtax/reports/notice_of_redemption.jasper';
    }
    
    def init() {
        return preview();
    }
    
    public def getReportData(){
        return svc.getReport(entity);
    }
    
}

