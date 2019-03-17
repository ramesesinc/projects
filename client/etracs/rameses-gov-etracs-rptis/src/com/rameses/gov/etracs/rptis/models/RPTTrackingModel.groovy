package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

public class RPTTrackingModel extends CrudFormModel
{
    @Service('FAASService')
    def faasSvc;
    
    def types;
    def refentity;
    def filetype;
    
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
        entity.objid = refentity?.objid;
        entity.filetype = filetype;
        entity.txntype = refentity.txntype;
        entity.pin = refentity.fullpin;
        entity.remarks = refentity.prevtdno;
        entity.landcount = 0;
        entity.bldgcount = 0;
        entity.machcount = 0;
        entity.planttreecount = 0;
        entity.misccount = 0;
    }
    
    def listHandler = [
        fetchList : { entity.logs }
    ] as BasicListModel;
    
}


