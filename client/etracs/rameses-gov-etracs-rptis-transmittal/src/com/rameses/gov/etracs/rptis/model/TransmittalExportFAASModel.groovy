package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class TransmittalExportFAASModel extends TransmittalExportModel
{
    @Service('FAASSupportService')
    def supportSvc;
    
    public def exportItem(transmittalitem){
        def faasdata = supportSvc.getFaasData([objid:transmittalitem.refid]);
        return [
            filetype : 'faasdata',
            faasdata : faasdata,
        ]
    }
      
}