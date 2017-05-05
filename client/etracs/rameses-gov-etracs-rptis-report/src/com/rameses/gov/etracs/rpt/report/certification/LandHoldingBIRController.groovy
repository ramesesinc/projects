package com.rameses.gov.etracs.rpt.report.certification;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rpt.report.certification.CertificationBIRController;

public class LandHoldingBIRController extends CertificationBIRController
{
    @Service('RPTCertificationLandHoldingBirService')
    def svc;
    
    def getService(){
        return svc;
    }
    
    def reportPath = 'com/rameses/gov/etracs/rpt/report/certification/'
    String reportName = reportPath + 'LandHoldingCertification.jasper'

    SubReport[] getSubReports() {
        return [
            new SubReport('LandHoldingCertificationItem', reportPath + 'LandHoldingCertificationItem.jasper'),
            new SubReport('FootNoteItem', reportPath + 'FootNoteItem.jasper'),
        ] as SubReport[]
    }
}

