package com.rameses.gov.etracs.rptis.report.models;


import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;

public class FAASVerificationModel extends com.rameses.gov.etracs.rpt.report.certification.AbstractCertificationController
{
    @Service('RPTCertificationFAASVerificationService')
    def svc;
    
    String reportPath = 'com/rameses/gov/etracs/rptis/reports/'
    
    @PropertyChangeListener
    def listener = [
        'entity.include.*': { loadCurrentProperties() }
    ]
    
    public def getService(){
        return svc;
    }
    
    String getReportName(){
        return reportPath + 'faas_verification.jasper'
    }

    SubReport[] getSubReports() {
        return [
            new SubReport('FAASVerificationItem', reportPath + 'faas_verification_item.jasper')
        ] as SubReport[]
    }
    
    
    Map getParameters(){
        return [REPORTTITLE:'Verification of FAAS']
    }
    
    def getCertificationTypes(){
        return [
            [type:'bytaxpayer', caption:'By Taxpayer'],
        ]
    }
    
    void afterInit(){
        entity.includeland = true;
        entity.includebldg = true;
        entity.includemach = true;
        entity.includeplanttree = true;
        entity.includemisc = true;
    }
    
    void afterLookupTaxpayer(){
        loadCurrentProperties();
    }
    
    void loadCurrentProperties(){
        items = [];
        if (entity.taxpayer){
            items = svc.getCurrentProperties(entity);
        }
        listHandler.reload();
    }
    
    def save(){
        entity.items = items.findAll{it.include == true}
        if (!entity.items)
            throw new Exception('At least one selected property is required.');
            
        return svc.createCertification(entity);
    }
    
    def next(){
        mode = MODE_SELECT;
        return 'selectpage';
    }
    
    def items = [];
    
    def listHandler = [
        fetchList : { return items; },
        getRows   : { return items.size()},
    ] as EditorListModel;
    
    void selectAll(){
        items.each{it.include = true};
        listHandler.reload();
    }
    
    void deselectAll(){
        items.each{it.include = false};
        listHandler.reload();
    }
    
    def getPropertyCount(){
        return items.size();
    }
}

