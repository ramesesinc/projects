package com.rameses.clfc.collection.shortage.fundrequest;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class ShortageFundRequestController extends CRUDController
{
    @Caller
    def caller;

    @Binding
    def binding;

    String serviceName = "LoanCollectionShortageFundRequestService";
    String entityName = "shortagefundrequest";

    boolean allowCreate = false;
    boolean allowDelete = false;
    boolean allowApprove = false;

    def collector;
    def remittanceid;

    Map createEntity() {
        return [
            objid       : 'SFR' + new UID(),
            remittanceid: remittanceid,
            collector   : collector
        ]
    }

    void submitforapproval() {
        if (MsgBox.confirm("You are about to submit this request for approval. Continue?")) {
            entity = service.submitForApproval([objid: entity.objid]);
            binding.refresh('formActions');
            EventQueue.invokeLater({ caller?.reload(); });
        }
    }

    void approveDocument() {
        if (MsgBox.confirm("You are about to approve this request. Continue?")) {
            entity = service.approveDocument([objid: entity.objid]);
            binding.refresh('formActions');
            EventQueue.invokeLater({ caller?.reload(); });
        }
    }

    void reject() {
        if (MsgBox.confirm("You are about to reject this request. Continue?")) {
            entity = service.reject([objid: entity.obijid]);
            binding.refresh('formActions');
            EventQueue.invokeLater({ caller?.reload(); });
        }
    }
}