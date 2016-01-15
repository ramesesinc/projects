package com.rameses.clfc.treasury.dailycollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class DailyCollectionMainController extends CRUDController 
{
    @Binding
    def binding;
    
    @Service("DailyCollectionService")
    def svc;

    String serviceName = "DailyCollectionService";
    String entityName = "dailycollection";

    boolean allowCreate = false
    boolean allowDelete = false;
    boolean allowApprove = false;
    boolean allowEdit = true;
    
    Map editPermission = [domain: 'TREASURY', role: 'ACCT_ASSISTANT'];
   
    def optionList, selectedOption, opener;
    def prevcbs, prevoverages, prevshortages;
    def prevencashments, prevdepositslips;

    void afterOpen( data ) {
        allowEdit = data.allowEdit;
        binding?.refresh('formActions');
    }
    
    void afterEdit( data ) {
        resetOptionListOpener();
        optionsModel.reload();
        optionsModel.setSelectedIndex(selectedOption.index);

        def item;
        prevcbs = [];
        entity.cbs.each{ o->
            item = [:];
            item.putAll(o);
            prevcbs.add(item);
        }

        prevshortages = [];
        entity.shortages.each{ o->
            item = [:];
            item.putAll(o);
            prevshortages.add(item);
        }

        prevoverages = [];
        entity.overages.each{ o->
            item = [:];
            item.putAll(o);
            prevoverages.add(item);
        }

        prevencashments = [];
        entity.encashments.each{ o->
            item = [:];
            item.putAll(o);
            prevencashments.add(item);
        }

        prevdepositslips = [];
        entity.depositslips.each{ o->
            item = [:];
            item.putAll(o);
            prevdepositslips.add(item);
        }
    }


    void afterCancel() {
        entity.cbs = [];
        entity.cbs.addAll(prevcbs);

        entity.shortages = []
        entity.shortages.addAll(prevshortages);

        entity.overages = [];
        entity.overages.addAll(prevoverages);

        entity.encashments = [];
        entity.encashments.addAll(prevencashments);

        entity.depositslips = [];
        entity.depositslips.addAll(prevdepositslips);
        
        clear();
        resetOptionListOpener();
        optionsModel.reload();
        optionsModel.setSelectedIndex(selectedOption.index);
    }
    
    void afterSave( data ) {
        clear();
        resetOptionListOpener();
        optionsModel.reload();
        optionsModel.setSelectedIndex(selectedOption.index);
    }
    
    void clear() {
        entity._addedcbs = [];
        entity._removedcbs = [];
        entity._addedshortage = [];
        entity._removedshortage = [];
        entity._addedoverage = [];
        entity._removedoverage = [];
        entity._addedencashment = [];
        entity._removedencashment = [];
        entity._addeddepositslip = [];
        entity._removeddepositslip = [];
    }


    void resetOptionListOpener() {
        optionList.each{ o->
            o.opener = Inv.lookupOpener("dailycollection:"+o.name, [entity: entity, allowEdit: (mode != 'read'? true : false)]);
        }
        optionList.sort{ it.index }
    }

    void resetOptionList() {
        optionList = svc.getMainOptions();
        resetOptionListOpener();
    }
    
    def optionsModel = [
        getItems: {
            if (!optionList) resetOptionList();
            return optionList;
        }, 
        onselect: { o->
            opener = o.opener;
            binding.refresh('opener');
        }
    ] as ListPaneModel;

    def close() {
        return "_close";
    }
    
    void submitForVerification() {
        if (!MsgBox.confirm("You are about to submit this document for verification. Continue?")) return;
        
        entity = service.submitForVerification(entity);
    }
    
    def sendBack() {
        if (!MsgBox.confirm("You are about to send back this document. Continue?")) return;
        
        def handler = { remarks->
            try {
                entity = service.sendBack([objid: entity.objid, remarks: remarks]);
                allowEdit = entity.allowEdit;            
                EventQueue.invokeLater({
                     caller?.reload();
                     binding?.refresh();
                });
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        }
        return Inv.lookupOpener("remarks:create", [title: "Reason for Send Back", handler: handler]);
    }
    
    def viewSendback() {
        return Inv.lookupOpener("remarks:open", [title: "Reason for Send Back", remarks: entity.sendbackremarks]);
    }
    
    void verify() {
        if (!MsgBox.confirm("You are about to verify this document. Continue?")) return;
        
        entity = service.verify(entity);
    }
}