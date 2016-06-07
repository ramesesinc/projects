package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class PaymentInstallmentModel {
    
    @Caller
    def caller; 
    
    @Service("PersistenceService")
    def svc;

    def entity = [:]; 
    
    @PropertyChangeListener 
    def changelisteners = [
        "entity.(downpayment|term)" : { 
            entity.installmentamount = null; 
            def dpay = (entity.downpayment ? entity.downpayment : 0.0);
            if ( entity.amount>0.0 && entity.term>0 ) {
                entity.installmentamount = (entity.amount - dpay) / entity.term; 
            } 
        },
        
        "entity.(startdate|term)" : { 
            entity.enddate = null; 
            if ( entity.startdate && entity.term>0 ) { 
                def dt = java.sql.Date.valueOf( entity.startdate ); 
                entity.enddate = DateUtil.add( dt, ''+ entity.term +'M' ); 
            } 
        } 
    ]; 
    
    void init() { 
        def selitem = caller?.selectedItem; 
        entity.acctid = caller?.getMasterEntity()?.objid; 
        entity.ledgerid = selitem?.objid; 
        entity.amount = selitem?.amount; 
        entity.downpayment = 0.0;
        entity.amtpaid = 0.0; 
        entity.state = 'OPEN';
    }
    
    def doOk() { 
        entity._schemaname = 'waterworks_installment'; 
        entity.particulars = '-';
        svc.create( entity );  
        return "_close"; 
    } 
    
    def doCancel() { 
        return "_close"; 
    } 
}