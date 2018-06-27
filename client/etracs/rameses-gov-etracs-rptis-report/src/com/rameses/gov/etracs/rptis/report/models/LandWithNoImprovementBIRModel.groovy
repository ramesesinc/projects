package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;

public class LandWithImprovementController extends com.rameses.gov.etracs.rpt.report.certification.CertificationBIRController
{
    @Service('RPTCertificationLandBirService')
    def svc;
    
    boolean certbytd = true
    
    String title = 'Land with No Improvements (BIR)';
    
            
    def getService(){
        return svc;
    }
       
    def reportPath = 'com/rameses/gov/etracs/rptis/reports/'
    String reportName = reportPath + 'CertificationLandWithNoImprovement.jasper';
    
    SubReport[] getSubReports() {
        return [
            new SubReport('LandItem', reportPath + 'CertificationLandWithNoImprovementItem.jasper'),
        ] as SubReport[]
    } 
   
    Map getParameters(){
        return [REPORTTITLE: title]
    }
    
    void beforeSave(){
        entity.properties = [];
        entity.properties <<  [objid: entity.faasid];
    }    
    
        
    def getLookupFaas(){
        return InvokerUtil.lookupOpener('faas:lookup',[
            onselect : { 
                if (it.rputype != 'land')
                    throw new Exception('Selected FAAS is not Land. Only land property is allowed.')
                 
                entity.faasid = it.objid;
                entity.tdno= it.tdno;
                entity.taxpayer = it.taxpayer;
                entity.requestedby = it.taxpayer.name;
                entity.requestedbyaddress = it.taxpayer.address;
                service.validateNoImprovements(entity);
            },
            onempty  : { 
                entity.faasid = null;
                entity.tdno= null;
                entity.taxpayer = null;
                entity.requestedby = null;
                entity.requestedbyaddress = null;
            },
        ])
    }
    
    def getCertificationTypes(){
        return [
            [type:'byfaas', caption:'By FAAS'],
        ]
    }
}
