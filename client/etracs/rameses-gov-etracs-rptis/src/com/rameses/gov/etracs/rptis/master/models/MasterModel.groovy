package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;

class MasterModel extends CrudFormModel
{
    @Service('RPTISMasterExportService')
    def svc
    
    def allowApprove = true;
    def allowDisapprove = true;
    def showOrderno = false;
    def showSpecial = false;
    boolean showConfirm = false;
    
    public void afterCreate(){
        entity.state = 'DRAFT';
    }
    
    def export(){
    }
    
    void approve(){
        if (MsgBox.confirm('Approve?')){
            entity._schemaname = schemaName;
            entity.state = 'APPROVED';
            persistenceService.update(entity);
            refreshCaller();
        }
    }
    
    void disapprove(){
        if (MsgBox.confirm('Disapprove?')){
            entity._schemaname = schemaName;
            entity.state = 'DRAFT';
            persistenceService.update(entity);
            refreshCaller();
        }
    }
    
    void refreshCaller(){
        try {
            caller?.refresh();
        }
        catch(ign){;}
    }
    
    boolean isEditAllowed() { 
        if (entity.state == 'APPROVED') return false;
        return super.isEditAllowed();
    }
}