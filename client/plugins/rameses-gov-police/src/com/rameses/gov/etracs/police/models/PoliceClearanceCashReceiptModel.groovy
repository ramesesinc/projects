package com.rameses.gov.police.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.enterprise.treasury.cashreceipt.*;

public class PoliceClearanceCashReceiptModel extends AbstractCashReceipt { 
    
    @Service("PoliceClearanceCashReceiptService")
    def receiptSvc;
    
    @Service('QueryService') 
    def querySvc; 
    
    String title = "Police Clearance";
    
    def types = [];
    def accounts = [];
    
    @PropertyChangeListener 
    def listeners = [
        'entity.app.type': { o-> 
            updateAmounts(); 
        }, 
        'entity.totalcash': { o-> 
            entity.cashchange = 0.0; 
            if ( entity.totalcash > entity.amount ) {
                entity.cashchange = (entity.totalcash - entity.amount); 
            }             
            binding.refresh('entity.cashchange'); 
        }
    ]; 
     
    void init() { 
        def qparam = [_schemaname: 'collectiontype_account']; 
        qparam.findBy = [ collectiontypeid: entity.collectiontype?.objid ]; 
        accounts = querySvc.getList( qparam ); 
        if ( !accounts ) throw new Exception('No available accounts for this collection type'); 

        super.init();
        entity.app = [:]; 
        entity.items = []; 
        entity.totalnoncash = 0.0;
        completed = false;  
        
        accounts.collect{ it.tag }.findAll{( it )}.each{ types << it }
    }    
    
    public void validateBeforePost() { 
        if ( ! entity.payer ) throw new Exception('Payer is required.'); 
    } 
    
    public String getEntityType() {
        return "entityindividual";
    }
    
    void updateAmounts() { 
        entity.items.clear(); 
        entity.amount = entity.totalcash = entity.cashchange = 0.0; 
        def o = accounts.find{( it.tag == entity.app?.type )} 
        if ( !o?.defaultvalue ) throw new Exception('Please specify a default value for this type');
        
        entity.amount = o.defaultvalue; 
        binding.refresh('entity.(amount|totalcash|cashchange)');  

        entity.items << [ 
            receiptid: entity.objid, 
            amount: entity.amount, 
            item: o.account 
        ]; 
    }

} 
