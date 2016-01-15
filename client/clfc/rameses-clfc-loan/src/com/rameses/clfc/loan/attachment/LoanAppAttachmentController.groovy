package com.rameses.clfc.loan.attachment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

class LoanAppAttachmentController
{
    //feed by the caller
    def loanapp, caller, selectedMenu;
    def attachments;
    
    void init() {
        selectedMenu.saveHandler = { save(); }
    }
    
    void save() {
        println 'saving attachment'
    }
    
    def selectedAttachment;
    def attachmentHandler = [
        fetchList: {o->
            if(!attachments) attachments = [];
            attachments.each{ it._filetype = "attachment" }
        },
        onRemoveItem: {o->
            return removeItemImpl(o);
        },
        getOpenerParams: {o->
            return [mode: caller.mode, caller:this];
        }
    ] as EditorListModel;
    
    def addAttachment() {
        def handler = {attachment->
            attachments.add(attachment);
        }
        return InvokerUtil.lookupOpener("attachment:create", [handler:handler]);
    }
    
    void removeAttachment() {
        removeItemImpl(selectedAttachment);
    }
    
    boolean removeItemImpl(o) {
        if(caller.mode == 'read') return false;
        if(MsgBox.confirm("You are about to remove this attachment. Continue?")) {
            attachments.remove(o);
            return true;
        }
        return false;
    }
    
    def getHtmlview() {
        return '';
    }
}