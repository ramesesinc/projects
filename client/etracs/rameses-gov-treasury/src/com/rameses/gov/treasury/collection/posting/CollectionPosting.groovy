package com.rameses.gov.treasury.collection.posting

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

class CollectionPostingController
{
    @Binding
    def binding;
    
    @Service('CollectionPostingService')
    def svc;
    
    def chartTypes = ['NGAS', 'SRE'];
    
    def MODE_INIT   = 'init';
    def MODE_CREATE = 'create';
    def MODE_READ   = 'read';
    
    def entity;
    def mode;
            
    def totalDeposit = 0.0;
    def totalRemittance = 0.0;
    
    public String getTitle(){
        def t = 'Collection Posting' 
        if (entity.type)
             t += ' (' + entity.type + ')'
        return t
    }
            
    def init() {
        entity = [:];
        mode = MODE_INIT;
        return 'init';
    }
    
    void open(){
        entity = svc.open(entity);
        updateAmount();
        mode = MODE_READ;
    }
    
    
    def next(){
        entity.putAll(svc.init(entity));
        mode = MODE_CREATE;
        depositListHandler.reload();
        remittanceListHandler.reload();
        updateAmount();
        return 'default';
    }
    
    def back(){
        mode = MODE_INIT;
        return 'init';
    }
    
    
    void post(){
        if (MsgBox.confirm('Post collection?')){
            svc.post(entity);
            mode = MODE_READ;
        }
    }
    
    
    
    
    
    def selectedDeposit;
    def selectedRemittance;
    
    
    def depositListHandler = [
            getRows   : { return 50 },
            fetchList : { return entity.deposits },
            onColumnUpdate : { item, colName -> 
                updateAmount();
            },
    ] as EditorListModel;
            
            
    def remittanceListHandler = [
            getRows   : { return 50 },
            fetchList : { return entity.migratedremittances },
            onColumnUpdate : { item, colName -> 
                updateAmount();
            },
    ] as EditorListModel;
                    

    void updateAmount(){
        totalDeposit = 0.0;
        totalRemittance = 0.0;
        
        def deposits = entity.deposits.findAll{it.included == true}
        if (deposits)
            totalDeposit = deposits.amount.sum();
        
        def remittances = entity.migratedremittances .findAll{it.included == true}
        if (remittances)
            totalRemittance = remittances.amount.sum();
                
        entity.amount = totalDeposit + totalRemittance;
        
        binding?.refresh('entity.amount|totalDeposit|totalRemittance');
    }

}