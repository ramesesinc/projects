package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class RPTLedgerTagModel
{
    @Service('RPTLedgerService')
    def svc; 
    
    def entity;
    def selectedItem;
    def tags;
    
    void init(){
        tags = svc.getTags([objid:entity.objid]);
    }
    
    def listHandler = [
        fetchList :{ tags }
    ] as BasicListModel
    
    
    void addTag() {
        def tag = MsgBox.prompt('Enter Tag');
        if (tag) {
            def ledgertag = [:]
            ledgertag.parent = [objid: entity.objid];
            ledgertag.tag = tag.toUpperCase();
            ledgertag.putAll(svc.addTag(ledgertag));
            tags << ledgertag;
            listHandler.reload();
        }
    }

    void removeTag() {
        if (selectedItem && MsgBox.confirm('Remove tag?')) {
            svc.removeTag(selectedItem);
            tags.remove(selectedItem);
            listHandler.reload();
        }
    }
}