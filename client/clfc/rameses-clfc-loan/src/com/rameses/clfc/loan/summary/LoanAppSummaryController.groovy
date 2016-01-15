package com.rameses.clfc.loan.controller;

import com.rameses.rcp.annotations.*;
import com.rameses.clfc.util.*;

class LoanAppSummaryController
{
    //feed by the caller
    def caller, selectedMenu, loanapp; 
    
    @Service('LoanAppSummaryService')
    def service;
    def htmlbuilder = new SummaryHtmlBuilder();
    
    def getHtmlview() {
        def summary = service.open([objid: loanapp.objid]);
        return htmlbuilder.buildSummary(summary);
    }
}