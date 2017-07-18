package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPUInfoRestrictionModel extends SubPageModel
{
    @Service('DateService')
    def dtSvc;
    
    @Service('QueryService')
    def querySvc
    
    def selectedItem;
    
    def states = ['ACTIVE', 'UNRESTRICTED']
    
    void init(){
        if (!entity.restrictions)
            entity.restrictions = []
    }
    
    void afterModeChanged(){
        listHandler.reload();
    }

    
    def listHandler = [
        createItem  : { [parent:entity, state:'ACTIVE', receipt:[:]]},
        
        fetchList   : { entity.restrictions },
        
        validate : {li ->
            def item = li.item 
            if (item.state == 'UNRESTRICTED'){
                RPTUtil.required('Receipt No.', item.receipt.receiptno)
                RPTUtil.required('Receipt Date', item.receipt.receiptdate)
                
                def serverdate = dtSvc.getServerDate()
                def txndate = dtSvc.parse('yyyy-MM-dd', item.txndate)
                def receiptdate = dtSvc.parse('yyyy-MM-dd', item.receipt.receiptdate)
                
                if (receiptdate > serverdate) throw new Exception('Receipt Date must be less than or equal to current date.')
                if (receiptdate < txndate) throw new Exception('Receipt Date must be greater or equal than Txn Date.')
            }
        },
        
        onAddItem   : { item ->
            if (!entity.restrictions) 
                entity.restrictions = []
            entity.restrictions << item 
        },
        
        onRemoveItem : {item ->
            if (MsgBox.confirm('Delete selected restriction?')){
                def key = 'restrictions::deleted'
                if (!entity[key])
                    entity[key] = []
                entity.restrictions.remove(item)
                entity[key] << item;
                return true;
            }
            return false;
        }
        
    ] as EditorListModel 
    
    
    def getRestrictiontypes(){
        return querySvc.getList( [_schemaname:'faas_restriction_type',where:['1=1'], orderBy:'idx'])
    }
}    
    