package com.rameses.clfc.patch.report.amend.branch.outstanding

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class AmendBranchOutstandingReportController 
{
    @Service("AmendBranchOutstandingReportService")
    def service;
    
    @Service("DateService")
    def dateSvc;
    
    def txndate, selectedItem, list;
    def page, _remove, entity, searchtext;
    
    String title = "Amend Branch Outstanding";
    
    void init() {
        txndate = dateSvc.getServerDateAsString();
        page = 'default';
    }
    
    def close() {
        return '_close';
    }
    
    def back() {
        if (_remove) {
            if (!MsgBox.confirm("There are items removed. Continuing will cancel all items removed. Do you still want to continue?")) return;
        }
        page = 'default'
        return page;
    }
    
    def next() {
        entity = service.getList([txndate: txndate]);
        _remove = [];
        searchtext = null;
        search();
        page = 'main';
        return page;
    }
    
    def listHandler = [
        fetchList: { o->            
            if (!list) list = [];
            //if (!entity.list) entity.list = [];
            list.sort{ it.borrower }
            return list;
        }
    ] as BasicListModel;
    
    void removeItem() {
        if (!MsgBox.confirm("You are about to remove this item. Continue?")) return;
        
        if (!_remove) _remove = [];
        _remove.add(selectedItem);
        
        entity?.list.remove(selectedItem);
        search();
    }
    
    void cancelRemove() {
        if (!MsgBox.confirm("You are about to cancel items removed. Continue?")) return;
        
        _remove.each{ r->
            entity.list.add(r);
        }
        _remove = [];
        search();
    }
    
    void update() {
        entity = service.updateList([entity: entity, _remove: _remove]);
        _remove = [];
        search();
        MsgBox.alert("Successfully updated list");
    }
    
    void refresh() {
        refresh(entity?.list);
    }
    
    void refresh( sourcelist ) {
        list = [];
        if (sourcelist) list.addAll(sourcelist);
        listHandler?.reload();
    }
    
    void search() {
        def xlist;
        if (searchtext) xlist = entity.list.findAll{ (it.borrower.startsWith(searchtext) == true) }
        refresh(xlist);
    }
}

