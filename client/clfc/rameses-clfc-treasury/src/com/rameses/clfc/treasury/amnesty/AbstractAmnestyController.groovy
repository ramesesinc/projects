package com.rameses.clfc.treasury.amnesty;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

abstract class AbstractAmnestyController 
{
    @Binding
    def binding;
    
    @ChangeLog
    def changeLog;
    
    abstract String getServiceName();
    /*String getServiceName() {
        return "AmnestyService";
    }*/
    
    def getService() {        
        String name = getServiceName();
        if ((name == null) || (name.trim().length() == 0)) {
          throw new NullPointerException("Please specify a serviceName");
        }
        return InvokerProxy.getInstance().create(name);
    }
    
    
    def mode = 'read';
    def entity, ledger, prevoffers, prevoption;
    
    def createEntity() {
        return [
            objid           : 'MF' + new UID(),
            txnstate        : 'DRAFT'
        ];
    }
    
    void create() {
        entity = createEntity();
        mode = 'create';
    }
    
    void open() {
        entity = service.open(entity);
        mode = 'read';
    }
    
    def close() {
        return "_close";
    }
    
    def cancel() {
        if (mode == 'edit') {            
            if (changeLog.hasChanges()) {
                changeLog.undoAll();
                changeLog.clear();
            }
            
            entity.amnestyoption = prevoption;
            entity.offers = prevoffers;
            entity._removedoffers = [];
            entity._addedoffers = [];
            
            mode = 'read';
            return null;
        }
        return close();
    }
    
    void edit() {
        prevoption = entity.amnestyoption;
        prevoffers = [];
        def item;
        entity.offers.each{ o->
            item = [:];
            item.putAll(o);
            prevoffers.add(item);
        }
        mode = 'edit';
    }
    
    def validate( data ) {
        def msg = '';
        def flag = false;
        if (!data.borrower) msg += 'Borrower is required.\n';
        if (!data.ledger.balance) msg += 'Principal Balance is required.\n';
        if (!data.remarks) msg += 'Remarks is required.\n';
        if (!data.amnestyoption) msg += 'Option is required.\n';
        if (msg) flag = true;//throw new Exception(msg);
        return [msg: msg, haserror: flag];
    }
    
    def save() {
        def res = validate(entity);
        if (res.haserror == true) throw new Exception(res.msg);
        
        if (!MsgBox.confirm("You are about to save this record. Continue?")) return;
        
        if (mode== 'create') {
            entity = service.create(entity);
        } else if (mode == 'edit') {
            entity = service.update(entity);
        }
        mode = 'read';
        
        return null;
    }
    
    void submitForApproval() {
        if (!MsgBox.confirm("You are about to submit this document for approval. Continue?")) return;
        
        entity = service.submitForApproval(entity);        
    }
    
    def approve() {
        if (!MsgBox.confirm("You are about to approve this document. Continue?")) return;
        
        def handler = { remarks->
            try {                
                entity.posterremarks = remarks;
                entity = service.approveDocument(entity);

                binding?.refresh();
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        }
        return Inv.lookupOpener('remarks:create', [title: 'Approval remarks', handler: handler]);
    }
    
    def disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this document. Continue?")) return;
        
        def handler = { remarks->
            try {
                entity.posterremarks = remarks;
                entity = service.disapprove(entity);

                binding?.refresh();
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        }
        return Inv.lookupOpener('remarks:create', [title: 'Disapproval remarks', handler: handler]);
    }    
    
    void avail() {
        if (!entity.grantedoffer?.amount) throw new Exception("Please select an offer to avail.");
        
        def xoffer = entity.grantedoffer;       
        def decformat = new java.text.DecimalFormat("#,##0.00");
        /*
        def dateformat = new java.text.SimpleDateFormat("MMM-dd-yyyy");
        
        if (xoffer.isspotcash==1) {
            def dt;
            if (xoffer.date instanceof Date) {
                dt = xoffer.date;
            } else {
                dt = java.sql.Date.valueOf(xoffer.date);
            }
            offertext = decformat(xoffer.amout) + " Spot cash " + dateformat.format(dt);
        } else if (xoffer.isspotcash==0) {
            
        }*/
        def offertext = decformat.format(xoffer.amount) + " " + xoffer.term;
        
        def msg = "<html>You are about to avail offer <b>${offertext}</b>. Continue?</html>";
        if (!MsgBox.confirm(msg)) return;
        
        entity = service.avail(entity);
    }
    
    boolean getAllowAvail() {
        def flag = false;
        def state = entity.txnstate;
        def option = entity.amnestyoption;
        def isstateok = (state=='APPROVED' || state=='SEND_BACK')? true : false;
        if (mode=='read' && isstateok==true && option=='FIX' && !entity.txntype) {
            flag = true;
        }
        return flag;
    }
    
    boolean getAllowReject() {
        def flag = false;
        def state = entity.txnstate;
        def option = entity.amnestyoption;
        def isstateok = (state=='APPROVED' || state=='SEND_BACK')? true : false;
        if (mode=='read' && isstateok==true && option=='FIX' && !entity.txntype) {
            flag = true;
        }
        return flag;
    }
    
    boolean getAllowSubmitForVerification() {
        def flag = false;
        def state = entity.txnstate;
        def type = entity.txntype;
        if (mode=='read' && (state=='APPROVED' || state=='SEND_BACK') && type=='AVAIL') {
            flag = true;
        }
        return flag;
    }
    
    void submitForVerification() {
        if (!MsgBox.confirm("You are about to submit this amnesty for verification. Continue?")) return;
        
        entity = service.submitForVerification(entity);
    }
    
    void verify() {
        if (!MsgBox.confirm("You are about to verify this amnesty. Continue?")) return;
        
        entity = service.verify(entity);
    }
    
    def sendBack() {
        def handler = { remarks->
            try {
                entity.sendbackremarks = remarks;
                entity = service.sendBack(entity);

                binding?.refresh();
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        }
        return Inv.lookupOpener('remarks:create', [title: 'Reason for send back', handler: handler]);
    }
        
    def viewSendBack() {
        return Inv.lookupOpener("remarks:open", [title: "Reason for Send Back", remarks: entity.sendbackremarks]);
    }
    
    def reject() {
        def handler = { remarks->
            try {
                entity.rejectionremarks = remarks;
                entity = service.reject(entity);

                binding?.refresh();
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        }
        return Inv.lookupOpener('remarks:create', [title: 'Reason for rejection', handler: handler]);
    }
    
    def getStatus() {
        def str = entity.txnstate;
        if (entity.txntype) str += ' - ' + entity.txntype;
        return str;
    }
    
    def getOptionsList() {
        getOpeners: {
            def list = Inv.lookupOpeners("amnesty-plugin", [entity: entity, mode: mode]);
            
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
}

