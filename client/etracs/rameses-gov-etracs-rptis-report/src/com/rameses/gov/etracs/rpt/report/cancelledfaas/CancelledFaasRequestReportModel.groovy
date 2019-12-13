package com.rameses.gov.etracs.rpt.report.cancelledfaas;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.gov.etracs.rptis.util.*;

class CancelledFaasRequestReportModel extends CrudReportModel {
    @Service('CancelledFAASService')
    def svc
    
    void view() {
        data = svc.buildReportData(entity);
        saveSignatures(data);
        super.view();
    }
    
    void saveSignatures(reportdata){
        reportdata.signatories.each{ k, v ->
            def objid = v.objid + '-' + v.state 
            if (v.signature?.image)
                v.signatureis = DBImageUtil.getInstance().saveImageToFile(objid, v.signature.image)
        }
    }
    
}