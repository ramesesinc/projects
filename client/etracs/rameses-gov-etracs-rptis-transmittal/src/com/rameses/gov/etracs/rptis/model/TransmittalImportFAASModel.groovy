package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.io.File;

public class TransmittalImportFAASModel extends TransmittalImportModel
{   
    @Service('FAASSupportService')
    def faasSvc;
    
    @Service('RPTTransmittalService')
    def transmittalSvc;
    
    public def getFileType(){
        return 'faas';
    }
    
    public void importData(data){
        if (data.filetype == 'faasdata'){
            println 'saving faas...'
            faasSvc.saveFaasData(data.faasdata);
            println 'saving item...'
            transmittalSvc.saveItem(data.transmittalitem);
        }
    }
}