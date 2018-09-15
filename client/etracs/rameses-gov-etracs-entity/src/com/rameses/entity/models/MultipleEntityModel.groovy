package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.rmi.server.*;

class MultipleEntityModel extends CrudFormModel {
    
    @SubWindow 
    def window; 
    
    def getLookupMember() {
        return InvokerUtil.lookupOpener('entity:lookup', ['query.type': 'INDIVIDUAL,JURIDICAL']); 
    }             

    void afterCreate() {
        entity.members = [];
        if ( listHandler ) {
            listHandler.reload(); 
        } 
        if ( window ) 
            window.title = getTitle(); 
    }
    
    void afterSave() {
        if ( window ) { 
            window.update(); 
        }
    }
    
    String getTitle() {
        return (mode=='create'? 'New Multiple Entity': super.getTitle()); 
    }
    
    def selectedEntity;    
            
    def listHandler = [
        fetchList:{o-> 
            if (!entity) return null; 
            if (!entity.members) entity.members = [];
            return entity.members; 
        },

        onColumnUpdate: {item,colname-> 
            if (colname == 'member') { 
                def o = entity.members.find{ it.member.objid == item.member.objid } 
                if (o) throw new Exception('This member already exist in the list. Please select another one.'); 
            } 
        },

        onCommitItem: {item-> 
            rebuildNames();
            binding.refresh('entity.name');  
        }, 

        onAddItem: {item-> 
            item.objid = 'MEM'+new UID();
            item.entityid = entity.objid; 
            entity.members.add(item); 
            rebuildNames();
            binding.refresh('entity.name');
        }, 

        onRemoveItem: {item-> 
            if (!MsgBox.confirm('Are you sure you want to remove this item?')) return false;

            entity.members.remove(item); 
            rebuildNames(); 
            binding.refresh('entity.name'); 
            return true;
        }
    ] as EditorListModel;             

    void rebuildNames() {
        def buffer = new StringBuffer();
        def indexno = 0;
        entity.members.each{
            if (buffer.length() > 0) buffer.append(' ');                         
            if (it.prefix) buffer.append(it.prefix + ' ');

            buffer.append(it.member.name); 

            if (it.suffix) buffer.append(' ' + it.suffix); 

            indexno++;
            it.itemno = indexno;
        }
        def oldfullname = entity.fullname; 
        entity.fullname = buffer.toString();
        if (entity.name == oldfullname) entity.name = entity.fullname;  
    } 


    void copyFullName(){
        entity.name = entity.fullname
        rebuildNames();
        binding.refresh('entity.name')
    }
    
    def viewEntity() {
        if( !selectedEntity ) throw new Exception("Please select an member entity");
        String s = "entity" + selectedEntity.member.type.toLowerCase() + ":open";
        def op = Inv.lookupOpener( s, [entity:selectedEntity.member ]);
        op.target = "popup";
        return op;
    }
}
