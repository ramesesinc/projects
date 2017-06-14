package com.rameses.gov.etracs.bpls.reports.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class BPDelinquencyBuilderModel {
    
    @Binding
    def binding;
    
    @Service('BPDelinquencyReportService') 
    def svc;
            
    String title = 'Build Business Tax Delinquency';
    
    def task;
    
    def msg;
    def mode;
    def entity = [:];
    
    def init(){ 
        def o = svc.getStatus(); 
        if ( o.pending ) { 
            return resolvePending( o.pending ); 
            
        } else { 
            entity = [:]; 
            msg = 'Click Build to rebuild the business tax delinquency listing.'; 
            mode = 'init';
            return 'default'; 
        } 
    } 
    
    def doBuild() { 
        def o = svc.getStatus(); 
        if ( o.pending ) {
            resolvePending( o.pending ); 
            if ( mode == 'completed' ) return 'pending'; 
            
        } else { 
            entity = svc.init([:]);             
        } 
        msg = 'Pending rebuild of business tax delinquency listing'; 
        mode = 'pending'; 
        return 'pending';
    } 
    
    void doRefresh() {
        def o = svc.open([ objid: entity.objid ]); 
        if ( !o ) return; 
        
        resolvePending( o ); 
    } 
    
    def doDelete() { 
        if ( MsgBox.confirm('You are about to delete this record. Continue?')) {
            svc.removeReport([ objid: entity.objid ]); 
            return init();             
        }
        return null; 
    }    
    
    def doApprove() { 
        if ( MsgBox.confirm('You are about to approve this record. Continue?')) {
            def o = svc.approve([ objid: entity.objid ]); 
            if ( o.state.toString().toUpperCase()=='APPROVED' ) {
                return '_close';  
            }             
        }
        return null; 
    }
    
    def resolvePending( o ) {
        entity = o; 
        if ( o.state.toString().toUpperCase() == 'COMPLETED' ) {
            msg = 'Rebuilding of business tax delinquency listing has been completed';  
            mode = 'completed'; 
        } else {
            msg = 'Pending rebuild of business tax delinquency listing'; 
            mode = 'pending';             
        }
        return 'pending'; 
    }
} 