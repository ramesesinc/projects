package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class TransmittalExportSubdivisionModel extends TransmittalExportModel
{
    @Service('SubdivisionService')
    def subdivisionSvc;
    
    @Service('SubdivisionSupportService')
    def supportSvc;
    
    public def exportItem(transmittalitem){
        def data = [:]
        data.filetype = 'subdivisiondata';
        data.transmittalitem = transmittalitem;
        
        if ('DISAPPROVED'.equalsIgnoreCase(transmittalitem.status)){
            data.subdivisiondata = [objid:transmittalitem.refid, txnno:transmittalitem.refno]; 
        }
        else{
            def subdivision = subdivisionSvc.open([objid: transmittalitem.refid]);
            if (subdivision.originlguid == OsirisContext.env.ORGID)
                data.subdivisiondata = supportSvc.buildSubdivisionData(subdivision);
            else
                data.subdivisiondata = supportSvc.buildFaasesData(subdivision);                
        }
        return data;
    }
      
}