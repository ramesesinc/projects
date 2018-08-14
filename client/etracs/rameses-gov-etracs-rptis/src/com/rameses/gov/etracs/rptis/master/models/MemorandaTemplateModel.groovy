package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class MemorandaTemplateModel extends CrudFormModel
{
    def types = ['string', 'integer', 'decimal', 'date'];
        
    def fieldListHandler = [
        createItem : { 
            return  [:];
        },
        fetchList : { 
            if (!entity.fields){
                entity.fields = [];
            }
            entity.fields ;
        },
        onAddItem : {
            entity.fields << it;
        },
        validate: {
            if (!it.item.type){
                throw new Exception('Data type must be specified.');
            }
        },
        onRemoveItem : {
            if (MsgBox.confirm('Remove selected item?')){
                entity.fields.remove(it);
                return true;
            }
            return false;
        }
    ] as EditorListModel
    
    
    public void afterCreate(){
        entity.fields = [:];
        fieldListHandler.reload();
    }
    
}