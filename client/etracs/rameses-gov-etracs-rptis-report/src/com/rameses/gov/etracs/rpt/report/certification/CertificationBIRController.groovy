package com.rameses.gov.etracs.rpt.report.certification;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rpt.report.certification.AbstractCertificationController;

public abstract class CertificationBIRController extends AbstractCertificationController
{
    @PropertyChangeListener
    def listner = [
        'entity.asofyear' : {
            entity.taxpayer = null;
            entity.requestedby = null;
            entity.requestedbyaddress = null;
            properties = [];
            listHandler.reload();
            
            binding.refresh('entity.taxpayer.*|entity.requestedby.*');
        }
    ]
    
    
    boolean showasofyear = true;
    
    void afterLookupTaxpayer(){
        properties = service.getPropertiesForBIR(entity);
        listHandler.reload();
    }
    
    def properties; 
    
    def listHandler = [
        fetchList : { properties },
    ] as EditorListModel;
    
    
    void afterInit(){
        properties = [];
        listHandler?.reload();
    }
    
    void beforeSave(){
        entity.properties = properties.findAll{it.selected == true}
        if (!entity.properties)
            throw new Exception('At least one (1) property to certify is required.')
    }
    
    void selectAll(){
        properties.each{ it.selected = true;}
        listHandler.reload();
    }
    
    void deselectAll(){
        properties.each{ it.selected = false;}
        listHandler.reload();
    }
    
}

