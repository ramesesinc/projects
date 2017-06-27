package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 

class RPUBldgInfoStructureModel extends SubPageModel
{
    def rpuSvc;
    
    def floor;
    def selectedStructure 
    
    def getLookupStructure(){
        return InvokerUtil.lookupOpener('structure:lookup', [
            onselect: {
                selectedStructure.structure = it;
            },
                
            onempty: {
                selectedStructure.structure = null;
            }
        ])
    }    
            
    def getLookupStructureMaterial(){
        return InvokerUtil.lookupOpener('structurematerial:lookup', [
            structureid : selectedStructure.structure.objid,
                
            onselect: {
                selectedStructure.material = it;
            },
                
            onempty: {
                selectedStructure.material = null;
            }
        ])
    }
        
    
    def structureListHandler = [
        createItem : { 
            return [bldgrpuid:entity.rpu.objid, floor:floor] 
        },
        
        fetchList : { 
            return entity.rpu.structures.findAll{it.floor == floor} 
        },
        
        validate  : {li ->
            if (li.item.floor == null)
                throw new Exception('Floor is required.');
            if (!li.item.structure)
                throw new Exception('Structure is required.');
                
            //check duplicate floor/structure
            if (! entity.rpu.structures) entity.rpu.structures = [];
            if (entity.rpu.structures.size() > 1){
                def dups = entity.rpu.structures.findAll{
                    it.floor == li.item.floor && 
                    it.structure.objid == li.item.structure.objid &&
                    it.material?.objid == li.item.material?.objid
                }
                if (li.item.objid == null && dups )
                    throw new Exception('Duplicate structure for Floor ' + li.item.floor + ' is not allowed.');
            }
            
        },
        
        onAddItem : {item ->
            item.objid = 'BS' + new java.rmi.server.UID()
            entity.rpu.structures.add(item);
        },
        
        onRemoveItem :{ item ->
            if (MsgBox.confirm('Delete item?')){
                if (!entity.rpu._structures) 
                    entity.rpu._structures = [];
                entity.rpu._structures.add(item);
                entity.rpu.structures.remove(item);
                structureListHandler.reload();
                return true;
            }
            return false;
        },
        
        
    ] as EditorListModel
    
    
    @PropertyChangeListener()
    def listener = [
        'entity.rpu.floorcount' : {
            def deletedstructures = entity.rpu.structures.findAll{it.floor > entity.rpu.floorcount} 
            if (deletedstructures){
                entity.rpu.structures.removeAll(deletedstructures)
                entity.rpu._structures.addAll(deletedstructures);
            }
        }
    ]
    
    def getFloors(){
        if (entity.rpu.floorcount == 0)
            entity.rpu.floorcount = 1;
        def count = entity.rpu.floorcount 
        return 1..count.each{};
    }
   
}    
