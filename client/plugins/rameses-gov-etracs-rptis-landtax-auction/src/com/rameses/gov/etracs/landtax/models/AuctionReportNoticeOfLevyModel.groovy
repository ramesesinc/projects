package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;


class AuctionReportNoticeOfLevyModel extends ReportController
{
    @Caller
    def caller;
    
    public String getReportName(){
        return 'com/rameses/gov/etracs/landtax/reports/notice_of_levy.jasper';
    }
    
    public def getReportData(){
        return entity;
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