package com.rameses.clfc.encashment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class EncashmentMainController extends CRUDController
{
    @Binding
    def binding;

    String serviceName = "EncashmentService";
    String entityName = "encashment";

    /*
    @Service("EncashmentService")
    def svc;
    */
    
    @Service("DateService")
    def dateSvc;

    def opener, selectedOption, optionList = [];
    def prevcbs, prevreferences, prevencashments, prevchange;

    boolean allowDelete = false, allowApprove = false, allowEdit = true;    

    Map createEntity() {
        def data = [
            objid       : 'E' + new UID(),
            txnstate    : 'DRAFT',
            txndate     : dateSvc.getServerDateAsString().split(' ')[0],
            //items       : [],
            //cbs         : [],
            //references  : [],
            //breakdown   : [],
            //change      : []
        ];
        data.check = [
            objid   : data.objid,//'EC' + new UID(),
            txndate : data.txndate,
            parentid: data.objid
        ];
        /*
        data.cashbreakdown = [
            objid   : 'ECB' + new UID(),
            parentid: data.objid
        ];
        */
        return data;
    }

    void validateEntity( data ) {
        def msg = "";
        if (!data.txndate) msg += "Txndate is required.\n";
        if (!data.amount) msg += "Amount is required.\n";
        if (!data.remarks) msg += "Remarks is required.\n";
        if (!data.check.checkno) msg += "Check No. is required.\n";
        if (!data.check.txndate) msg += "Date is required.\n";
        if (!data.check.bank?.objid) msg += "Bank is required.\n";
        //if (!data.check.passbook?.objid) msg += "Passbook is required.\n";
        if (msg) throw new Exception(msg);
        
        def amt = 0;
        if (data.breakdown) {
            amt = data.breakdown.amount.sum();
        }
        if (!amt) amt = 0;
        
        def overage = 0
        if (data.overage) {
            overage = data.overage?.amount.sum();
        }
        if (!overage) overage = 0;
        amt += overage;
        
        def change = 0;
        if (data.change) {
            change = data.change?.amount.sum();
        }
        if (!change) change = 0;
        amt -= change;
        
        if (data.amount != amt)
            throw new Exception("Total encashment is not equal to amount for encashment.");

        def ref;
        data.breakdown.each{ o->
            ref = data.references.find{ it.denomination == o.denomination }
            if (o.qty > ref.qty)
                throw new Exception("Cannot save encashment. Qty for encashment exceeds reference qty.");
        }
    }

    void afterCreate( data ) {
        resetOptionList();
        optionsModel.reload();
        optionsModel.setSelectedIndex(0);
    } 

    void beforeSave( data ) {
        validateEntity(data);  
    }

    void afterSave( data ) {
        resetOptionListOpener();
        optionsModel.reload();
        optionsModel.setSelectedIndex(selectedOption.index);        
    }

    void afterOpen( data ) {
        resetOptionList();
        optionsModel.reload();
        optionsModel.setSelectedIndex(0);

        if (data.txnstate != 'DRAFT') allowEdit = false;
    }

    void afterEdit( data ) {
        resetOptionListOpener();
        optionsModel.reload();
        optionsModel.setSelectedIndex(selectedOption.index);

        prevcbs = [];
        def item;
        entity.cbs.each{ o->
            item = [:];
            item.putAll(o);
            prevcbs.add(item);
        }

        prevreferences = [];
        entity.references.each{ o->
            item = [:];
            item.putAll(o);
            prevreferences.add(item);
        }

        prevencashments = [];
        entity.encashments.each{ o->
            item = [:];
            item.putAll(o);
            prevencashments.add(item);
        }
        
        prevchange = [];
        entity.change.each{ o->
            item = [:];
            item.putAll(o);
            prevchange.add(item);
        }
    }

    void afterCancel() {
        def changeLog;
        optionList.each{ o->
            changeLog = o.opener.handle.changeLog;
            if (changeLog != null && changeLog?.hasChanges()) {
                changeLog.undoAll();
                changeLog.clear();
            }
        }

        entity.cbs = [];
        entity.cbs.addAll(prevcbs);

        if (entity._addedcbs) entity._addedcbs = [];
        if (entity._removedcbs) entity._removedcbs = [];

        entity.references = [];
        entity.references.addAll(prevreferences);

        if (entity._addedreferences) entity._addedreferences = [];
        if (entity._removedreferences) entity._removedreferences = [];

        entity.encashments = [];
        entity.encashments.addAll(prevencashments);
        
        if (entity._addedencashments) entity._addedencashments = [];
        if (entity._removedencashments) entity._removedencashments = [];
        
        entity.change = [];
        entity.change.addAll(prevchange);

        resetOptionListOpener();
        optionsModel.reload();
        optionsModel.setSelectedIndex(selectedOption.index);
    }
   
    void resetOptionListOpener() {
        optionList.each{ o->
            o.opener = Inv.lookupOpener("encashment:"+o.name, [entity: entity, allowEdit: (mode != 'read'? true : false)]);
        }
        optionList.sort{ it.index }
    }

    void resetOptionList() {
        optionList = [];
        def list = Inv.lookupOpeners('encashment-plugin');
        def props;
        list?.each{ o->
            props = o.properties;
            if (props) {
                optionList.add(props);
            }
        }
        //optionList = svc.getMainPageItems();
        resetOptionListOpener();
    }

    void submitForApproval() {
        if (!MsgBox.confirm('You are about to submit this document for approval. Continue?')) return;
        
        entity = service.submitForApproval(entity);
        allowEdit = false;
        resetOptionListOpener();
        optionsModel.reload();
        optionsModel.setSelectedIndex(selectedOption.index);
        /*
        if (MsgBox.confirm("You are about to submit this encashment for approval. Continue?")) {
            entity = service.submitForApproval(entity);
            allowEdit = false;
            resetOptionListOpener();
            optionsModel.reload();
            optionsModel.setSelectedIndex(selectedOption.index);
        }
        */
    }

    void approveDocument() {
        if (!MsgBox.confirm('You are about to approve this document. Continue?')) return;
        
        entity = service.approveDocument(entity);
        resetOptionListOpener();
        optionsModel.reload();
        optionsModel.setSelectedIndex(selectedOption.index);
        /*
        if (MsgBox.confirm("You are about to approve this encashment. Continue?")) {
            entity = service.approveDocument(entity);
            resetOptionListOpener();
            optionsModel.reload();
            optionsModel.setSelectedIndex(selectedOption.index);
        }
        */
    }

    void disapprove() {
        if (!MsgBox.confirm('You are about to disapprove this document. Continue?')) return;
        
        entity = service.disapprove(entity);
        resetOptionListOpener();
        optionsModel.reload();
        optionsModel.setSelectedIndex(selectedOption.index);
        /*
        if (MsgBox.confirm("You are about to disapprove this encashment. Continue?")) {
            entity = service.disapprove(entity);
            resetOptionListOpener();
            optionsModel.reload();
            optionsModel.setSelectedIndex(selectedOption.index);
        }
        */
    }

    def optionsModel = [
        getItems: {
            if (!optionList) resetOptionList();
            return optionList;
        }, 
        onselect: { o->
            opener = o.opener;
            opener?.handle?.refresh();
            binding.refresh('opener');
        }
    ] as ListPaneModel;

    def close() {
        return "_close";
    }
    
}