package com.rameses.gov.etracs.rpt.ledger.ui;


import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*
import com.rameses.gov.etracs.rpt.util.*;


public class RPTLedgerManualController extends RPTLedgerController
{
    @Service("RPTLedgerManualService")
    def manualSvc;
    
    @Service('LGUService')
    def lguSvc;
    
    String type = 'manual';
    
    def MODE_CREATE = 'create'
    
    String entityName = 'rptledgermanual'
    
    @PropertyChangeListener
    def listener = [
        'entity.rputype' : {
            if (entity.rputype != 'land')
                entity.idleland = 0;
        },
        'entity.barangay' : {
            entity.fullpin = entity.barangay.pin;
            binding.refresh('entity.fullpin');
        }
    ]
    
    def create(){
        entity = [
            objid   : 'RLM' + new java.rmi.server.UID(), 
            filetype: 'rptledgermanual', 
            state   : STATE_PENDING,
            backtax : false,
            taxable : true,
            idleland : 0,
            totalmv : 0.0,
            totalav : 0.0,
            totalareaha : 0.0,
            partialbasicint: 0.0,
            partialbasic: 0.0,
            partialsef: 0.0,
            partialsefint: 0.0,
            manualdiff: 0.0,
            subledger : [:],
            administrator : [:],
        ]
        mode = MODE_CREATE
        return 'init'
    }
    
    def createSubledger(){
        def retval = create();
        entity.subledger.objid = entity.objid;
        return retval;
    }
    
    def getLookupParentLedger(){
        return InvokerUtil.lookupOpener('rptledger:lookup', [
            onselect: {
                if (it.state == 'PENDING') 
                    throw new Exception('Ledger is still pending.');
                if (it.state == 'CANCELLED') 
                    throw new Exception('Ledger has already been cancelled.');
                entity.subledger.parent = it;
                entity.subledger.subacctno = null;
                binding.refresh('entity.parent.subacctno');
            },
            
            onempty : {
                entity.subledger.parent = [:];
                entity.subledger.subacctno = null;
            }
        ])
    }

    def save(){
        entity.putAll(manualSvc.validateAndCreateManualLedger(entity));
        mode = MODE_READ
        return 'default'
    }

    def update(){
        mode = MODE_READ
        return 'default'
    }
    
    def delete(){
        if (MsgBox.confirm('Delete record?')){
            manualSvc.deleteLedger(entity);
            return '_close';
        }
    }

    
    void approve(){
        if (MsgBox.confirm('Approve ledger?')) {
            entity.putAll(manualSvc.approveLedger(entity))
        }
    }

    def getLgus(){
        def orgclass = OsirisContext.env.ORGCLASS
        def orgid = OsirisContext.env.ORGID

        if ('PROVINCE'.equalsIgnoreCase(orgclass)) {
            return lguSvc.lookupMunicipalities([:])
        }
        else if ('MUNICIPALITY'.equalsIgnoreCase(orgclass)) {
            return [lguSvc.lookupMunicipalityById(orgid)]
        }
        else if ('CITY'.equalsIgnoreCase(orgclass)) {
            return [lguSvc.lookupCityById(orgid)]
        }
        return []
    }

    def getBarangays(){
        if (! entity.lgu)
            return [];
        return lguSvc.lookupBarangaysByRootId(entity.lgu?.objid);
    }
    
    
    List getTxntypes() {
        return svc.getTxnTypes()
    }
    
    List getClassifications() {
        return svc.getClassifications()
    }
    
    List getQuarters(){
        return [1,2,3,4]
    }    
    
    List getRpuTypes(){
        return svc.getRpuTypes();
    }
    
    List getTxnTypes(){
        return svc.getTxnTypes();
    }    
     
    def getSubledgerinfo(){
        if (entity.subledger.objid )
            return 'Subledger of TD No. ' + entity.subledger.parent.tdno + '.'
         return '';
    }
    
    
}

