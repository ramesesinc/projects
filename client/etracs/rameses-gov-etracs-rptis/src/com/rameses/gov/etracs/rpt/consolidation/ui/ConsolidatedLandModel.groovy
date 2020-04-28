package com.rameses.gov.etracs.rpt.consolidation.ui;
        

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class ConsolidatedLandModel 
{
    @Binding 
    def binding;
    
    @Service('RealPropertyService')
    def rpSvc;
    
    def svc;
    
    def entity;
    def selectedItem;
    def lands;
    
    def mode;
    def MODE_READ = 'read';
    def MODE_EDIT = 'edit';
    
    String title = 'Consolidated Lands';
    
    
    void refresh(){
        init();
        landListHandler?.load();
    }
    
    void init(){
        lands = svc.getConsolidatedLands(entity.objid);
        mode = MODE_READ;
    }
    
    void edit(){
        mode = MODE_EDIT;
    }
    
    void save(){
        mode = MODE_READ;
    }
    
    
    def getLookupFaas(){
        return InvokerUtil.lookupOpener('faas:lookup', [
            onselect : { 
                if ( it.rputype != 'land' )
                    throw new Exception('Only land property is allowed.');
                if (it.state != 'CURRENT' )
                    throw new Exception('Only current record is allowed.');
                if (it.barangayid != entity.rp.barangayid )
                    throw new Exception('Selected land is invalid. It must be part of Barangay ' + entity.rp.barangay + '.')
                    
                selectedItem.faas = it;
                selectedItem.rpuid = it.rpuid;
                selectedItem.rpid  = it.realpropertyid;
                selectedItem.landfaasid = it.objid;
                        
                selectedItem.rpu = [
                    fullpin      : it.fullpin,
                    totalareasqm : it.totalareasqm,
                    totalareaha  : it.totalareaha,
                ];
            },
                
            onempty : {
                selectedItem.faas = null;
                selectedItem.rpu  = null;
                selectedItem.landfaasid = null;
            }
        ])
    }
    
    
    def landListHandler = [
        getRows : { return 50 },
            
        createItem : { return [
            consolidationid : entity.objid,
        ]},
                
        fetchList : { return lands },
                
        validate : {li ->
            def item = li.item;
            svc.validateConsolidatedLand(item)
        },
                
        onAddItem : { item ->
            item.objid = 'CI' + new java.rmi.server.UID();
            item.txnno = entity.txnno
            svc.saveConsolidatedLand(item);
            lands.add(item);
            
        },
                
        onRemoveItem : { item ->
            if (MsgBox.confirm('Delete selected item?')){
                svc.deleteConsolidatedLand(item);
                lands.remove(item);
                return true;
            }
            return false;
        }
    ] as EditorListModel
    
    
    
    
    
    def getTotalAreaHa(){
        if (lands)
            return lands.rpu.totalareaha.sum();
        return 0.0;
    }
    
    def getTotalAreaSqm(){
        if (lands)
            return lands.rpu.totalareasqm.sum();
        return 0.0;
    }
    
    def getCount(){
        return lands.size();
    }
    
    boolean getAllowEdit(){
        if (mode != 'read') return false;
        if (entity.state == 'APPROVED') return false;
        if (!entity.taskstate.matches('receiver|examiner|taxmapper|provtaxmapper')) return false;
        if (OsirisContext.env.USERID != entity.assignee.objid) return false;
        return true;
    }
}
