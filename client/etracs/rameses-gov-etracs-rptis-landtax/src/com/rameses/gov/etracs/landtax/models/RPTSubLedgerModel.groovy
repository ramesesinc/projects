package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class RPTSubLedgerModel
{       
    
    @Binding 
    def binding;
    
    @Service('RPTLedgerService')
    def svc;
    
    @Service('NumberService')
    def numSvc;
    
    def onadd;
    def onupdate;
    
    
    def ledger;
    def entity;
    def mode;
    def totalSubledgerAreaSqm;
    def totalSubledgerMV;
    def totalSubledgerAV;
    
    def MODE_CREATE = 'create';
    def MODE_EDIT = 'edit'
    def MODE_READ = 'read';
    
    @PropertyChangeListener
    def listener = [
        'entity.totalareaha' : {
            entity.totalareasqm = entity.totalareaha * 10000.0;
            calcMvAv()
            binding?.refresh('entity.(totalareasqm|totalmv|totalav)');
        },
        'entity.totalareasqm' : {
            entity.totalareaha = entity.totalareasqm / 10000.0;
            calcMvAv()
            binding?.refresh('entity.(totalareaha|totalmv|totalav)');
        },
        'entity.taxpayer' : {
            def address = entity.taxpayer.address.text 
            entity.taxpayer.address = address;
            binding?.refresh('entity.taxpayer.address');
        }
    ]
    
    void calcMvAv(){
        entity.totalmv = numSvc.round(entity.totalareasqm / ledger.totalareasqm * ledger.totalmv);
        entity.totalav = numSvc.roundToTen(entity.totalareasqm / ledger.totalareasqm * ledger.totalav);
    }
    
    String getTitle(){
        def t = 'Realty Tax SubLedger';
        if (mode == MODE_CREATE)
            return t + ' (Create)';
        return t;
    }
    
    void create(){
        entity = [:]
        entity.putAll(ledger);
        entity.objid = 'SL' + new java.rmi.server.UID();
        entity.parent = [objid:ledger.objid];
        entity.rptledger = ledger;
        entity.state = 'PENDING';
        entity.taxpayer = null;
        entity.totalav = 0.0;
        entity.totalareasqm = ledger.totalareasqm - totalSubledgerAreaSqm;
        entity.totalareaha = entity.totalareasqm / 10000.0;
        calcMvAv();
        entity.partialbasic = 0.0;
        entity.partialbasicint = 0.0;
        entity.partialsef = 0.0;
        entity.partialsefint = 0.0;
        mode = MODE_CREATE;
    }
    
    void edit(){
        mode = MODE_EDIT;
    }
    
    void cancel(){
        mode = MODE_READ;
    }
    
    void open(){
        entity.state = entity.rptledger.state;
        entity.taxpayer = entity.rptledger.taxpayer;
        entity.totalav = entity.rptledger.totalav
        entity.totalmv = entity.rptledger.totalmv
        entity.totalareaha = entity.rptledger.totalareaha
        entity.totalareasqm = entity.rptledger.totalareasqm
        entity.lastyearpaid = entity.rptledger.lastyearpaid
        entity.lastqtrpaid = entity.rptledger.lastqtrpaid
        mode = MODE_READ;
    }
    
    void save(){
        validate()
        updateSubLedgerInfo();
        
        if (mode == MODE_CREATE){
            svc.createSubLedger(entity);
            if (onadd) onadd(entity);
        }
        else{
            svc.updateSubLedger(entity);
            if (onupdate) onupdate(entity);
        }
        mode = MODE_READ;
    }
    
    void approve(){
        if (MsgBox.confirm('Approve?') ){
            entity.putAll(svc.approveSubLedger(entity));
            if (onupdate) onupdate(entity);
        }
    }
    
    
    void updateSubLedgerInfo(){
        entity.tdno = ledger.tdno + '-' + entity.subacctno;
        entity.fullpin = ledger.fullpin + '-' + entity.subacctno;
        entity.faases = [];
        entity.faases << createLedgerFaas();
    }
    
    def createLedgerFaas(){
        return [
            objid                   : entity.objid,
            state                   : entity.state,
            rptledgerid             : entity.objid,
            faasid                  : null, 
            tdno                    : ledger.tdno,
            txntype                 : ledger.txntype,
            classification          : ledger.classification,
            actualuse               : ledger.classification,
            taxable                 : (ledger.taxable == 1) ,
            backtax                 : false,
            fromyear                : entity.lastyearpaid,
            fromqtr                 : 1,
            toyear                  : 0,
            idleland                : 0,
            toqtr                   : 0,
            assessedvalue           : entity.totalav,
            systemcreated           : true,
            reclassed               : false,
        ]
    }
    
    
    
    void validate(){
        if (entity.lastyearpaid < ledger.lastyearpaid)
            throw new Exception('Last year paid be greater than or equal to ' + ledger.lastyearpaid + '.');
            
        if (entity.lastyearpaid == ledger.lastyearpaid && entity.lastqtrpaid < ledger.lastqtrpaid  )
            throw new Exception('Last quarter paid must be greater than or equal to ' + ledger.lastqtrpaid + '.');
            
        if (entity.totalareaha == ledger.totalareaha)
            throw new Exception('Sub-Ledger area must be less than Main Ledger area.');
            
        def totalSubledgerAreaHa = totalSubledgerAreaSqm / 10000;
        if (entity.totalareaha + totalSubledgerAreaHa > ledger.totalareaha)
            throw new Exception('Total Sub-Ledger area must not exceed Main Ledger area.');
            
        if (entity.totalmv + totalSubledgerMV > ledger.totalmv)
            throw new Exception('Total Sub-Ledger market value must not exceed Main Ledger market value.');
            
        if (entity.totalav + totalSubledgerAV > ledger.totalav)
            throw new Exception('Total Sub-Ledger assessed value must not exceed Main Ledger assessed value.');
    }
    

}