package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;


class AuctionReportNoticeModel extends ReportController
{
    @Caller
    def caller;
    
    @Service('PropertyAuctionNoticeService')
    def svc;
    
    public String getReportName(){
        return 'com/rameses/gov/etracs/landtax/reports/' + entity.step.report + '.jasper';
    }
    
    def initBatch(){}
    
    public def getReportData(){
        checkAuctionReference();
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
    
    void printImmediate() {
        report.viewReport(); 
        ReportUtil.print( report.report, false ); 
    }

    void checkAuctionReference() {
        if ('NOPAS' == entity.step.objid && !entity.auction?.objid) {
            svc.associateAuction(entity)
        }
    }
}