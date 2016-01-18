package com.rameses.gov.etracs.rpt.transmittal.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.util.*;


public class TransmittalImporter
{
    def showInfo;
    def service;
    def importer;
    def helper;
    
    public TransmittalImporter(def service, def showInfo){
        this.service = service;
        this.showInfo = showInfo;
        
        helper = [
            importSignatories   : importSignatories,
            importRequirements  : importRequirements,
            importImages        : importImages,
            showInfo            : showInfo
        ]
        
        importer = [
            faas : new FaasImport(service:service, helper:helper),
            transmittal : new TransmittalImport(service:service, helper:helper),
        ]
    }
    
    void importData(data){
        def imp = importer[data.filetype];
        if (!imp)
            throw new Exception('Importer Class for type ' + data.filetype + ' is not defined.');
        imp.importData(data);
        doSleep();
    }
    
    void doSleep(){
        try{
            Thread.sleep(500);
        }
        catch(e){
            ;
        }
    }   
    
    def importSignatories = {data ->
        data.signatories.each{
            service.importSignatory(data.filetype, it, data.transmittalid);
        }
    }
    
    def importRequirements = {data -> 
        data.requirements.each{
            service.importRequirement(it, data.transmittalid);
        }
    }
    
    def importImages = {data ->
        data.images.each{ header ->
            try{
                service.importImageHeader(header, data.transmittalid);
                header.chunks.each{
                    service.importImageChunk(it);
                }
            }
            catch(e){
                //see rpttransmittal_log
            }
            
        }
    }
       
}

class FaasImport
{
    def service;
    def helper;
    
    void importData(data){
        helper.showInfo('Importing FAAS ' + data.faas.tdno + '...');
        try{
            service.importFaas(data.faas, data.transmittalid);
            helper.importSignatories(data);
            helper.importRequirements(data);
            helper.importImages(data);
        }
        catch(e){
            // see rpttransmittal_log 
        }
        helper.showInfo('done.\n');
    }
}


class TransmittalImport
{
    def service;
    def helper;
    
    void importData(data){
        helper.showInfo('Importing Transmittal ' + data.transmittal.txnno + '...');
        service.importTransmittal(data);
        helper.showInfo('done.\n');
    }
}
