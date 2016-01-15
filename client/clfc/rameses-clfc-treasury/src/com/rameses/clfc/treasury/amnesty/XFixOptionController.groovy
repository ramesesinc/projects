package com.rameses.clfc.treasury.amnesty

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class FixOptionController 
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    def entity;
    def allowEdit;
    def selectedOffer;
    def grantedoffer;
    def forApprovalMode;
    def approvalMode;
    def allowAmend;
    def prevoffer;
    
    void init() {
        if (!entity) entity = [:];
        if (allowAmend==true) {
            prevoffer = [:];
            prevoffer.putAll(entity.grantedoffer);
        }
    }
    
    def tableHandler = [
        fetchList:{o ->
            if (!entity.offers) entity.offers = [];
            return entity.offers;
        },
        onOpenItem: { itm, colName-> 
            //def editAmountOnly = false;
            //if (entity.txnstate == 'FOR_APPROVAL' && caller?.isFla == true) editAmountOnly = true;
            def params = [
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
            return Inv.lookupOpener('offer:open', params);
        }
    ] as BasicListModel;
      
    
    public void setSelectedOffer( selectedOffer ) {
        this.selectedOffer = selectedOffer;
        
        if (allowAmend==true || (entity.txnstate == 'APPROVED' && !entity.txntype)) {
            entity.grantedoffer = selectedOffer;
            binding.refresh('grantedoffer');
        }
    }
    
    def buildOfferText( offer ) {
        def text = new java.text.DecimalFormat("#,##0.00").format(offer.amount);
        if (offer.isspotcash == 1) {
            text += " spot cash";
            if (offer.date) {
                def date;
                if (offer.date instanceof Date) {
                    date = offer.date
                } else {
                    date = java.sql.Date.valueOf(offer.date);
                }
                date = new java.text.SimpleDateFormat("MMM-dd-yyyy").format(date)
                text += " " + (date? date : "");
            }
        } else {
            if (offer.days > 0) text += " " + offer.days + " day(s)";
            if (offer.months > 0) text += " " + offer.months + " month(s)";
        }
        return text;
    }
    
    def getGrantedoffertext() {
        def text = "";
        if (entity.grantedoffer) {
            text = buildOfferText(entity.grantedoffer);
        }
        return text;
    }
    
    def getPreviousoffertext() {
        def text = "";
        if (prevoffer) {
            text = buildOfferText(prevoffer);
        }
        return text;
    }
    
    def addOffer(){
        def handler = { o->
            entity.offers.add(o);
            
            if (!entity._addedoffers) entity._addedoffers = [];
            entity._addedoffers.add(o);
            
            tableHandler.reload();
            binding?.refresh('selectedOffer');
        }
        return Inv.lookupOpener('offer:create',[handler: handler, parentid: entity.objid]);
    }

    void removeOffer() {
        if (!MsgBox.confirm("You are about to remove this offer. Continue?")) return;
        
        if (!entity._removedoffers) entity._removedoffers = [];
        entity._removedoffers.add(selectedOffer);
        entity.offers.remove(selectedOffer);
        tableHandler.reload();
    }
    
}

