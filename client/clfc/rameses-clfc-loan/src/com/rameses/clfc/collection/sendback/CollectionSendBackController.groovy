package com.rameses.clfc.collection.sendback;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class CollectionSendBackController
{
    @Binding
    def binding;

    @Service("RemittanceSendBackService")
    def service;

    def entity;
    def remittanceid;
    def mode = 'read';
    def action;
    def afterSaveHandler;
    def afterAcceptHandler;

    String title = "Send Back";

    void init() {
        mode = 'create';
        entity = [
            objid       : 'SB' + new UID(),
            state       : 'DRAFT',
            remittanceid:  remittanceid  
        ];
    }

    def close() {
        return "_close";
    }

    void open() {
        entity = service.openByRemittanceid([remittanceid: remittanceid]);
        if (!entity) init();
    }

    void save() {
        if (MsgBox.confirm("You are about to save this document. Continue?")) {
            entity = service.create(entity);
            mode = 'read';
            if (afterSaveHandler) afterSaveHandler(entity);
            binding.refresh('formActions');
        }
    }

    void accept() {
        if (MsgBox.confirm("You are about to accept this document. Continue?")) {
            entity = service.accept(entity);
            if (afterAcceptHandler) afterAcceptHandler(entity);
            binding.refresh('formActions');
        }
    }
}