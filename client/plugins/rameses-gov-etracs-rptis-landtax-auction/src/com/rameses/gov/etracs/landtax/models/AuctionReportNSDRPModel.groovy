package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;


class AuctionReportNSDRPModel extends ReportController
{
    @Caller
    def caller;
    
    @Service('PropertyAuctionReportNSDRPService')
    def svc;
    
    public String getReportName(){
        return 'com/rameses/gov/etracs/landtax/reports/nsdrp.jasper';
    }
    
    def initFirst() {
        entity._reporttype = 'FIRST';
        return preview();
    }
    
    def initSecond() {
        entity._reporttype = 'SECOND';
        return preview();
    }
    
    public def getReportData(){
        return svc.getReport(entity);
    }
    
    def doClose(){
        try{ 
            caller.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return '_close';
    }
}