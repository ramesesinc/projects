package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class TransmittalExportRedFlagModel extends TransmittalExportModel
{
    @Service('RPTRedFlagService')
    def redflagSvc;
    
    @Service('FAASChangeInfoService')
    def changeinfoSvc;
    
    public def exportItem(transmittalitem){
        def data = [:]
        data.filetype = 'rptredflag';
        data.transmittalitem = transmittalitem;
        data.redflag = redflagSvc.open([objid:transmittalitem.refid]);
        
        if ('MUNICIPALITY'.equalsIgnoreCase(OsirisContext.env.ORGCLASS)){
            data.changeinfo = changeinfoSvc.openByRedFlag(data.redflag);
            data.changeinfo.remove = true;
        }
        
        return data;
    }
      
}