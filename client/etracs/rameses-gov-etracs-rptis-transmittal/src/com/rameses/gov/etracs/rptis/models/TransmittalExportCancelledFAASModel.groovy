package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class TransmittalExportCancelledFAASModel extends TransmittalExportModel
{
    @Service('CancelledFAASService')
    def cancelledFaasSvc;
    
    @Service('CancelledFAASSupportService')
    def supportSvc;
    
    public def exportItem(transmittalitem){
        def data = [:]
        data.filetype = 'cancelledfaas';
        data.transmittalitem = transmittalitem;
        
        if ('DISAPPROVED'.equalsIgnoreCase(transmittalitem.status)){
            data.cancelledfaas = [objid:transmittalitem.refid, txnno:transmittalitem.refno]; 
        }
        else{
            def cancelledfaas = cancelledFaasSvc.openCancelledFaas([objid:transmittalitem.refid]);
            data.cancelledfaas = supportSvc.getCancelledFaasData(cancelledfaas);
        }
        return data;
    }
      
}