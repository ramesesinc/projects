package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPTLedgerModel extends CrudFormModel  
{
    @Service('RPTLedgerService')
    def svc;
    
    boolean showConfirm = false;

    @FormTitle
    @FormId
    public String getFormId(){
        return 'Realty Tax Ledger : ' + entity.tdno 
    }
    
    void approve(){
        if (MsgBox.confirm('Approve')){
            svc.approve(entity);
            reloadEntity();
            refreshSections();
        }
    }
    
    void refreshSections() {
        sections?.each {
            try { it.handle.refresh(); }catch(e){;}
        }
    }
    
    def reloadEntity(){
        super.reloadEntity();
        refreshSections();
    }
    
    /*--------------------------------------------------------------
    *
    * FAAS HISTORY  SUPPORT 
    *
    --------------------------------------------------------------*/
    def selectedItem 

    def onaddItem = { item ->
        addItem('faases', item);
        itemHandlers.faases.reload();
    }

    def addFaas() {
        return InvokerUtil.lookupOpener('rptledgerfaas:create',[onadd:onaddItem, ledger:entity] )
    }
    
    def onupdateItem = { item ->
        selectedItem.putAll(item);
        itemHandlers.faases.refreshSelectedItem();
    }
    
    def editFaas() {
        return InvokerUtil.lookupOpener('rptledgerfaas:edit',[onupdate:onupdateItem, ledger:entity, ledgerfaas:selectedItem])
    }
    
    void removeFaas() {
        if( MsgBox.confirm( 'Delete selected item?' )) {
            removeItem('faases', selectedItem);
            itemHandlers.faases.reload();
        }
    }
    
    def fixLedgerFaas(){
        if (!selectedItem) 
            throw new Exception('Selet FAAS to fix.')
            
        return InvokerUtil.lookupOpener('rptledger:fixledgerfaas', [
            entity : selectedItem,
            svc    : svc, 
            oncomplete : {
                if (it.toqtr == null) it.toqtr = 0
                selectedItem.putAll(it);
                itemHandlers.faases.reload();
                entity.lastyearpaid = it.lastyearpaid;
                entity.lastqtrpaid = it.lastqtrpaid;
                binding.refresh('.*')
            }
        ])
    }
    
    def onaddNewRevisionHandler = { item ->
        svc.saveNewRevisionLedgerFaas( item )
        reloadEntity();
    }
    
    def addNewLedgerFaas(){
        return InvokerUtil.lookupOpener('rptledgerfaas:createnewrevision',[svc:svc, onadd:onaddNewRevisionHandler, ledger:entity] )
    }
    
    def viewTaxDec(){
        def inv = Inv.lookupOpener('td:report', [entity:[objid:selectedItem.faasid]])
        if (inv){
            inv.target="popu";
        }
        return inv;
    }    
}