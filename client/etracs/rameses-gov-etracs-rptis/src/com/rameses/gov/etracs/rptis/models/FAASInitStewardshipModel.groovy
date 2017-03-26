package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class FAASInitStewardshipModel extends FAASInitTxnModel
{
    @Service('FAASStewardshipService')
    def stewardSvc;
    
    def newpininfo = 'New Stewardship PIN';
    def nextstewardshipno;
    
    @PropertyChangeListener 
    def listener = [
        'entity.*' : { buildPin(); }
    ]
    
    void buildPin() {
        entity.fullpin = newpininfo;
        if (entity.faas && entity.stewardshipno){
            def stewardshipno = entity.stewardshipno.toString().padLeft(3,'0')
            entity.fullpin = entity.faas.fullpin + '-' + stewardshipno
        }
        binding.refresh('entity.(fullpin|stewardshipno)');
    }
    
    
    void afterInit(){
        entity.fullpin = newpininfo;
    }
    
    def getLookupFaas(){
        return InvokerUtil.lookupOpener('faas:lookup', [
            state   : 'CURRENT',
            rputype : 'land', 
            
            onselect: {
                if (it.state == 'CANCELLED')
                    throw new Exception('FAAS has already been cancelled.');
                if (it.state != 'CURRENT')
                    throw new Exception('Cannot process record. The FAAS is not yet current.')
                if (it.stewardshipno)
                    throw new Exception('Cannot process record. The FAAS is already under stewardship.');
                    
                 entity.faas = it;
                 entity.ry = entity.faas.ry 
                 nextstewardshipno = stewardSvc.getNextStewardshipNo([rpumasterid:entity.faas.rpumasterid]);
                 entity.stewardshipno = nextstewardshipno;
                 println 'entity.stewardshipno -> ' + entity.stewardshipno
                 buildPin();
            },
            
            enempty :{ entity.faas = null; }
        ])
    }
        
    def process(){
        if (entity.stewardshipno < nextstewardshipno)
            throw new Exception('Stewardship No. must be greater than or equal to ' + nextstewardshipno + '.');
        def faas = stewardSvc.initStewardship(entity);
        return InvokerUtil.lookupOpener('faas:open', [entity:faas]);
    }
    
}
