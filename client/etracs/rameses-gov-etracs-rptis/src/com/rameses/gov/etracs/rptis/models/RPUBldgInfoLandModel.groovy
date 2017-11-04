package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 

class RPUBldgInfoLandModel extends SubPageModel
{
    @Service('BldgRPUService')
    def bldgrpuSvc;
    
    def rpuSvc;
    def selectedItem;
    def items;
    
    
    void init(){
        items = bldgrpuSvc.getBldgLands([objid:entity.rpu.objid])
    }
    
    void afterModeChanged(){
        binding?.refresh()
    }
    
    
    def listHandler = [
        createItem  : { return [
                objid : 'BL' + new java.rmi.server.UID(),
                rpu   : [objid:entity.rpu.objid],
        ]},
    
        onRemoveItem  : {item ->
            if (MsgBox.confirm('Remove selected item?')){
                bldgrpuSvc.deleteBldgLand(item)
                items.remove(item);
                return true;
            }
            return false;
        },
        
        onAddItem : {item ->
            bldgrpuSvc.saveBldgLand(item)
            items << item;
        },
       
        validate    : {li ->
            def item = li.item;
            if (!item.landfaas) 
                throw new Exception('TD No. is required.')
            if (items.find{it.landfaas.objid == item.landfaas.objid})
                throw new Exception('Duplicate TD No. is not allowed.')
            if (entity.rp.pin == item.landfaas.fullpin)
                throw new Exception('The same land is not allowed.')
                
            item.landrpumaster = [objid:item.landfaas.rpumasterid]
        },
        
        fetchList   : { items },
    ] as EditorListModel
    
    
    def getLookupBldgLand(){
        return Inv.lookupOpener('faas:lookup', [
                rputype   : 'land', 
                
                onselect  : {
                    if (!it.state.matches('CURRENT|INTERIM')) throw new Exception('Only interim or current FAAS is allowed.')
                    selectedItem.landfaas = it;
                },
                
                onempty  : {
                    selectedItem.landfaas = null;
                }
        ])
    }
       
}    
