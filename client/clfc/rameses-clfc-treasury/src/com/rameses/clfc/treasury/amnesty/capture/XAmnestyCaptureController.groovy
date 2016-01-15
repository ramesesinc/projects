package com.rameses.clfc.treasury.anmesty.capture;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class XAmnestyCaptureController extends CRUDController
{
    @Binding
    def binding;
    
    @Service("DateService")
    def dateSvc;
    
    String serviceName = "AmnestyCaptureService";
    String title = "Amnesty Capture";
    
    boolean allowDelete = false;
    boolean allowApprove = false;
    boolean allowEdit = true;
    
    def option;
    def prevoffers;
    def prevgrantedoffer;

    @PropertyChangeListener
    def listener = [
        "option": {o ->
            entity.amnestyoption = o.caption;
            println 'option change';
        }
    ];
    
    
    Map createEntity() {
        return [
            objid           : 'AMNSTYC' + new UID(),
            txnstate        : 'DRAFT',
            txnmode         : 'CAPTURE',
            txndate         : dateSvc.getServerDateAsString().split(" ")[0],
            iswaivepenalty  : 0,
            iswaiveinterest : 0,
            version         : 1
        ];
    }
    
    def borrowerLookup = Inv.lookupOpener('ledgerborrower:lookup', [
        onselect: { o->
           entity.borrower = o.borrower;
           entity.loanapp = o.loanapp;
           entity.ledgerid = o.objid;
        },     
        pastdueledgers: true  
    ]);

    def options = [
        [name: 'waiver', caption: 'WAIVER'],
        [name: 'fix', caption: 'FIX']
    ];
    
    void afterOpen( data ) {
        if (data.txnstate!='DRAFT') allowEdit = false;
        option = options.find{ it.caption == data.amnestyoption }
    }
    
    void afterEdit( data ) {
        prevoffers = [];
        def item;
        data.offers.each{ o->
            item = [:];
            item.putAll(o);
            prevoffers.add(item);
        }
        prevgrantedoffer = [:];
        prevgrantedoffer.putAll(entity.grantedoffer);
    }
    
    void afterCancel() {
        entity.offers = prevoffers;
        entity.grantedoffer = prevgrantedoffer;
    }
    
    void refresh( text ) {
        binding.refresh(text);
    }
    
    void refresh() {
        binding.refresh();
    }
    
    def getOpener() {
        if (!option) return;
        def params = [
            entity          : entity,
            allowEdit       : (mode != 'read'? true : false),
            refreshHandler  : { o->
                binding.refresh(o);
            }
        ];
        return Inv.lookupOpener(option.name + "capture:option", params);
    }
    
    
    void approveDocument() {
        if (!MsgBox.confirm("You are about to approve this document. Continue?")) return;
        
        entity = service.approveDocument(entity);
        allowEdit = false;
        binding.refresh('formActions');
    }
    
    void closeAmnesty() {
        if (!MsgBox.confirm("You are about to close this document. Continue?")) return;
        
        entity = service.closeAmnesty(entity);
        binding.refresh('formActions');
    }
    
}

