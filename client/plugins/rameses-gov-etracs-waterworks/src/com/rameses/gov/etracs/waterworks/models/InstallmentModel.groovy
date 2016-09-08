package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class InstallmentModel extends CrudFormModel {

    @Service('DateService')
    def dateSvc; 
    
    def txntypelist = [];
    def months = [];

    void afterInit() { 
        def m = [ _schemaname: 'waterworks_txntype', findBy:[ ledgertype:'waterworks_installment' ] ];
        txntypelist = queryService.getList( m )*.objid; 
        months = dateSvc.getMonths(); 
    }

    void afterCreate() {
        entity.acctid = caller.masterEntity.objid;
    }
        
    def numFormat = new java.text.DecimalFormat("#,##0.00");
    
    def getFormattedInstallmentAmount() {
        return numFormat.format( entity.installmentamount ); 
    }
    
    @PropertyChangeListener 
    def changelisteners = [
        "entity.(downpayment|term|amount)" : { o->
            entity.installmentamount = 0; 
            
            if( entity.downpayment == null ) return;
            if( !entity.amount ) return;
            if( !entity.term  ) return;
            try { 
                def amt = (entity.amount - entity.downpayment);
                if( amt > 0 ) {
                    entity.installmentamount =  amt / (entity.term * 1.0); 
                }
            } catch(e){
                println e.message;
            }
        }
    ]; 
    

}