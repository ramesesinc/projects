package com.rameses.gov.etracs.rptis.report.models;


import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;

public class MultiplePropertyModel extends AbstractCertificationModel
{
    @Service('RPTCertificationMultipleSevice')
    def svc;
    
    
    def getService(){
        return svc;
    }

    
    def reportPath = 'com/rameses/gov/etracs/rpt/report/certification/'
    String reportName = reportPath + 'MultiplePropertyCertification.jasper'

    void afterInit(){
        showincludemembers  = true;
    }
    
    SubReport[] getSubReports() {
        return [
            new SubReport('MultiplePropertyCertificationItem', reportPath + 'MultiplePropertyCertificationItem.jasper'),
            new SubReport('MultiplePropertyCertificationItemMembers', reportPath + 'MultiplePropertyCertificationItemMembers.jasper'),
            new SubReport('FootNoteItem', reportPath + 'FootNoteItem.jasper'),
        ] as SubReport[]
    }
    
    Map getParameters(){
        return [REPORTTITLE:'Real Property Holdings']
    }
    
    def getCertificationTypes(){
        return [
            [type:'bytaxpayer', caption:'By Taxpayer'],
        ]
    }
    
}