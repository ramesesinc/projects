package com.rameses.clfc.treasury.ledger.amnesty

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.text.*;

class AmnestyInformationController 
{
    def entity, mode = 'read', recommendationmode = 'read';
    def decFormat = new DecimalFormat('#,##0.00');
    def dateFormat = new SimpleDateFormat("MMM-dd-yyyy");
    
    def parseDate( date ) {
        if (!date) return null;
        if (date instanceof Date) {
            return date;
        } else {
            return java.sql.Date.valueOf(date);
        }
    }
    
    void init() {
        if (!entity) entity = [:];
    }
    
    def selectedItem;
    def listHandler = [
        fetchList: { o->
            if (!entity.items) entity.items = [];
            entity?.items?.each{ println 'ei ' + it }
            return entity.items;
        },
        onOpenItem: { itm, colName->
            if (itm) return openItemImpl(itm);
            return null;
        }
    ] as BasicListModel;
    
    def openItem() {
        return openItemImpl(selectedItem);
    }
    
    def openItemImpl( itm ) {
        def handler = { o->
            if (entity?.txnstate) o.state = entity?.txnstate;
            buildDescription(o);

            def i = entity.items?.find{ it.objid == o.objid }
            if (i) {
                entity?.items?.remove(i);
                o._edited = true;
                entity?.items?.add(o);
                //i.clear();
                //i._edited = true;
                //i.putAll(o);
            }

            if (entity?._addeditems) {
                i = entity?._addeditems?.find{ it.objid == o.objid }
                if (i) {
                    entity?._addeditems?.remove(i);
                    entity?._addeditems?.add(o);
                    //i = o;
                    //i.clear();
                    //i.putAll(o);
                }
            }

            listHandler?.reload();
        }
        def xmode = mode;
        if (entity?.txnstate == 'FOR_APPROVAL') {
            xmode = recommendationmode;
        }
        def op = Inv.lookupOpener('ledgeramnesty:item:open', [entity: itm, handler: handler, mode: xmode]);
        if (!op) return null;
        return op;
    }
    
    def addItem() {
        def handler = { o->
            if (entity?.txnstate) o.state = entity?.txnstate;
            buildDescription(o);
        
            if (!entity._addeditems) entity._addeditems = [];
            entity._addeditems.add(o);
            
            if (!entity.items) entity.items = [];
            entity.items.add(o);
            listHandler?.reload();
        }
        def op = Inv.lookupOpener('ledgeramnesty:item:create', [handler: handler]);
        if (!op) return null;
        return op;
    }
    
    void buildDescription( data ) {
        def description;
        def type = data?.amnestytype?.value;
        if (type == 'FIX') {
            description = decFormat.format(data.amount) + ' ';
            if (data?.type?.caption) {
                description += data.type.caption + ' ';
            }
            def xtype = data?.type?.value;
            if (xtype == 'lumpsum') {
                if (data.days > 0) description += data.days + ' Day(s) ';
            } else {
                if (data.usedate==0 || !data.usedate) {
                    if (data.months > 0) description += data.months + ' Month(s) ';
                    if (data.days > 0) description += data.days + ' Days(s) ';
                } else if (data.usedate==1) {
                    description += ' until ' + dateFormat.format(parseDate(data.date));
                }
            }
        } else {
            description = data?.amnestytype?.caption;
        }
        data.description = description;
    }
    
    void removeItem() {
        if (!MsgBox.confirm('You are about to remove this item. Continue?')) return;
        
        entity.items?.remove(selectedItem);
        if (entity._addeditems) entity._addeditems.remove(selectedItem);
        
        if (!entity._removeditems) entity._removeditems = [];
        entity._removeditems.add(selectedItem);
        
        listHandler?.reload();
    }
    
    void approveItem() {
        if (!MsgBox.confirm('You are about to approve this item. Continue?')) return;
        
    }
    
    void disapproveItem() {
        if (!MsgBox.confirm('You are about to disapprove this item. Continue?')) return;
    }
}

