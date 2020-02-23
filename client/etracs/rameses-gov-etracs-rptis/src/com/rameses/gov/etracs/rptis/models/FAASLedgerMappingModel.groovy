package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
        
public class FAASLedgerMappingModel {
    @Binding
    def binding;

    @Service("RPTUtilityService")
    def svc;

    def entity;
    def ledger;
    def hasledger;
    def info;

    void init() {
        def data= svc.getLedgerByFaas([objid: entity.objid, tdno: entity.tdno]);
        ledger = data.ledger
        hasledger = data.hasledger
        if (hasledger) {
            info = 'FAAS and Ledger are currently mapped.'
        }
    }

    String getTitle() {
        String title = 'Change FAAS and Ledger Mapping';
        if (hasledger) {
            title = 'FAAS and Ledger Mapping';
        }
        return title;
    }
    
    

    void updateMapping() {
        if (MsgBox.confirm('Update ledger mapping for this FAAS?')) {
            def data = svc.changeFAASLedgerMapping([faasid: entity.objid, rptledgerid: ledger.objid]);
            ledger = data.ledger
            hasledger = data.hasledger       
            // binding.refresh();
        }
    }
}