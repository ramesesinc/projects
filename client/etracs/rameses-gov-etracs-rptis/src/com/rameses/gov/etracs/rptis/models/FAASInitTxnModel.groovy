package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class FAASInitTxnModel
{
    @Binding
    def binding;
    
    @Invoker 
    def invoker 
    
    @Service('FAASService')
    def svc;
    
    @Service('Var')
    def var; 

    def entity = [:]
    def txntitle;
    def showCapture = false;
    def attributes;
    
    String getTitle(){
        return invoker.caption + ': Initial';
    }
    
    
    void afterInit(){}
    
    void init(){
        txntitle = invoker.caption;
        entity.txntype = svc.getTxnType(invoker.properties.txntype.toUpperCase());
        entity.datacapture = false;
        attributes = svc.getTxnTypeAttributes(entity.txntype)
        showCapture = var.get('faas_transaction_process_as_capture');
        if (showCapture) 
            showCapture = showCapture.toLowerCase().matches('1|y|yes|t|true');
        else 
            showCapture = false;
        afterInit();
    }
    
    void afterLookupFaas(){}
    
    def getLookupFaas(){
        return InvokerUtil.lookupOpener('faas:lookup', [
            onselect: {
                if (it.state == 'CANCELLED')
                    throw new Exception('FAAS has already been cancelled.');
                if (it.state != 'CURRENT')
                    throw new Exception('Cannot process record. The FAAS is not yet current.')
                    
                 entity.faas = it;
                 afterLookupFaas();
            },
            
            enempty :{ entity.faas = null; }
        ])
    }
    
    
    def process(){
        entity.attributes = listHandler.selectedValue;
        def faas = svc.initOnlineTransaction(entity);
        if (entity.datacapture == true)
            return InvokerUtil.lookupOpener('faas:capture:open', [entity:faas]);
        return InvokerUtil.lookupOpener('faas:open', [entity:faas]);
    }
    
    
    def listHandler = [
        fetchList : { return attributes },
        isMultiSelect: { true },
    ] as BasicListModel;
}