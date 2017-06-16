package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class TransmittalExportFAASModel extends TransmittalExportModel
{
    @Service('FAASSupportService')
    def supportSvc;
    
    public def exportItem(transmittalitem){
        def data = [:]
        data.filetype = 'faasdata';
        data.transmittalitem = transmittalitem;
        
        if ('DISAPPROVED'.equalsIgnoreCase(transmittalitem.status)){
            data.faasdata = [objid:transmittalitem.refid, tdno:transmittalitem.refno]; 
        }
        else{
            data.faasdata = supportSvc.getFaasData([objid:transmittalitem.refid]);
        }
        return data;
    }
      
}