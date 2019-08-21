package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class ActiveCompromiseAgreementsListingModel extends com.rameses.gov.etracs.rpt.report.AsyncReportController
{
    @Service('LandTaxReportCompromiseAgreementService') 
    def svc 
    
    String title = 'List of Compromise Agreements'
    
    def reportpath = 'com/rameses/gov/etracs/landtax/report/compromise/'
    String reportName = reportpath + 'rptcompromise_listing.jasper';
        
    void buildReportData(entity, asyncHandler){
        svc.generateReport(entity, asyncHandler)
    }
        
   def formControl = [
        getFormControls: {
            return [
                new FormControl( "combo", [caption:'LGU', name:'entity.lgu', items:'lgus', expression:'#{item.name}', emptyText:'ALL']),
                new FormControl( "combo", [caption:'Barangay', name:'entity.barangay', items:'barangays', expression:'#{item.name}', depends:'entity.lgu', dynamic:true, preferredSize:'0,21', emptyText:'ALL']),
            ]
        },
   ] as FormPanelModel;
}