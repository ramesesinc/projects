package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class AuctionReportDeliveredNoticeListModel extends com.rameses.gov.etracs.rptis.report.models.AsyncReportModel2
{
    @Service('PropertyAuctionReportNoticeListingService') 
    def svc 
    
    String title = 'List of Notices'
    String reportName = 'com/rameses/gov/etracs/landtax/reports/notice_list.jasper';  
    
    def steps;
    def states;
    
    def initReport(){
        entity.qtr = null;
        steps = svc.getSteps();
        states = svc.getStates();
        return 'default';
    }
            
    void buildReportData(entity, asyncHandler){
        svc.buildReport(entity, asyncHandler)
    }
    
    Map getParameters(){
        return data.parameters;
    }
    
   def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [captionWidth:110, caption:'LGU', name:'entity.lgu', items:'lgus', expression:'#{item.name}', allowNull:false, required:true]),
                new FormControl( "combo", [captionWidth:110, caption:'Type of Notice', name:'entity.step', items:'steps', expression:'#{item.title}', allowNull:false, required:true]),
                new FormControl( "combo", [captionWidth:110, caption:'State', name:'entity.state', items:'states', expression:'#{item.title}', allowNull:false, required:true]),
                new FormControl( "integer", [captionWidth:110, caption:'Year', name:'entity.year', required:true, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:110, caption:'Month', name:'entity.month', items:'months', depends:'entity.year', expression:'#{item.caption}', required:true, dynamic:true, preferredSize:'100,19']),
                new FormControl( "combo", [captionWidth:110, caption:'Day', name:'entity.day', items:'days', depends:'entity.year,entity.month', dynamic:true, preferredSize:'100,19', emptyText:'ALL']),
            ]
        },
   ] as FormPanelModel;
   
}