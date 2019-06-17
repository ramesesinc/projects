package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import java.rmi.server.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.osiris2.reports.*;

class RemittanceReportAbstractRPTSummaryModel extends com.rameses.etracs.shared.ReportController 
{
    @Service("LandTaxReportAbstractTaxCollectionService")
    def svc
            
    String title = "Abstract of Real Property Tax Collection (Summary)";
    String reportpath = "com/rameses/gov/etracs/landtax/reports/"
    String reportName = reportpath + 'abstract_rpt_collection_summary.jasper' 
            
    def data = [:]
            
    def getReportData(){
        data =  svc.generateAbstractOfRPTCollectionSummary( entity );
        return data.items
    }
            
    Map getParameters(){
        return data.header 
    }
}
