package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class StatementOfShareModel 
{
    def report;
    def reports;

    String title = 'Statement of Share Reports'

    void init() {
        def entity = [context: OsirisContext]
        reports = Inv.lookupOpeners('statementofshare:report', [context: OsirisContext]).findAll{
            def vw = it.properties.visibleWhen;
            return  ((!vw)  ||  ExpressionResolver.getInstance().evalBoolean(vw, [context: OsirisContext]));
        }
    }

    def next() {
        if (!report) throw new Exception('Report Type should be specified.');
        return report;
    }

}
