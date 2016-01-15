package com.rameses.clfc.ledger.compromise;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.LoanUtil;
import java.rmi.server.UID;

class LoanLedgerCompromiseController extends CRUDController
{
    @Binding
    def binding;

    String serviceName = "LoanLedgerCompromiseService";
    String entityName = "compromise";

    boolean allowApprove = false;
    boolean allowEdit = true;
    boolean allowDelete = true;

    def compromiseTypes = LoanUtil.compromiseTypes;
    def selectedOffer, ledgerid;
    def offer, offerList, prevEntity;

    Map createEntity() {
        return [
            objid   : 'LC' + new UID(),
            state   : 'DRAFT',
            offers  : [],
            ledgerid: ledgerid
        ];
    }

    void afterCreate( data ) {
        listHandler.reload();
    }

    void afterOpen( data ) {
        if (data.state != 'DRAFT') {
            allowEdit = false;
            allowDelete = false;
        }
    }
    
    public void setSelectedOffer( selectedOffer ) {
        this.selectedOffer = selectedOffer;
        if (entity.state == 'FOR_APPROVAL') {
            entity.offer = selectedOffer;
            entity.offer.days = entity.offer.term.days;
            entity.offer.months = entity.offer.term.months;
        }
    }

    def listHandler = [
        fetchList: { o->
            if (!entity.offers) entity.offers = [];
            return entity.offers;
        },
        createItem: { 
            return [
                objid               : 'LCO' + new UID(), 
                parentid            : entity.objid, 
                term                : [days:0, months:0],
                payuntilamountpaid  : 0
            ]; 
        },
        onAddItem: { o->
            entity.offers.add(o);
            if (!entity._added) entity._added = [];
            entity._added.add(o);
        },
        onRemoveItem: { o->
            if (MsgBox.confirm('You are about to remove this offer. Continue')) {
                if (!entity._removed) entity._removed = [];
                entity._removed.add(o);
                entity.offers.remove(o);
                return true;
            }
            return false;
        },
        onColumnUpdate: {itm, colName->
            itm._edited = true;
        }
    ] as EditorListModel;

    void submitForApproval() {
        if (MsgBox.confirm("You are about to submit this document for approval. Continue?")) {
            entity = service.submitForApproval(entity);
            allowEdit = false;
            allowDelete = false;
            binding.refresh();
        }
    }

    void afterSave( data ) {
        binding.refresh('formActions');
    }

    void approveDocument() {
        if (!entity.offer) 
            throw new Exception("Plase select an offer before you can approve this document.");

        if (MsgBox.confirm("You are about to approve this document. Continue?")) {
            entity = service.approveDocument(entity);
            binding.refresh();
        }
    }

    void disapprove() {
        if (MsgBox.confirm("You are about to disapprove this document. Continue?")) {
            entity = service.disapprove(entity);
            binding.refresh();
        }
    }

    void changeOffer() {
        prevEntity = [:];
        prevEntity.putAll(entity);
        prevEntity.offers = [];
        def item;
        entity.offers.each{ o->
            item = [:];
            item.putAll(o);
            prevEntity.offers.add(item);
        }
        create();
        entity.dteffective = prevEntity.dteffective;
        entity.remarks = prevEntity.remarks;
        entity.txntype = prevEntity.txntype;
        entity.prevcompromiseid = prevEntity.objid;
        entity.prevoffer = prevEntity.offer;
        prevEntity.offers.each{ o->
            item = [:];
            item.putAll(o);
            item.objid = 'LCO' + new UID();
            item.parentid = entity.objid;
            entity.offers.add(item);
            if (!entity._added) entity._added = [];
            entity._added.add(item);
        }
        entity.changeoffer = true;
        listHandler.reload();
        allowEdit = true;
        allowDelete = true;
    }
}