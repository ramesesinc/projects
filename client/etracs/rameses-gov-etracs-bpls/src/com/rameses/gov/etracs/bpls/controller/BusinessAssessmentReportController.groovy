package com.rameses.gov.etracs.bpls.controller;

import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;

class BusinessAssessmentReportController extends com.rameses.etracs.shared.ReportController {

    String title = "Business Assessment";
    String reportpath = 'com/rameses/gov/etracs/bpls/reports/assessment/'; 

    def data;
    def task;
    
    String getReportPath() { 
        return reportpath; 
    } 
    String getReportName() {
        return getReportPath() + 'Assessment.jasper'; 
    } 

    SubReport[] getSubReports() {  
        return [
            new SubReport("AssessmentItem", getReportPath() + "AssessmentItem.jasper"), 
            new SubReport("AssessmentGross", getReportPath() + "AssessmentGross.jasper") 
        ] as SubReport[] 
    }

    def getReportData() {
        entity.bin = entity.business.bin;
        entity.totaltax = entity.totals?.tax;
        entity.totalregfee = entity.totals?.regfee;
        entity.totalcharge = entity.totals?.othercharge;
        entity.totalamount = entity.totals?.total;

        def taxes = entity.taxfees.findAll{ it.taxfeetype=='TAX' }
        def regfees = entity.taxfees.findAll{ it.taxfeetype=='REGFEE' }
        def charges = entity.taxfees.findAll{ it.taxfeetype.toString().matches('TAX|REGFEE')==false }
        taxes?.each{ 
            it.sortindexno=0 
        }
        regfees?.each{ 
            it.sortindexno = (it.lob?.objid == null? 2: 1); 
        }
        charges?.each{  
            it.sortindexno = (it.lob?.objid == null? 4: 3); 
        } 
        entity.taxfees.sort{ it.sortindexno } 
        entity.applications = []; 
        entity.applications << [
            year: entity.appyear, taxes: taxes, 
            regfees: regfees.sort{ it.sortindexno }, 
            charges: charges.sort{ it.sortindexno }  
        ]; 
        afterReportData( entity ); 
        return entity; 
    } 

    public void afterReportData( entity ) {
        //do nothing 
    } 
} 
