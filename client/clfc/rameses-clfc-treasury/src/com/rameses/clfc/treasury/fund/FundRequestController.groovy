package com.rameses.clfc.treasury.fund;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*
import java.rmi.server.UID;

class FundRequestController 
{
    @Service('FundRequestService') 
    def service; 
    
    @Binding
    def binding; 
    
    @Caller
    def caller;
    
    def mode = 'read'; 
    def entity = [:]; 
    def txntypes = ['NEW', 'RELOAD']; 

    void create() {
        mode = 'create';
        entity = [
            objid: 'FREQ'+new UID(), 
            data: [
                collectors: [],
                threshold: 500
            ]                      
        ]; 
    }
    
    void open() {
        mode = 'read'; 
        entity = service.open([objid: (entity.refid? entity.refid: entity.objid)]); 
    }
    
    def getData() {
        return entity.data;
    }
    
    
    def collectorLookup = Inv.lookupOpener('collector:lookup', [
        onselect:{o-> 
            o.name = (o.lastname + ', ' + o.firstname + (o.middlename? ' '+o.middlename: ''));
            data.collectors.each{c-> 
                if(c.collector?.objid == o.objid) { 
                    throw new Exception("Collector '"+ o.name + "' already exist in the list"); 
                }
            }               
            def item = [
                objid: 'FD' + new UID(), 
                threshold: data.threshold, 
                collector: o 
            ];
            data.collectors.add(item); 
            collectorListHandler.reload(); 
            binding.refresh('totalThreshold'); 
        } 
    ]);
    
    def selectedItem;
    def collectorListHandler = [
        fetchList:{params-> 
            return data.collectors; 
        },
        onCommitItem: {
            binding.refresh('totalThreshold'); 
        } 
    ] as EditorListModel;
    
    void removeSelectedItem() {
        if(selectedItem == null) return;
        
        data.collectors.remove(selectedItem); 
        collectorListHandler.reload(); 
        binding.refresh('totalThreshold'); 
    }
    
    def getTotalThreshold() {
        def total = 0.0;
        data.collectors.each{
            total += (it.threshold? it.threshold: 0.0);
        }
        return total; 
    }
    
    def close() {
        return '_close'; 
    }
    
    def cancelCreate() {
        return '_close'; 
    }    
    
    void saveCreate() { 
        if (!(entity.amount >= totalThreshold)) 
            throw new Exception('The total treshold amount has exceeded the request fund amount');
        
        data.collectors.each{
            if(!it.threshold) {
                def collector = it.collector;
                throw new Exception('Please specify threshold for collector ' + collector.lastname + ', ' + collector.firstname);
            } 
        }
        
        if (!MsgBox.confirm('You are about to submit this transaction. Continue?')) return;
        
        service.create(entity);
        mode = 'read'; 
        MsgBox.alert('Request successfully submitted');
    } 
    
    def approve() {
        if (!MsgBox.confirm('You are about to approve this request. Continue?')) return;
        
        def handler = { remarks-> 
            try {
                entity = service.approve([objid: entity.objid, remarks: remarks]);
                EventQueue.invokeLater({ caller?.reload(); }); 
                binding.refresh(); 
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        } 
        return Inv.lookupOpener('remarks:create', [title: 'Remarks for Approval', handler: handler]); 
    }
    
    def reject() {
        if (!MsgBox.confirm('You are about to reject this request. Continue?')) return;
        
        def handler = { remarks-> 
            try {
                entity = service.reject([objid: entity.objid, remarks: remarks]); 
                EventQueue.invokeLater({ caller?.reload(); }); 
                binding.refresh(); 
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        } 
        return Inv.lookupOpener('remarks:create', [title: 'Remarks for Rejection', handler: handler]);
    } 
}
