package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class TransactionInitModel {
    def openers;
    def opener;
    
    String title = "New FAAS Transaction";
    
    void init() {
        openers = Inv.lookupOpeners('faas:transaction').sort{ a, b ->
            a.properties.index <=> b.properties.index
        }
        
    }
    
    def process() {
        return opener;
    }
}