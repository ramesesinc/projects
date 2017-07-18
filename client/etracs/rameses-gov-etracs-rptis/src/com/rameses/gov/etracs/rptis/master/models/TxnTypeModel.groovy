package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;


class TxnTypeModel extends MasterModel
{
    @Service('FAASTxnTypeService')
    def svc 
    
    boolean createAllowed = false;
    boolean deleteAllowed  = false;
    boolean allowApprove = false;
    boolean allowDisapprove = false;
            
    def selectedItem;
        
    def listHandler = [
        fetchList : { return entity.attributes },
                
        createItem : { return [
                txntype_objid  : entity.objid,
                objid    : entity.objid,
                idx      : (entity.attributes ? entity.attributes.idx.max() + 1 : 1),
            ]},
                
        onAddItem : {item ->
            entity.attributes << item;
        },
                
        onRemoveItem : {item ->
            if (MsgBox.confirm('Delete selected item?')){
                entity.attributes.remove(item);
                return true;
            }
            return false;
        },
                
        validate  : {li ->
            def item = li.item;
            
            if (!item.attr) throw new Exception('Attribute is required.');
            if (item.idx == null) throw new Exception('Order is required.');
            
            def dup = entity.attributes.find{it.attribute == item.attr.attribute};
            if (dup) throw new Exception('Duplicate attribute is not allowed');
            
            item.attribute = item.attr.attribute; 
            
        },
                
    ] as EditorListModel
            
            
            
    void addAttribute(){
        def attr = MsgBox.prompt('New Attribute');
        if (!attr)
            return;
        svc.createAttributeType(attr);
    }
    
}