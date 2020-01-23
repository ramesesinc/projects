package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class BatchGRItemModel 
{
    @Binding
    def binding;
    
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
    String entityName = 'batchgr';
    
    void init() {
        refreshCounts();
        loadItems();
    }
    
    void refresh() {
        init();
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
        
    def getShowRevise() {
        if (processing) return false;
        if (entity.state != 'DRAFT') return false;
        if (!entity.taskstate.matches('provtaxmapper|taxmapper')) return false;
        if (entity.assignee?.objid != OsirisContext.env.USERID) return false;
        if (items && items.find{it.state == 'FORREVISION'} == null) return false;
        return true;
    }

    def getShowExclude() {
        if (processing) return false;
        if (entity.state != 'DRAFT') return false;
        if (!entity.taskstate.matches('provtaxmapper|taxmapper|appraiser|provappraiser')) return false;
        if (entity.assignee?.objid != OsirisContext.env.USERID) return false;
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
     * BATCH REVISION TASK 
    ---------------------------------------------------------*/
    def itemsforrevision;
    
    void loadItemsForRevision() {
        itemsforrevision = items.findAll{it.state.matches('FORREVISION|ERROR')};
    }
    
    def showStatus = {
        msg = it;
        binding?.refresh('msg');
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
            def size = itemsforrevision.size();
            while(!cancelled && size > 0) {
                def item = itemsforrevision.remove(0);
                showStatus('Revising TD No. ' + item.tdno);
                onRevise(svc.revise(item));
                size = itemsforrevision.size();
                try{
                    Thread.sleep(100);
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

    def openFaas() {
    	def faas = [objid: selectedItem.objid];
    	return Inv.lookupOpener('faas:capture:open', [entity: faas]);
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
                entity: landfaas,
                onUpdate: { refresh(); },
        ]);
    }
    
}