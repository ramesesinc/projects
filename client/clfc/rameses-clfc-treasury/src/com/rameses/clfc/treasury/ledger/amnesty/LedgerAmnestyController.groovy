package com.rameses.clfc.treasury.ledger.amnesty

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LedgerAmnestyController 
{
    @Service('LedgerAmnestyService')
    def service;
    
    @Service('DateService')
    def dateSvc;
    
    @ChangeLog
    def changeLog;
    
    def mode = 'read', recommendationmode = 'read', entity;
    def lockid = 'LOCK' + new UID();
    
    def createEntity() {
        return [
            objid   : 'AMST' + new UID(),
            txnstate: 'DRAFT',
            txnmode : 'ONLINE',
            txndate : dateSvc.getServerDateAsString().split(' ')[0],
            lockid  : lockid
        ]
    }
    
    void create() {
        removeLockid();
        entity = createEntity();
        mode = 'create';
        recommendationmode = 'read';
    }
    
    void open() {
        //entity.lockid = lockid;
        //entity = service.updateLockid(entity);
        entity = service.open(entity);
        mode = 'read';
        recommendationmode = 'read';
    }
    
    void removeLockid() {
        //service.removeLockid(entity);
    }
    
    def close() {
        removeLockid();
        return '_close';
    }
    
    def previtems;
    void edit() {
        /*
        previtems = [];
        def itm;
        entity?.items?.each{ o-> 
            itm = [:];
            itm.putAll(o);
            previtems.add(itm);
        }
        */
        previtems = copyList(entity?.items);
        mode = 'edit';
    }
    
    def copyList( src ) {
        def list = [];
        def itm;
        src?.each{ o->
            itm = [:];
            itm.putAll(o);
            list.add(itm);
        }
        return list;
    }
    
    def cancel() {
        if (mode == 'edit') {            
            if (changeLog.hasChanges()) {
                changeLog.undoAll();
                changeLog.clear();
            }
            
            if (previtems) {
                entity.items = [];
                entity.items.addAll(previtems);
            }
            entity._addeditems = [];
            entity._removeditems = [];
            
            mode = 'read';
            return null;
        }
        return '_close';
    }
    
    def getStatus() {
        def str = '';
        if (entity?.txnmode) {
            str += entity.txnmode + ' ';
            if (entity?.txnstate) str += '- ';
        }
        if (entity?.txnstate) str += entity.txnstate + ' ';
        
        return str;
    }
    
    def getOptionsList() {
        getOpeners: {
            def params = [entity: entity, mode: mode, recommendationmode: recommendationmode];
            def list = Inv.lookupOpeners("ledgeramnesty-plugin", params);
            
            def item = list.find{ it.properties.plugintype == 'poster' }
            if (item) {
                if (!entity?.poster?.objid) {
                    list.remove(item);
                } else if (entity?.poster?.objid && entity.txnstate=='DISAPPROVED') {
                    item.caption = 'Disapprover';
                }
            }
            
            return list;
        }
    }
    
    def validate( data ) {
        def msg = '';
        def flag = false;
        if (!data?.borrower) msg += 'Borrower is required.\n';
        if (!data?.remarks) msg += 'Remarks is required.\n';
        if (msg) flag = true;//throw new Exception(msg);
        return [msg: msg, haserror: flag];
    }
    
    void save() {
        def res = validate(entity);
        if (res.haserror == true) throw new Exception(res.msg);
        
        if (!MsgBox.confirm("You are about to save this record. Continue?")) return;
        
        if (mode== 'create') {
            entity = service.create(entity);
        } else if (mode == 'edit') {
            entity = service.update(entity);
        }
        mode = 'read';
    }
    
    void submitForApproval() {
        if (!MsgBox.confirm('You are about to submit this document for approval. Continue?'))
        
        println 'passing 1';
        entity = service.submitForApproval(entity);
        println 'passing 2';
    }
    
    void returnDocument() {
        if (!MsgBox.confirm('You are about to return this document. Continue?')) return;
        
        entity = service.returnDocument(entity);
    }
    
    void editRecommendations() {
        previtems = copyList(entity?.items);
        recommendationmode = 'edit';
    }
    
    void cancelRecommendations() {
        if (previtems) {
            entity?.items = [];
            entity?.items.addAll(previtems);
        }
        recommendationmode = 'read';
    }
    
    void saveRecommendations() {
        if (!MsgBox.confirm('You are about to save recommendations. Continue?')) return;
        
        entity = service.saveRecommendations(entity);
        recommendationmode = 'read';
    }
        
    @Close
    void closing() { 
        removeLockid();
        //println 'closing';
    } 
    
}

