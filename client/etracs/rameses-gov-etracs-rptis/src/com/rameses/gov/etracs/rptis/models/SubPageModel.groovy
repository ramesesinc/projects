package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.interfaces.SubPage;

public class SubPageModel implements SubPage
{
    @Binding 
    def binding;
    
    @Caller
    def caller;
    
    def faasmodel;
    def rpumodel;
    def entity;
    def mode = 'read';
    
    void init(){
        
    }
    
    void afterModeChanged(){
        
    }
    
    void calculateAssessment(){
        rpumodel.calculateAssessment();
    }    
        
    void modeChanged(String mode){
        this.mode = mode;
        afterModeChanged();
        binding?.refresh();
    }
    
    boolean getAllowEdit(){
        return faasmodel.allowEdit;
    }
    
    boolean getAllowEditPinInfo(){
        return faasmodel.allowEditPinInfo;
    } 
    
    boolean getAllowEditPrevInfo(){
        return faasmodel.allowEditPrevInfo;
    } 
    
    boolean getShowActions(){
        return faasmodel.showActions;
    }     
        
}
