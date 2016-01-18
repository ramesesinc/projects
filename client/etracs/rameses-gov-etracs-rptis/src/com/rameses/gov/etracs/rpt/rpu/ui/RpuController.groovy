package com.rameses.gov.etracs.rpt.rpu.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 

public class RpuController extends com.rameses.gov.etracs.rpt.workflow.BasicInfoController
{
    @Service('RPUService')
    def rpuSvc; 
    
    def tabHandler = [
        getOpeners : {
            callbacks.calculateAssessment = calculateAssessmentCallback;
            def type = 'rpu' + entity.rpu.rputype + ':info'
            return InvokerUtil.lookupOpeners(type, [
                    entity      : entity, 
                    rpuSvc      : rpuSvc,
                    callbacks   : callbacks,
            ]);
        }
    ] as TabbedPaneModel;
    
    void recalculate(){
        calculateAssessmentCallback();
    }
    
    def calculateAssessmentCallback = {
        calculateAssessment();
    }    
    
    void calculateAssessment(){
        entity.rpu.dtappraised = entity.appraiser?.dtsigned;
        entity.rpu.putAll( rpuSvc.calculateAssessment(entity.rpu) )
    }
    
}