package com.rameses.gov.etracs.rptis.report.models;


import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;

public class LandHoldingWithImprovementModel extends AbstractCertificationModel
{
    @Service('RPTCertificationLandHoldingService')
    def svc;
    
    
    def getService(){
        return svc;
    }

    
    def reportPath = 'com/rameses/gov/etracs/rpt/report/certification/';
    String reportName = reportPath + 'LandHoldingCertification.jasper';

    void afterInit(){
        showincludemembers  = true;
    }
    
    def save(){
        return svc.createLandHoldingWithImprovement(entity);
    }

    SubReport[] getSubReports() {
        return [
            new SubReport('LandHoldingCertificationItem', reportPath + 'LandHoldingCertificationItem.jasper'),
            new SubReport('LandHoldingCertificationItemMembers', reportPath + 'LandHoldingCertificationItemMembers.jasper'),
            new SubReport('FootNoteItem', reportPath + 'FootNoteItem.jasper'),
        ] as SubReport[]
    }
    
    Map getParameters(){
        return [REPORTTITLE:'Land Holding with Improvements']
    }
    
    def getCertificationTypes(){
        return [
            [type:'bytaxpayer', caption:'By Taxpayer'],
        ]
    }
    
}