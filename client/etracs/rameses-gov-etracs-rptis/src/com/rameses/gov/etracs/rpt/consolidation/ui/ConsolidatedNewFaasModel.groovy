package com.rameses.gov.etracs.rpt.consolidation.ui;


import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class ConsolidatedNewFaasModel
{
    @Service('FAASService')
    def faasSvc;

    @Service('LGUService')
    def lguSvc;
    
    @Service('Var')
    def var;

    @Binding
    def binding;
    
    def svc;
    def entity;
    def faas;
    def info;
    def oncreate = {};
    def mode = 'read';

    void init(){
        info = [:]
    }

    void edit() {
        mode = 'edit'
    }

    void create() {
        entity._info = info;
        def data = svc.createConsolidatedFaas(entity)
        entity.putAll(data.consolidation);
        oncreate(entity);
        mode = 'read'
    }

    boolean getAllowEdit() {
        if (entity.state.matches('APPROVED')) return false;
        if (entity.state.matches('.*assign.*')) return false;
        if (!entity.taskstate.matches('.*taxmapper.*')) return false;
        if (entity.assignee.objid != OsirisContext.env.USERID) return false;
        return true;
    }
    

    /*========================================
    * PIN SUPPORT   
    *========================================*/
    
    def pinTypes = ['new', 'old']
    
    @PropertyChangeListener
    def listener = [
        'info.rputype' : {
            info.barangay = null;
            info.rp = null;
            info.suffix = null;
            if (info.rputype == 'land')
                info.suffix = 0
            info.isection = null;
            info.iparcel = null;
            
        },
        
        'info.pintype' :{
            info.isection = null;
            info.iparcel = null;
        },
        
        'info.*' : { buildPin(); }
    ] 
        
    void buildPin(){
        RPTUtil.buildPin(info, var);
        binding?.refresh('info.fullpin');
    }
    
    def getLgus(){
        return lguSvc.getLgus();
    }

    def getBarangays(){
        if (!info.lgu)
            return [];
        return lguSvc.getBarangaysByParentId(info.lgu.objid);
    }        
}
