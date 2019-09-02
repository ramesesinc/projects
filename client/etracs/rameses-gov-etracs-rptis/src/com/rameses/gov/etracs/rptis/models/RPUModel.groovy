package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.*; 
import com.rameses.rcp.common.*; 
import com.rameses.osiris2.client.*; 
import com.rameses.osiris2.common.*; 
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.gov.etracs.rptis.interfaces.SubPage;

public class RPUModel extends SubPageModel
{
    @Service('RPUService')
    def rpuSvc; 
    
    def openers = [];
    
    void init(){
    }    
    
    void modeChanged(String mode){
        super.modeChanged(mode);
        openers.each{
            ((SubPage)it.handle).modeChanged(mode);
        }
    }

    def tabHandler = [
        getOpeners : {
            def type = 'rpu' + entity.rpu.rputype + ':info'
            openers = InvokerUtil.lookupOpeners(type, [
                    entity      : entity, 
                    rpuSvc      : rpuSvc,
                    faasmodel   : faasmodel,
                    rpumodel    : this,
            ]);
            return openers;
        }
    ] as TabbedPaneModel;
    
    void recalculate(){
        calculateAssessment();
    }
    
    void calculateAssessment(){
        //TODO: 
        if (isAllowModify()) {
            entity.rpu.txntype = entity.txntype;
            entity.rpu.effectivityyear = entity.effectivityyear;
            entity.rpu.putAll( rpuSvc.calculateAssessment(entity.rpu) )
            modeChanged(entity.state);
        }
    }

    def isAllowModify() {
        if (entity._modify_) return true;
        if (entity.state.matches('CURRENT|CANCELLED')) return false;
        if (RPTUtil.toBoolean(entity.datacapture, false) == true) return true;
        if (getAllowEdit()) return true;
        return false;
    }
    
}