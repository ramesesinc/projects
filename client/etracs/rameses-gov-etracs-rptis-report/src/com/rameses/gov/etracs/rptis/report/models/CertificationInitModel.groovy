package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class CertificationInitModel {
    def openers;
    def opener;
    
    String title = "Issue Certification";
    
    void init() {
        openers = Inv.lookupOpeners('rptcertification')
    }
    
    def issue() {
        return opener;
    }
}