package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class StructureModel extends MasterModel
{
    def selectedItem;
    
    @Service('PersistenceService')
    def persistence; 
    
    @Service('StructureService')
    def svc 
    
    public void afterCreate(){
        super.afterCreate();
        entity.showinfaas = false;
        entity.materials = [];
    }
            
    def lookupMaterial = InvokerUtil.lookupOpener("material:lookup",[:]);
            
    def listHandler = [
        getRows    : { return entity.materials.size() + 20 },
        fetchList    : { return entity.materials },
        createItem   : { return  [ structure:[objid:entity.objid], display:0, isnew:true, idx:0] },
        validate     : { li -> 
            def item = li.item 
            validateItem( item ) 
        
            item.structure_objid = entity.objid
            item.material_objid = item.material.objid 
            item.material_code = item.material.code
            item.material_name = item.material.name 
        },
        onAddItem    : { item -> 
            entity.materials.add( item );
        },
        onRemoveItem : { item -> 
            if( MsgBox.confirm("Delete selected item?") ) {
                removeItem(item)
                entity.materials.remove( item );
                return true;
            }
            return false;
        },
    ] as EditorListModel;
            
            
    void validateItem( item ){
        if( item.isnew && entity.materials.find{it.material_objid == item.material.objid} ){
            def msg = 'Duplicate Material is not allowed.'
            MsgBox.err(msg)
            throw new Exception(msg)
        }
    }
   
    void removeItem(item){
        def st = [_schemaname:'structurematerial']
        st.putAll(item);
        persistence.removeEntity(st);
    }
    
}