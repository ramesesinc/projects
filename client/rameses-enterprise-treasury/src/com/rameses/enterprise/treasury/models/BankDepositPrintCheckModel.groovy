package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;
import java.rmi.server.*;

class BankDepositPrintCheckModel {

    @Binding
    def binding ;

    @Service('BankService')
    def bankSvc;

    def entity ;
    def checks ;
    def stdparams = [:];
    def reportdata = [:];
    def reportname ;
    def subreportname;

    def totalcheck ;

    @PropertyChangeListener
    def listener= [
        "entity.noofchecksperpage" : {o-> 
            entity.frompage = 1
            entity.topage = getTotalNoOfPages()
            reportdata.pagecount = (Integer) getTotalNoOfPages();
            binding.refresh("entity.frompage|entity.topage")
        },

        "deposittype" : {
            reportdata.checks = []
            if (deposittype != null){
                reportdata.checks = checks.findAll{it.deposittype == deposittype }
                reportdata.checks.each{ it.selected = true } 
            }
            numchecks = reportdata.checks.size();
            listHandler.reload();
            entity.frompage = 1
            entity.topage = 1
            reportdata.pagecount = 1;
            binding.refresh("numchecks|entity.frompage|entity.topage");
        }
    ] 

    def init() {
        entity = [:]
        entity.printrange = "all" 
        numchecks  = 0 
    }

    def doOk() {
        if( entity.printrange == "range"){
            if( entity.frompage < 1 || entity.frompage > entity.topage )
                throw new Exception("Pages from is out of range. Please check.  ") 

            if( entity.topage < entity.frompage  )
                throw new Exception("Pages from is out of range. Please check.  ")     

            if( entity.topage > totalcheck ) entity.topage = totalcheck 
        } 

        def list = reportdata.checks.findAll{it.selected == true}.clone()
        def p = entity.frompage 
        for( int i=1; i <= entity.topage; i++) {
            reportdata.noncashpayments = []
            if( i != p) {
                reportdata.noncashpayments.addAll( list[0..(entity.noofchecksperpage -1 )] )
                list.removeAll( reportdata.noncashpayments );
                continue;
            } else if( list.size() >= entity.noofchecksperpage) {
                reportdata.noncashpayments.addAll( list[0..(entity.noofchecksperpage -1 )] )
            } else if( list.size() > 0) {
                reportdata.noncashpayments.addAll( list )
            }

            reportdata.noncashpayments.each{ it.checkno = it.refno } 
            reportdata.noncash = reportdata.noncashpayments.amount.sum()     
            MsgBox.alert("Insert check deposit slip.\n Printing page $i of ${reportdata.pagecount}. " )
            reportdata.pagenumber = i;
            report.viewReport(); 

            if ( ReportUtil.isDeveloperMode()) { 
                return Inv.lookupOpener('report:preview', [ 
                    report: report, 
                    title : 'Check Deposit Slip' 
                ]);
            } 

            ReportUtil.print( report.report, true );

            list.removeAll( reportdata.noncashpayments );
            p++;
        }
        return null; 
    }

    def doCancel() { return "_close"}

    def getTotalNoOfPages( ) {
        if( entity.noofchecksperpage >= numchecks) return 1 

        def rem = entity.noofchecksperpage - ( numchecks.mod(entity.noofchecksperpage)  ) 
        return (numchecks + rem) / entity.noofchecksperpage
    }

    def report = [
        getParameters : { return stdparams },
        getReportName : { return reportname }, 
        getReportData : { return reportdata }, 
        getSubReports : {
            return [
                new SubReport("checkbreakdownitem", subreportname )
            ] as SubReport[]
        }
    ] as ReportModel;

    def deposittype;
    def getDeposittypes(){
        return bankSvc.getDepositTypes();
    } 

    def numchecks;
    def listHandler = [
        fetchList : { 
            return reportdata.checks
        }, 
        onColumnUpdate: {item, colname-> 
            if (colname == 'selected') {
                 numchecks = reportdata.checks.findAll{it.selected == true}.size();
                 binding.refresh("numchecks")
            }
        }        
    ] as EditorListModel; 
}  
