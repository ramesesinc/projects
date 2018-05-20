package com.rameses.enterprise.treasury.models;

import com.rameses.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

//This is not a standlone report. It is called from RemittanceModel
class RemittanceApprovalModel extends com.rameses.seti2.models.SimpleFormReportModel {

    @Service("RemittanceService")
    def service;  

    @Caller
    def caller;
    
    def entity;
    def onAccept;
    def onReject;
    def _reportData;
    
    public Object getReportData() {
        if(_reportData) return _reportData;
        _reportData = service.getReportData([ objid: entity.objid ]); 
        return _reportData;
    }
    
    
    public String getReportName() {
        return "com/rameses/enterprise/treasury/printforms/remittance.jasper";    
    }

    void accept() {
        service.acceptForLiquidation( [objid: reportData.objid ] );
        reportData.state = 'POSTED';
        if(onAccept)onAccept()
        //return "_close"
    }

    def sendBack() {
        if ( MsgBox.confirm('You are about to change the transaction status to DRAFT. Proceed?')) {
            service.sendBack([ objid: reportData.objid ]); 
            if(onReject)onReject()
            return '_close'; 
        } 
        return null; 
    } 
    
    void signCollector() {
        def h = { o->
            service.updateCollectorSignature( [objid: reportData.objid, signature: o ] );
            Base64Cipher cipher = new Base64Cipher()
            reportData.collector.signature = cipher.decode(o);
            viewReport();
        }
        Modal.show( "verify_submit_with_signature", [handler: h] );
    }
    
    void signApprover() {
        def h = { o->
            service.updateApproverSignature( [objid: reportData.objid, signature: o ] );
            Base64Cipher cipher = new Base64Cipher()
            reportData.liquidatingofficer.signature = cipher.decode(o);
            viewReport();
        }
        Modal.show( "verify_submit_with_signature", [handler: h] );
    }
}