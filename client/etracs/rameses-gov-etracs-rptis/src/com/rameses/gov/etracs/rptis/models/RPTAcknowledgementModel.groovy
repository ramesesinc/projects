package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

public class RPTAcknowledgementModel extends CrudFormModel
{
    @Service('FAASService')
    def faasSvc;

    @Service('DateService')
    def dtSvc;

    @Service('Var')
    def var;

    @Service('RPTTrackingService')
    def trackingSvc;

    @Service('PersistenceService')
    def persistence;

    boolean showConfirm = false;
    def selectedItem;
    
    def types;
    
    def getTxntypes() {
        if (!types) {
            types = faasSvc.getTxnTypes();
        }
        return types;
    }
    
    def getReleaseModes() {
        return LOV.RPT_RELEASING_MODES*.key
    }
    
    public void afterCreate(){
        entity.state = 'DRAFT';
        entity.releasedate = calcReleaseDate();
        entity.receivedby = OsirisContext.env.FULLNAME;
    }

    def calcReleaseDate() {
        def numdays = var.getProperty('assessor_acknowledgement_receipt_numdays', 9);
        return (dtSvc.getServerDate() + numdays);
    }

    void approve() {
        if (MsgBox.confirm('Approve?')) {
            entity.state = 'APPROVED';
            persistence.update(entity);
            reload();
        }
    }

    public def getPrintFormData() {
        entity.landcount = entity.items.findAll{it.faas.rputype == 'land'}.size()
        entity.bldgcount = entity.items.findAll{it.faas.rputype == 'bldg'}.size()
        entity.machcount = entity.items.findAll{it.faas.rputype == 'mach'}.size()
        entity.planttreecount = entity.items.findAll{it.faas.rputype == 'planttree'}.size()
        entity.misccount = entity.items.findAll{it.faas.rputype == 'misc'}.size()
        return entity; 
    }
    
    def getLogs() {
        if (selectedItem == null || !selectedItem.newfaas) {
            return [];
        } else {
            return trackingSvc.getLogs(selectedItem.newfaas);
        }
    }
    
    def logListHandler = [
        fetchList: { getLogs() },
    ] as BasicListModel
}


