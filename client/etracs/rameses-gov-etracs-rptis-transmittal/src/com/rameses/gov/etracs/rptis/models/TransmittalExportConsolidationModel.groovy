package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class TransmittalExportConsolidationModel extends TransmittalExportModel
{
    @Service('ConsolidationService')
    def consolidationSvc;
    
    @Service('ConsolidationSupportService')
    def supportSvc;
    
    public def exportItem(transmittalitem){
        def data = [:]
        data.filetype = 'consolidationdata';
        data.transmittalitem = transmittalitem;
        
        if ('DISAPPROVED'.equalsIgnoreCase(transmittalitem.status)){
            data.consolidationdata = [objid:transmittalitem.refid, txnno:transmittalitem.refno]; 
        }
        else{
            def consolidation = consolidationSvc.open([objid: transmittalitem.refid]);
            if (consolidation.originlguid == OsirisContext.env.ORGID)
                data.consolidationdata = supportSvc.buildConsolidationData(consolidation);
            else
                data.consolidationdata = supportSvc.buildFaasesData(consolidation);                
        }
        return data;
    }
      
}