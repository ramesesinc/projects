package com.rameses.gov.etracs.rpt.ledger.ui;


import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*
import com.rameses.gov.etracs.rptis.util.*;


public class RPTLedgerController 
{
    @FormTitle
    @FormId
    def formTitle
    
    @Binding
    def binding
    
    @Service("RPTLedgerService")
    def svc;
    
    @Service("FAASRestrictionService")
    def restrictionSvc;
    
    @Service('RPTBillingService')
    def billSvc;
    
    def entity = [state:'PENDING'];
    def mode;

    def STATE_PENDING = 'PENDING'
    def STATE_APPROVED = 'APPROVED'

    def MODE_READ   = 'read'
    def MODE_EDIT   = 'edit';
    
    String entityName = 'rptledger'
    String type = 'online';
    
    String getTitle(){
        return 'Realty Tax Ledger (' + entity.state + ')'
    }
    
    void open(){
        mode    = MODE_READ
        entity.putAll(svc.loadLedger(entity));
        calcTotals();
        reloadItems();
        binding?.refresh();
    }
    
    void cancel(){
        open()
        reloadItems();
    }

    void reloadItems(){
        historyListHandler?.reload()
        ledgerItemsHandler?.reload()
        paymentListHandler?.reload()
    }
    
    void edit(){
        mode = MODE_EDIT
    }

    def save(){
        mode = MODE_READ
        return 'default'
    }
    
    def delete(){
        if (MsgBox.confirm('Delete ledger?')){
            svc.deleteLedger(entity);
            return '_close';
        }
    }
    
    void approve(){
        if (MsgBox.confirm('Approve ledger?')) {
            entity.putAll(svc.approveLedger(entity));
            open();
        }
    }



    /*--------------------------------------------------------------
    *
    * FAAS HISTORY  SUPPORT 
    *
    --------------------------------------------------------------*/
    def selectedItem 

    def historyListHandler = [
        fetchList  : { return entity.faases},

    ] as BasicListModel


    def onaddHandler = { item ->
        svc.saveLedgerFaas( item )
        entity.faases.add( item )
        historyListHandler.reload() 
    }

    def addFaas() {
        return InvokerUtil.lookupOpener('rptledgerfaas:create',[svc:svc, onaddHandler:onaddHandler, ledger:entity] )
    }
    
    def onaddNewRevisionHandler = { item ->
        svc.saveNewRevisionLedgerFaas( item )
        open();
    }
    
    def addNewLedgerFaas(){
        return InvokerUtil.lookupOpener('rptledgerfaas:createnewrevision',[svc:svc, onaddHandler:onaddNewRevisionHandler, ledger:entity] )
    }
    
    def onupdateHandler = { item ->
        svc.saveLedgerFaas( item )
        binding.refresh('selectedItem')
    }
    
    def editFaas() {
        if( ! selectedItem ) return 
        if( RPTUtil.isTrue(selectedItem.systemcreated) ) {
            throw new Exception('System posted item is not editable.')
        }
        return InvokerUtil.lookupOpener('rptledgerfaas:edit',[svc:svc, onupdateHandler:onupdateHandler, ledger:entity, ledgerfaas:selectedItem])
    }
    
    void removeFaas() {
        if( ! selectedItem ) return 
        if( RPTUtil.isTrue(selectedItem.systemcreated) ) {
            throw new Exception( 'Cannot delete a system created entry.' )
        }
        if( MsgBox.confirm( 'Remove last item?' )) {
            svc.removeLedgerFaas( entity.faases.last() )
            entity.faases.remove( entity.faases.last())
            historyListHandler.reload()
        }
    }


    /*--------------------------------------------------------------
    *
    * LEDGER ITEMS  SUPPORT 
    *
    --------------------------------------------------------------*/
    def ledgerItems;

    def ledgerItemsHandler = [
        fetchList : { return entity._ledgerItems},
        onRemoveItem : {item -> 
            if (MsgBox.confirm('Delete selected item?')){
                svc.removeLedgerItem(item);
                entity._ledgerItems.remove(item);
                return true; 
            }
            return false;
        }
    ] as EditorListModel

        
    void deleteLedgerItem(){
        if (!selectedLedgerItem) return;
        if (selectedLedgerItem.taxdifference == false){
            throw new Exception('Only Tax Difference item can be deleted.');
        }
        if (MsgBox.confirm('Delete selected item?')){
            svc.removeLedgerItem(selectedLedgerItem);
            entity._ledgerItems.remove(selectedLedgerItem);
            ledgerItemsHandler.reload();
        }
    }
    

    /*--------------------------------------------------------------
    *
    * CREDIT LIST  SUPPORT 
    *
    --------------------------------------------------------------*/
    def selectedPayment

    def paymentListHandler = [
        fetchList : { return entity._credits }
    ] as BasicListModel

    def getLastyearqtrpaid(){
        if (entity.lastyearpaid == null || entity.lastqtrpaid == null)
            return '';
        if (entity.lastqtrpaid == 4 || entity.lastqtrpaid == 0)
            return entity.lastyearpaid;
        return entity.lastqtrpaid + "Q, " + entity.lastyearpaid;
    }
    
    def getBalance(){
        return entity._ledgerItems.amtdue.sum();
    }
    
    
    def onbill = {
        open();
        binding.refresh();
    }
    
    def fixLedgerFaas(){
        if (!selectedItem) 
            throw new Exception('Ledger FAAS to fix is required.')
            
        return InvokerUtil.lookupOpener('rptledger:fixledgerfaas', [
            entity : selectedItem,
                
            svc    : svc, 
                
            oncomplete : {
                if (it.toqtr == null) it.toqtr = 0
                selectedItem.putAll(it);
                ledgerItemsHandler.refreshSelectedItem();
                open();
                entity.lastyearpaid = it.lastyearpaid;
                entity.lastqtrpaid = it.lastqtrpaid;
                binding.refresh('.*')
            }
        ])
    }

    
    /*==================================================
    * LEDGER ITEM SUPPORT 
    ==================================================*/
    def selectedLedgerItem;
    
    def addLedgerItem(){
        return InvokerUtil.lookupOpener('rptledgeritem:create', [
            svc         : svc,
            entity      : entity, 
            faases      : entity.faases,
            
            onadd       : { open(); }
        ] )
    }
    
    def editLedgerItem(){
        return InvokerUtil.lookupOpener('rptledgeritem:edit', [
            svc         : svc,
            entity      : entity, 
            item        : selectedLedgerItem,
            faases      : entity.faases,
            
            onupdate : { open();}
        ] )
    }
       
    void repostLedgerItems(){
        if (MsgBox.confirm('Repost ledger items?')){
            svc.repostLedgerItems(entity);
            open();
        }
    }
    
    
    
    
    /*======================================================
    **
    * SUBLEDGER SUPPORT
    * 
    *=======================================================*/
       
    def selectedSubLedger;
    def totalSubledgerArea = 0.0;
    def totalSubledgerMV = 0.0;
    def totalSubledgerAV = 0.0;
    def subledgerCount = 0;
    
    def subledgerListHandler = [
        fetchList : { entity._subledgers }
    ] as BasicListModel
    
    
    def addSubLedger(){
        if (totalSubledgerArea >= entity.totalareaha)
            throw new Exception('Subledger is no longer allowed.\nMain Ledger area has totally been allocated.')
            
        return InvokerUtil.lookupOpener('rptsubledger:create', [
                ledger            : entity,
                totalSubledgerArea : totalSubledgerArea, 
                totalSubledgerMV   : totalSubledgerMV, 
                totalSubledgerAV   : totalSubledgerAV, 
                
                onadd  : {
                    entity._subledgers << it;
                    subledgerListHandler.load();
                    calcTotals();
                }
        ])
    }
    
    def openSubLedger(){
        return InvokerUtil.lookupOpener('rptsubledger:open', [
                ledger              : entity,
                totalSubledgerArea   : totalSubledgerArea, 
                totalSubledgerMV     : totalSubledgerMV, 
                totalSubledgerAV     : totalSubledgerAV, 
                entity              : svc.openLedger(selectedSubLedger),
                onupdate  : {
                    selectedSubLedger.putAll(it)
                    subledgerListHandler.load();
                    calcTotals();
                }
        ])
    }
    
    def deleteSubLedger(){
        if (MsgBox.confirm('Delete selected item?')){
            def item = entity._subledgers.find{it.objid == selectedSubLedger.objid}
            svc.deleteSubLedger(item);
            entity._subledgers.remove(item);
            subledgerListHandler.load();
            calcTotals();
        }
    }
    
    void calcTotals(){
        totalSubledgerArea = 0.0;
        totalSubledgerMV = 0.0;
        totalSubledgerAV = 0.0;
        subledgerCount = 0;
        
        if (entity._subledgers) {
            totalSubledgerArea = entity._subledgers.totalareaha.sum();
            totalSubledgerMV = entity._subledgers.totalmv.sum();
            totalSubledgerAV = entity._subledgers.totalav.sum();
            subledgerCount = entity._subledgers.size();
        }
        binding?.refresh('totalSubledger.*|subledgerCount');
    }
    
    def viewTaxDeclaration(){
        def inv = Inv.lookupOpener('td:report', [entity:[objid:selectedItem.faasid]])
        if (inv){
            inv.target="popu";
        }
        return inv;
    }
    
    
    

    /*--------------------------------------------------------------
    *
    * RESTRICTION SUPPORT 
    *
    --------------------------------------------------------------*/    
    def selectedRestriction;
    def _restrictions;
    
    def getRestrictions(){
        if (_restrictions == null){
            _restrictions = svc.getRestrictions([objid:entity.objid])
        }
        return _restrictions
    }
    
    def restrictionListHandler = [
        fetchList : { return restrictions }        
    ] as BasicListModel;
    
    def oncreate = {
        restrictions << it 
        restrictionListHandler.reload()
    }    
    def onupdate = {
        selectedRestriction.putAll(it)
        restrictionListHandler.refresh()
    }
    
    def getRestrictionParam(){
        def p = [oncreate:oncreate, onupdate:onupdate]
        p.parent = [objid:entity.faasid, tdno:entity.tdno, fullpin:entity.fullpin, owner:entity.taxpayer]
        p.ledger = [objid:entity.objid]
        return p
    }
    def addRestriction(){
        return Inv.lookupOpener('faas_restriction:create', restrictionParam)
    }
    
    def openRestriction(){
        def p = restrictionParam
        p.entity = selectedRestriction
        return Inv.lookupOpener('faas_restriction:open', p)
    }    
    
    void deleteRestriction(){
        if (!selectedRestriction) return;
        if (MsgBox.confirm('Delete selected restriction?')){
            restrictionSvc.removeRestriction(selectedRestriction);
            restrictions.remove(selectedRestriction);
            restrictionListHandler.reload();
        }
    }

    def getMessagelist(){
        def queryinfo = [];
        if (entity.restrictions){
            queryinfo << 'Ledger is currently under restriction';
        }
        
        if (entity.subledger){
            queryinfo << 'Sub-Ledger of PIN ' + entity.subledger.parent.fullpin + '.';
        }
        
        if (RPTUtil.isTrue(entity.undercompromise)){
            queryinfo << 'Ledger is under compromise agreement.'
        }
            
        if (queryinfo)
            return queryinfo;
        return null;
    }
}

