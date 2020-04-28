package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class TaxClearanceEditModel
{
    def entity;
    def manualCertificationNo;
    def showTransferPayment;
    def afterEdit = {};

    @Service('LandTaxReportTaxClearanceService')
    def svc;

    def save() {
        if (MsgBox.confirm('Save changes?')) {
            svc.update(entity);
            afterEdit();
            return '_close';
        }
    }

}
