package com.rameses.gov.etracs.rpt.collection.ui;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rpt.common.*;


class RPTReceiptNoLedgerAddController
{
    @Binding
    def binding;
    
    @Service('RPTReceiptNoLedgerService')
    def svc;
            
    @Service('PropertyClassificationService')
    def propSvc;
    
    @Service('LGUService')
    def lguSvc;
    
    def onAdd;      //handler 
    def onUpdate;   //handler
    
    def MODE_CREATE = 'create';
    def MODE_EDIT   = 'edit';
    
    def mode;
    def ledgerfaas;
    def bill;
    def partial 
    
    String getTitle(){
        if (mode == MODE_CREATE)
            return 'Realty Tax No Ledger (Add)'
        return 'Realty Tax No Ledger (Edit)'
    }
    
            
    
    void init(){
        bill.rptledger = [faases:[]]
        ledgerfaas = [:]
        ledgerfaas.owner = [:]
        partial = [amount: 0.0]
        mode   = MODE_CREATE;
    }
    
    
    void edit(){
        mode = MODE_EDIT;
    }
    
    def doClose(){
        if (MsgBox.confirm('Cancel entry?')){
            return '_close'
        }
        return null;
    }
    
    
    def doAdd(){
        if (MsgBox.confirm('Add ledger faas?')){
            if (onAdd ) onAdd(ledgerfaas);
            return '_close';
        }
        return null;
    }
    
    
    def doUpdate(){
        if (MsgBox.confirm('Update ledger?')){
            if (onUpdate) onUpdate(ledgerfaas);
            return '_close';
        }
        return null;
    }
    
    
    
    void calculateDue(){
        validateInfo();
        bill.noledger = true;
        bill.billtoyear = ledgerfaas.toyear;
        bill.billtoqtr = ledgerfaas.toqtr;
        bill.rptledger.faases.clear();
        bill.partial = partial 
        ledgerfaas.items = [];
        ledgerfaas.taxes  = []
        bill.rptledger.faases << ledgerfaas;
        bill = svc.calculateDue(bill);
        ledgerfaas.items = bill.items;
        ledgerfaas.taxes = bill.taxes;
        ledgerfaas.putAll(bill.ledgerfaasinfo)
        listHandler.load();
    }
    
    void validateInfo(){
        if (ledgerfaas.fromyear <= 0)
            throw new Exception('From Year must be greater than zero.')
        if (ledgerfaas.toyear <= 0)
            throw new Exception('To Year must be greater than zero.')
        if (ledgerfaas.fromyear > ledgerfaas.toyear)
            throw new Exception('From Year must be less than or equal to To Year.')
        if (ledgerfaas.fromyear == ledgerfaas.toyear && ledgerfaas.fromqtr > ledgerfaas.toqtr)
            throw new Exception('From Quarter must be less than or equal to To Quarter.')
    }

    
    def listHandler = [
        fetchList : { return ledgerfaas.items; },
        onCommitItem  : {item ->
            item.basicnet = item.basic + item.basicint - item.basicdisc;
            item.sefnet = item.sef + item.sefint - item.sefdisc;
            item.total = item.basicnet + item.sefnet + item.firecode;
            binding.refresh('amountdue');
        }
    ] as EditorListModel
    
    
    
    def getQuarters(){
        return [1,2,3,4]
    }
    
    
    def getClassifications(){
        return propSvc.getList([:])
    }
    
    
    def getRputypes(){
        return ['land', 'bldg', 'mach', 'planttree', 'misc']
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
        if (! ledgerfaas.lgu)
            return [];
        return lguSvc.lookupBarangaysByRootId(ledgerfaas.lgu?.objid);
    }
   
    
    def getTxntypes(){
        return svc.getTxntypes();
    }
    
    
    def getAmountdue(){
        def due = ledgerfaas.items?.total.sum();
        if (due == null) due = 0.0;
        return due;
    }
    
}

