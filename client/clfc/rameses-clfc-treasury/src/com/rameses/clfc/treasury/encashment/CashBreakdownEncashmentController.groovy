package com.rameses.clfc.treasury.encashment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CashBreakdownEncashmentController
{
    @ChangeLog
    def changeLog;
    
    @Service("EncashmentService")
    def service;

    def entity, list, selectedItem;

    void init() {
        list = service.getEncashmentsByCashBreakdown(entity);
    }

    def listHandler = [
        fetchList: { o->
            if (!list) list = [];
            return list;
        }
    ] as BasicListModel;

    def getCashbreakdown() {       
        def entries = [];
        def totalbreakdown = 0;
        if (selectedItem?.items) {
            entries = selectedItem.items;
            totalbreakdown = selectedItem.items.amount.sum();
        }
        def params = [
            entries         : entries,
            totalbreakdown  : totalbreakdown,
            editable        : false,
        ]
        return Inv.lookupOpener('clfc:denomination', params);
    }

    def close() {
        return "_close";
    }
}