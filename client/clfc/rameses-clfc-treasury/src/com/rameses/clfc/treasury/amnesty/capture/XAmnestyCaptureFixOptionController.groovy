package com.rameses.clfc.treasury.amnesty.capture

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class XAmnestyCaptureFixOptionController 
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    def entity;
    def allowEdit;
    def selectedOffer;
    def grantedoffer;
    def prevoffer;
        
    void init() {
        if (!entity) entity = [:];
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
                allowEdit       : allowEdit,
                handler         : { o->
                    if (o.isspotcash==1 && o.date < entity.dtstarted)
                        throw new Exception("Date for spot cash must be greater than or equal to date started.");
                    
                    tableHandler.reload(); 
                    binding.refresh('grantedoffer');
                }
            ]
            return Inv.lookupOpener('offercapture:open', params);
        }
    ] as BasicListModel;
      
    
    public void setSelectedOffer( selectedOffer ) {
        this.selectedOffer = selectedOffer;
        
        if (allowEdit==true) {
            entity.grantedoffer = selectedOffer;
            if (!entity.grantedoffer) {
                entity.dtended = null;
            } else {
                def offer = entity.grantedoffer;
                try {
                    if (offer.isspotcash!=0) {
                        entity.dtended = offer.date;
                    } else if (offer.isspotcash==0) {
                        Calendar c = Calendar.getInstance();
                        def date;
                        if (entity.dtstarted instanceof Date) {
                            date = entity.dtstarted;
                        } else if (entity.dtstarted instanceof String) {
                            date = java.sql.Date.valueOf(entity.dtstarted);
                        }
                        c.setTime(date);
                        if (offer.days) c.add(Calendar.DATE, offer.days);
                        if (offer.months) c.add(Calendar.MONTH, offer.months);
                        entity.dtended = new java.text.SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
                    }
                    
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                //caller?.binding.refresh('entity.dtended');
            }
            binding.refresh('grantedoffer');
            caller?.binding.refresh('entity.dtended');
            //caller?.refresh('entity.dtended');
            //println 'end date ' + entity.dtended;
            /*def r = [
                run: {
                    caller?.refresh('entity.dtended');
                }
            ] as Runnable;
            new Thread(r).start();*/
            //EventQueue.invokeLater({ caller?.refresh('entity.dtended'); });
            
        }
    }
    
    def buildOfferText( offer ) {
        def text = "";
        text += new java.text.DecimalFormat("#,##0.00").format(offer.amount);
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
        if (!entity.dtstarted) throw new Exception("Please specify date started.");
        
        def handler = { o->
            if (o.isspotcash==1 && o.date < entity.dtstarted) 
                throw new Exception("Date for spot cash must be greater than or equal to date started.");
            
            entity.offers.add(o);
            
            if (!entity._addedoffers) entity._addedoffers = [];
            entity._addedoffers.add(o);
            
            tableHandler.reload();
            //binding?.refresh('selectedOffer');
        }
        return Inv.lookupOpener('offercapture:create',[handler: handler, parentid: entity.objid]);
    }

    void removeOffer() {
        if (!MsgBox.confirm("You are about to remove this offer. Continue?")) return;
        
        if (!entity._removedoffers) entity._removedoffers = [];
        entity._removedoffers.add(selectedOffer);
        entity.offers.remove(selectedOffer);
        tableHandler.reload();
    }
    
}

