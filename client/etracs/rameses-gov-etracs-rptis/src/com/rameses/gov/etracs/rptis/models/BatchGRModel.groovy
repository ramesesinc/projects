package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class BatchGRModel 
{
    @Binding
    def binding;
    
    @Service('BatchGRService')
    def svc; 
    
    @Service('FAASLookupService')
    def faasLookupSvc; 
    
    def entity;
    def items;
    def msg;
    def processing = false;
    def cancelled = false;
    def selectedItem;
    def counts = [count:0, revised:0, approved:0, error:0 ];
    
    
    String title = 'General Revision';
    
    void open() {
        entity.putAll(svc.open(entity));
        refreshCounts();
        loadItems();
        if (entity.state == 'DRAFT') {
            updateItemStatus();
        }
    }
    
    def delete() {
        if (MsgBox.confirm('Delete batch record?')) {
            svc.delete(entity);
            return '_close';
        }
    }
    
    void refresh() {
        open();
    }
    
    void revise() {
        loadItemsForRevision();
        if (itemsforrevision && MsgBox.confirm('Revise all FAAS?')) {
            cancelled = false;
            processing = true;
            new Thread(task).start();
        }
    }
    
    void cancel() {
        cancelled = true;
        processing = false;
    }
    
    def getShowDelete() {
        if (processing) return false;
        if (entity.state != 'FORREVISION') return false;
        if (items && items.find{it.newfaasid != null}) return false;
        return true;
    }
    
    def getShowRevise() {
        if (processing) return false;
        if (entity.state != 'FORREVISION') return false;
        if (items && items.find{it.state == 'FORREVISION'} == null) return false;
        return true;
    }
    
    void loadItems() {
        items = svc.getItems(entity);
        items.each{ 
            updateDisplayPin(it)
        }
        listHandler?.reload();
    }
    
    void updateDisplayPin(item) {
        if (item.rputype == 'land') {
            item._fullpin = item.fullpin;
            item._newfullpin = (item.newfaasid ? item.newfaas.fullpin : '');
        } else {
            item._fullpin = '     * ' + item.fullpin;
            item._newfullpin = (item.newfaasid ? ('     * ' + item.newfaas.fullpin) : '');
        }
    }
    
    void refreshCounts() {
        counts = svc.getCounts(entity);
        binding?.refresh('counts.*');
    }
    
    
    def openItem() {
        if (selectedItem.state == 'ERROR') {
            return Inv.lookupOpener('batchgr_error:open', [entity: selectedItem]);
        } else if (selectedItem.state == 'REVISED') {
            return openRevisedFaas();
        }
    }
    
    def listHandler = [
        getRows : { items.size()},
        fetchList : { items },
        onOpenItem : {item, colname -> openItem()},
    ] as BasicListModel
 
   
    
    /*---------------------------------------------------------
     * UPDATE ITEM STATUS TASK 
    ---------------------------------------------------------*/
    /* The batch items status are not fully sync. 
     * Sync the state and newfaasid for items that 
     * are already revised 
     */
    void updateItemStatus(){
        processing = true;
        new Thread(updateItemTask).start();
    }
    
    def updateItemTask = [
        run: {
            showStatus('Updating item status. Pleas wait...');
            items.each{
                showStatus('Updating item status: ' + it.tdno);
            }
            entity.state = 'FORREVISION';
            svc.update(entity);
            
            loadItems();
            
            onComplete();
        }
    ] as Runnable;
    
    
    
    /*---------------------------------------------------------
     * BATCH REVISION TASK 
    ---------------------------------------------------------*/
    def itemsforrevision;
    
    void loadItemsForRevision() {
        itemsforrevision = items.findAll{it.state.matches('FORREVISION|ERROR')};
    }
    
    def showStatus = {
        msg = it;
        binding?.refresh('msg|itemActions');
    }
    
    def onRevise = {info ->
        updateDisplayPin(info.item);
        def item = items.find{it.objid == info.item.objid}
        item.putAll(info.item);
        entity.putAll(info.entity);     
        listHandler.refreshItem(item);
        refreshCounts();
        binding.refresh('formActions|itemActions');
    }    
    
    def onComplete = {
        msg = null;
        processing = false;
        refreshCounts();
        binding.refresh();
    }
    
    def task = [
        run : {
            while(!cancelled && itemsforrevision) {
                def item = itemsforrevision.remove(0);
                showStatus('Revising TD No. ' + item.tdno);
                onRevise(svc.revise(item));
                try{
                    Thread.sleep(250);
                }catch(e) {
                    //
                }
            }
            itemsforrevision = null;
            onComplete();
        }
    ] as Runnable
    
    void reviseItem() {
        if (selectedItem && !selectedItem.newfaasid && MsgBox.confirm('Revise selected item?')) {
            onRevise(svc.revise(selectedItem));
        }
    }
    
    
    
    def reloadItem = {
        def item = svc.getItem(selectedItem);
        if (item) {
            selectedItem.putAll(item);
            listHandler.refreshItem(selectedItem);
            entity.putAll(svc.open(entity));
            refreshCounts();
            binding.refresh('formActions|itemActions');
        }
    }
    
    def openRevisedFaas() {
        def faas = [objid: selectedItem.newfaasid]
        return Inv.lookupOpener('faas:capture:open', [
                entity: faas, 
                afterApprove: reloadItem,
                afterUpdate: reloadItem,
                afterDelete: reloadItem,
        ]);
    }
    
    void excludeFaas() {
        if (MsgBox.confirm('Exclude selected item from revision?')) {
            svc.excludeItem(selectedItem);
            loadItems();
            refreshCounts();
        }
    }
    
    def modifyPin() {
        def faases = faasLookupSvc.lookupFaas([objid: selectedItem.newfaasid]);
        if (!faases) return;
        
        def landfaas = faases[0];
        
        return Inv.lookupOpener('faas:modifypin', [
                faas: landfaas,
                onUpdate: { refresh(); },
        ]);
    }
    
}