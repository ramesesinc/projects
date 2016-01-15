package com.rameses.clfc.settings.branchloan

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class BranchLoanSettingsController extends CRUDController
{
    @Binding
    def binding;
    
    String serviceName = "BranchLoanSettingsService";
    String entityName = 'brancloansettings';
    String prefix = 'BLS';
    
    boolean allowDelete = false;
    boolean allowApprove = false;
    boolean allowEdit = true;
    
    void beforeCreate(data) {
        allowEdit = true;
        binding?.refresh('formActions');
    }
    
    void afterOpen( data ) {
        checkEditable(data);
    }
    
    void checkEditable( data ) {
        allowEdit = true;
        if (data.txnstate == 'ACTIVE') {
            allowEdit = false;
        }
        println 'allow edit ' + allowEdit;
        binding?.refresh('formActions');
    }
    
    void activate() {
        if (!MsgBox.confirm("You are about to activate this setting. Continue?")) return;
        
        entity = service.activate(entity);
        checkEditable(entity);
    }
    
    void deactivate() {
        if (!MsgBox.confirm("You are about to deactivate this setting. Continue?")) return;
        
        entity = service.deactivate(entity);
        checkEditable(entity);
    }
}

