package com.rameses.enterprise.treasury.models;

import com.rameses.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
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
    
    def decFormat = new java.text.DecimalFormat('0.00');     
    public Object getReportData() {
        if( _reportData) return _reportData;
        
        _reportData = service.getReportData([ objid: entity.objid ]); 
        def list = _reportData.cashbreakdown; 
        list.each{
            it.indexno = ((Number) (it.denomination ? it.denomination : 0)).intValue(); 
        }
        list.sort{ -it.indexno }
        list.each { 
            it.caption = it.denomination.toString(); 
            if ( it.denomination instanceof Number ) {
                it.caption = decFormat.format( it.denomination ); 
            }
        } 
        _reportData.cashbreakdown = list; 
        return _reportData;
    }
    
    void view() { 
        def path = 'com/rameses/gov/treasury/remittance/report/rcd'; 
        def mainreport = path + '/rcd_main.jasper'; 
        def subreports = new SubReport[8]; 
        subreports[0] = new SubReport('CollectionType', path + '/collectiontype.jasper');
        subreports[1] = new SubReport('CollectionSummary', path + '/collectionsummary.jasper');
        subreports[2] = new SubReport('RemittedForms', path + '/remittedforms.jasper');
        subreports[3] = new SubReport('NonSerialRemittances', path + '/nonserialremittances.jasper');
        subreports[4] = new SubReport('NonSerialSummary', path + '/nonserialsummary.jasper');
        subreports[5] = new SubReport('OtherPayments', path + '/otherpayments.jasper');
        subreports[6] = new SubReport('Denomination', path + '/denomination.jasper');
        subreports[7] = new SubReport('CancelSeries', path + '/cancelseries.jasper');
        
        reportHandler = [
            getReportName: {
                return mainreport; 
            },
            getSubReports: {
                return subreports; 
            }
        ]; 
        super.view(); 
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