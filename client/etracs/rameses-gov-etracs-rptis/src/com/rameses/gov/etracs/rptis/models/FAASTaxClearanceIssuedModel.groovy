package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class FAASTaxClearanceIssuedModel
{
    @Service('QueryService')
    def qrySvc;

    @Service('LandTaxReportTaxClearanceService')
    def svc; 
    
    def entity;
    def clearances;

    String title = 'List of Tax Clearances Issued'
    
    void init(){
        clearances = svc.getTaxClearancesIssued(getLedger());
    }

    def getLedger() {
        def filter = [_schemaname: 'rptledger'];
        filter.select = 'objid';
        filter.findBy = [faasid: entity.objid];
        def ledger = qrySvc.findFirst(filter);
        if (!ledger) {
            filter.findBy = [tdno: entity.tdno];
            ledger = qrySvc.findFirst(filter);
        }
        if (!ledger) {
            throw new Exception('No clearances found for this FAAS.');
        }
        return ledger;
    }
    
    def listHandler = [
        fetchList :{ clearances }
    ] as BasicListModel
    
}