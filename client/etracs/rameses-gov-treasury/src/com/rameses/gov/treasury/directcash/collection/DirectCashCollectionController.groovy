package com.rameses.gov.treasury.batchcapture.batch; 

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
        
public class DirectCashCollectionController
{

    @Binding 
    def binding;    

    @Service("DirectCashCollectionService")
    def svc;

    
    def MODE_CREATE = 'create';
    def MODE_READ   = 'read';
    def MODE_EDIT   = 'edit';
    
    def mode;
    def entity;
    def selectedItem;
    
    String getTitle(){
        return 'Direct To Cash (' + entity.state + ')'
    }

    
    void init(){
        entity = [
            objid  : 'DC' + new java.rmi.server.UID(),
            state  : 'DRAFT',
            amount : 0.0,
            issuecashreceipt: 0, 
            items  : [],
        ];
        
        mode = MODE_CREATE;
    }
    
    
    void open(){
        entity = svc.open(entity)
        mode = MODE_READ;
    }
    
    
    void save(){
        if(!entity.items)    
            throw new Exception("Please include at least one item");
        if(entity.amount<=0)
            throw new Exception("Amount must not be zero");
        
        throw new Exception("Please include at least one item");
        if (mode == MODE_CREATE)
            svc.create(entity);
        else
            svc.update(entity);
        mode = MODE_READ;
    }
    
    
    void edit(){
        mode = MODE_EDIT;
    }
    
    def delete(){
        if (MsgBox.confirm('Delete record?')){
            svc.removeEntity(entity);
            return '_close';
        }
        return null;
    }
    
    void approve(){
        if (MsgBox.confirm('Approve record?')){
            svc.doApprove(entity);
            entity.state = 'APPROVED';
        }
    }
    
    
    def listHandler = [
            
        createItem : { return [parentid:entity.objid] },
        
        validate   : { li -> 
            def item = li.item;
            if ( ! item.item )
                throw new Exception('Account is required.');
            if (item.amount == null || item.amount <= 0.0)
                throw new Exception('Amount is required.');
        },
                
        onAddItem  : { item ->
            item.objid = 'DCI' + new java.rmi.server.UID();
            entity.items << item;
        },
                
        onRemoveItem : { item ->
            if (MsgBox.confirm('Delete selected item?')){
                svc.removeItem(item);
                entity.items.remove(item);
                calcAmount();
                return true;
            }
            return false;
        },
                
        onCommitItem : {
            calcAmount();
        },
                
        fetchList : { 
            return entity.items;
        },
        
    ] as EditorListModel;
    
    
    void calcAmount(){
        entity.amount = entity.items.amount.sum();
        if (entity.amount == null)
            entity.amount = 0.0;
        binding.refresh('entity.amount');
    }
}