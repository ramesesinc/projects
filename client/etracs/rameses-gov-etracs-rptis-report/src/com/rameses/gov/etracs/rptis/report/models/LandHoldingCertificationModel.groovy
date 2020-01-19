package com.rameses.gov.etracs.rptis.report.models;


import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;

public class LandHoldingCertificationModel extends AbstractCertificationModel
{
    @Service('RPTCertificationLandHoldingService')
    def svc;
    
    
    def getService(){
        return svc;
    }

    void afterInit(){
        showincludemembers  = true;
    }
    
    def reportPath = 'com/rameses/gov/etracs/rpt/report/certification/'
    String reportName = reportPath + 'LandHoldingCertification.jasper'

    SubReport[] getSubReports() {
        return [
            new SubReport('LandHoldingCertificationItem', reportPath + 'LandHoldingCertificationItem.jasper'),
            new SubReport('LandHoldingCertificationItemMembers', reportPath + 'LandHoldingCertificationItemMembers.jasper'),
            new SubReport('FootNoteItem', reportPath + 'FootNoteItem.jasper'),
        ] as SubReport[]
    }
    
    Map getParameters(){
        return [REPORTTITLE:'Land Holdings']
    }
    
    def getCertificationTypes(){
        return [
            [type:'bytaxpayer', caption:'By Taxpayer'],
        ]
    }
    
}