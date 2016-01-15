package com.rameses.clfc.treasury.amnesty

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class FixOptionController 
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    def entity, mode = 'read', selectedOffer;
    def selectedRecommendation;
    
    void init() {
        if (!entity) entity = [:];
    }
    
    def recommendationHandler = [
        fetchList: { o->
            if (!entity.recommendation) entity.recommendation;
            return entity.recommendation;
        },
        onOpenItem: { itm, colName->
            return Inv.lookupOpener('offer:open', [entity: itm, mode: mode])
        }
    ] as BasicListModel
    
    def tableHandler = [
        fetchList:{o ->
            if (!entity.offers) entity.offers = [];
            return entity.offers;
        },
        onOpenItem: { itm, colName-> 
            /*def params = [
                entity  : selectedOffer,
                mode    : mode
            ]*/
            def isfla = false;
            if (entity.txnstate=='FOR_APPROVAL' && ClientContext.currentContext.headers.ROLES.containsKey("LOAN.FLA_OFFICER")) {
                    isfla = true;
            }
            return Inv.lookupOpener('offer:open', [entity: itm, mode: mode, isfla: isfla]);
            //def editAmountOnly = false;
            //if (entity.txnstate == 'FOR_APPROVAL' && caller?.isFla == true) editAmountOnly = true;
            /*def params = [
                entity          : selectedOffer,
                txnstate        : entity.txnstate,
                allowEdit       : allowEdit,
                forApprovalMode : forApprovalMode,
                approvalMode    : approvalMode,
                allowAmend      : (allowAmend? allowAmend : false),
                handler         : { 
                    tableHandler.reload(); 
                    binding.refresh('grantedoffer');
                }
            ]
            return Inv.lookupOpener('offer:open', params);*/
        }
    ] as BasicListModel;      
    
    void setSelectedOffer( selectedOffer ) {
        this.selectedOffer = selectedOffer;
        def state = entity.txnstate;
        if ((state=='APPROVED' || state=='SEND_BACK') && !entity.txntype) {
            entity.grantedoffer = selectedOffer;
        }
    }
    
    def addRecommendation() {
        def handler = { o->
            if (!entity.recommendation) entity.recommendation = [];
            entity.recommendation.add(o);
            
            recommendationHandler?.reload();
        }
        return Inv.lookupOpener('offer:create', [handler: handler, parentid: entity.objid, mode: mode]);
    }
    
    void removeRecommendation() {
        if (!MsgBox.confirm("You are about to remove this recommendation. Continue?")) return;
        
        entity.recommendation.remove(selectedRecommendation);
        recommendationHandler?.reload();
    }
    
    def addOffer(){
        def handler = { o->            
            if (!entity._addedoffers) entity._addedoffers = [];
            entity._addedoffers.add(o);
            
            entity.offers.add(o);
            
            tableHandler.reload();
            binding?.refresh('selectedOffer');
        }
        return Inv.lookupOpener('offer:create',[handler: handler, parentid: entity.objid, mode: mode]);
    }

    void removeOffer() {
        if (!MsgBox.confirm("You are about to remove this offer. Continue?")) return;
        
        if (!entity._removedoffers) entity._removedoffers = [];
        entity._removedoffers.add(selectedOffer);
        
        if (entity._addedoffers) entity._addedoffers.remove(selectedOffer);
        
        entity.offers.remove(selectedOffer);
        tableHandler.reload();
    }
    
    def getAmountavailedtext() {
        return entity.amountavailed;
        /*def str = '';
        def offer = entity.grantedoffer;
        def decformat = new java.text.DecimalFormat("#,##0.00");
        def dateformat = new java.text.SimpleDateFormat("MMM-dd-yyyy");
        if (offer.isspotcash == 1) {
            str = "Spot cash " + decformat.format(offer.amount) + " " + dateformat.format(offer.date);
        } else if (offer.isspotcash == 0) {
            str = decformat.format(offer.amount);
            if (offer.days > 0) str += ' ' + offer.days + ' day(s)';
            if (offer.months > 0) str += ' ' + offer.months + ' month(s)';
            if (offer.days == 0 && offer.months == 0) str += ' No maturity date';
        }
        return str;*/
    }
    
}

