package com.rameses.clfc.treasury.loan.arliquidation

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LoanARLiquidationController extends CRUDController
{
    @Binding
    def binding;
    
    String serviceName = "LoanARLiquidationService";
    String entityName = "loanarliquidation";
    String role = 'ACCT_ASSISTANT';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    def selectedAr, prevar; //selectedLiquidation;
    //def prevar;//, prevliquidation;
    
    
    Map createEntity() {
        return [objid: 'LAL' + new UID(), txnstate: 'DRAFT'];
    }
    
    void afterCreate( data ) {
        binding?.refresh('totalar');
        //binding?.refresh('total.(ar|liquidation)');
        arHandler?.reload();
        //liquidationHandler?.reload();
    }
    
    void checkEditable( data ) {
        def state = data.txnstate;
        if (state=='DRAFT' || state=='SEND_BACK') {
            allowEdit = true;
        } else {
            allowEdit = false;
        }
        binding?.refresh('formActions');
    }
    
    void afterSave( data ) {
        checkEditable(data);
    }
    
    void afterOpen( data ) {
        checkEditable(data);
    }
    
    void afterEdit( data ) {
        prevar = [];
        def item, xitem, mitem;
        data.arlist.each{ o->
            item = [:];
            item.putAll(o);
            item.items = [];
            o.items.each{ p->
                xitem = [:];
                xitem.putAll(p);
                xitem.items = [];
                p.items.each{ i->
                    mitem = [:];
                    mitem.putAll(i);
                    xitem.items.add(mitem);
                }
                item.items.add(xitem);
            }
            prevar.add(item);
        }
        
        /*
        prevliquidation = [];
        data.liquidations.each{ o->
            item = [:];
            item.putAll(o);
            prevliquidation.add(item);
        }
        */
    }
    
    void afterCancel() {
        entity.arlist = [];
        entity.arlist.addAll(prevar);
        arHandler?.reload();
        
        /*
        entity.liquidations = [];
        entity.liquidations.addAll(prevliquidation);
        liquidationHandler?.reload();
        */
    }
    
    void setSelectedAr( selectedAr ) {
        this.selectedAr = selectedAr;
        itemsHandler?.reload();
    }
    
    def getTotalar() {
        if (!entity.arlist) return 0;
        
        def a = entity.arlist?.ar?.totalamount?.sum();
        return (a? a : 0);
    }
    
    def arHandler = [
        fetchList: { o->
            if (!entity.arlist) entity.arlist = [];
            return entity.arlist;
        }
    ] as BasicListModel
    
    def addAr() {
        def handler = { o->
            def i = entity.arlist.find{ it.ar.objid == o.objid }
            if (i) throw new Exception("AR already selected.");
            
            def item = [
                objid   : 'LLA' + new UID(),
                parentid: entity.objid,
                ar      : o
            ];
            if (o.check) {
                item.ar.checkno = o.check.no;
                item.ar.checkdate = o.check.date;
            }
            
            if (!entity._addedar) entity._addedar = [];
            entity._addedar.add(item);   
            
            def xlist = service.getArItems(o);
            xlist.each{ p->
                p.refid = p.objid;
                p.objid = 'LLAD' + new UID();
                p.parentid = item.objid;
                p.liquidationid = entity.objid
                p.totalbreakdown = 0;
                if (!entity._addedardetail) entity._addedardetail = [];
                entity._addedardetail.add(p);
                
                if (!item.items) item.items = [];
                item.items.add(p);
            }         
            entity.arlist.add(item);
            
            arHandler?.reload();
            binding?.refresh('totalar');
        }
        return Inv.lookupOpener('loanar:lookup', [onselect: handler, state: 'FOR_LIQUIDATION']);
    }
    
    void removeAr() {
        if (!MsgBox.confirm("You are about to remove this AR. Continue?")) return;
        
        if (!entity._removedar) entity._removedar = [];
        entity._removedar.add(selectedAr);
        
        selectedAr.items.each{ o->
            if (entity._addedardetail) entity._addedardetail.remove(o);
            
            if (!entity._removedardetail) entity._removedardetail = [];
            entity._removedardetail.add(o);
        }
        
        if (entity._addedar) entity._addedar.remove(selectedAr);
        entity.arlist.remove(selectedAr);
        arHandler?.reload();
        binding?.refresh('totalar');
    }
    
    def itemsHandler = [
        fetchList: { o->
            if (!selectedAr) return [];
            
            if (!selectedAr.items) selectedAr.items = [];
            return selectedAr.items;
        },
        onOpenItem: { itm, colName->
            def handler = { o->
                if (!o.list) throw new Exception("At least 1 breakdown is required.");
                
                if (o.added) entity._addedbreakdown = o.added;
                if (o.removed) entity._removedbreakdown = o.removed;
                
                itm.items = o.list;
                def a = o.list?.amount.sum();
                itm.totalbreakdown = (a? a : 0);
                itm._edited = true;
                itemsHandler?.reload();
            }
            def params = [
                data    : itm,
                handler : handler,
                mode    : mode,
                entity  : entity
            ]
            return Inv.lookupOpener('loanarliquidationitem:open', params);
        }
    ] as BasicListModel;
    
    /*
    def getTotalliquidation() {
        if (!entity.liquidations) return 0;
        
        def a = entity.liquidations?.amount?.sum();
        return (a? a : 0);
    }
    
    def liquidationHandler = [
        fetchList: { o->
            if (!entity.liquidations) entity.liquidations = [];
            return entity.liquidations;
        },
        onOpenItem: { itm, colName->
            def handler = { o->
                o._edited = true;
                selectedLiquidation.putAll(o);
                liquidationHandler?.reload();
                binding?.refresh('totalliquidation');
            }
            return Inv.lookupOpener('loanarliquidationitem:open', [handler: handler, entity: itm, mode: mode]);
        }
    ] as BasicListModel;
    
    def addLiquidation() {
        def handler = { o->
            if (!o.parentid) o.parentid = entity.objid;
            
            if (!entity._addedliquidation) entity._addedliquidation = [];
            entity._addedliquidation.add(o);
            
            entity.liquidations.add(o);
            liquidationHandler?.reload();
            binding?.refresh('totalliquidation');
        }
        return Inv.lookupOpener('loanarliquidationitem:create', [handler: handler, mode: mode]);
    }
    
    void removeLiquidation() {
        if (!MsgBox.confirm("You are about to remove this liquidation. Continue?")) return;
        
        if (!entity._removedliquidation) entity._removedliquidation = [];
        entity._removedliquidation.add(selectedLiquidation);
        
        if (entity._addedliquidation) entity._addedliquidation.remove(selectedLiquidation);
        entity.liquidations.remove(selectedLiquidation);
        liquidationHandler?.reload();
        binding?.refresh('totalliquidation');
    }
    */
    
    void submitForVerification() {
        if (!MsgBox.confirm("You are about to submit this document for verification. Continue?")) return;
        
        entity = service.submitForVerification(entity);
        checkEditable(entity);
    }
    
    void verify() {
        if (!MsgBox.confirm("You are about to verify this document. Continue?")) return;
        
        entity = service.verify(entity);
        checkEditable(entity);
    }
    
    def sendBack() {
        def handler = { remarks->
            entity.remarks = remarks;
            entity = service.sendBack(entity);
            binding?.refresh();
            checkEditable(entity);
        }
        return Inv.lookupOpener('remarks:create', [title: 'Reason for Send Back', handler: handler]);
    }
    
    def viewSendBack() {
        return Inv.lookupOpener('remarks:open', [title: 'Reason for Send Back', remarks: entity.sendbackremarks]);
    }
}

