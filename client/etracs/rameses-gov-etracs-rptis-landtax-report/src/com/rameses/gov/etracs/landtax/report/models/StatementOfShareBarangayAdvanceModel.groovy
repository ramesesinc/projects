package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;

class StatementOfShareBarangayAdvanceModel extends StatementOfShareReportModel
{
    def initReport(){
        entity.advanceyear = entity.year + 1;
        return 'default';
    }
}
