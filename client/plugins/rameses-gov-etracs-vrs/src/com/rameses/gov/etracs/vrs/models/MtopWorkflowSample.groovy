package com.rameses.gov.etracs.vrs.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.wf.models.*;

public class MtopWorkflowSample extends WorkflowModel {
    
    String title = "Sample Data";
    
    def entity = [objid:'T1'];
    
    String processname = 'mtop';
    
    String getRefid() {
        return entity.objid;
    }
    
    def open() {
        return super.loadTask();
    }
    
    void beforeSignal(def p) {
        if( p.to == 'approval' ) {
            p.properties  = [p1:"BANANA", p2: "WATUSI"];
        }
        else if( p.to == 'release' ) {
            p.properties  = [p1:"RELEASE DO", p2: "WORNER"];
        }
    }

    
}