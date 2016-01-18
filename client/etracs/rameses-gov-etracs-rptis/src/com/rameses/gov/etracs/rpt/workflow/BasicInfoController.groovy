package com.rameses.gov.etracs.rpt.workflow;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rpt.util.RPTUtil;

class BasicInfoController 
{
    @Binding 
    def binding;
    
    @Caller
    def caller;
    
    def entity;
    def callbacks;

    
    void init(){
    }

    void calculateAssessment() {
        callbacks.calculateAssessment();
        binding.refresh('.*');
    }

    
    boolean getAllowEdit(){
        return callbacks.allowEdit();
    }
    
    boolean getAllowEditPinInfo(){
        return callbacks.allowEditPinInfoCallback();
    } 
    
    boolean getAllowEditPrevInfo(){
        return callbacks.allowEditPrevInfoCallback();
    } 
    
    boolean getShowActions(){
        return callbacks.showActions();
    } 
}    
    