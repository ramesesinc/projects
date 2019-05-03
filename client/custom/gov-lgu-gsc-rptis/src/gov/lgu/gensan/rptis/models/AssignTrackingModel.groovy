package gov.lgu.gensan.rptis.models;
        
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.common.*;

public class AssignTrackingModel 
{
    @Caller
    def caller;
    
    @Invoker
    def invoker;
    
    @Service('GensanAcknowledgementService')
    def svc;
    
    def entity;
    def tracking;
    
    def getRefno() {
        def refno = entity[invoker.properties.fieldName];
        if (!refno) refno = 'NEW';
        return refno;
    }
    
    void init() {
        tracking = [reftype: invoker.properties.refType];
        tracking.refno = getRefno();
        tracking.ref = [objid: entity.objid];
    }
    
    def assign() {
        svc.assignTracking(tracking);
        entity.trackingno = tracking.trackingno;
        caller.binding.refresh();
        return '_close';
    }
}


