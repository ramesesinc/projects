package com.rameses.gov.etracs.ctc;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.enterprise.treasury.cashreceipt.*;

class  CorporateCtcCashReceipt extends AbstractCashReceipt 
{
    @Service('CorporateCTCService')
    def ctcSvc;
    
    @Service('JuridicalEntityService')
    def entitySvc 

    def payerdata  = [:];
    
    def hasbusinessinfo = false;
    def haspropertyinfo = false;
    
    def hastin = false;
    def hasorgtype = false;
    def hasnature = false;
    def hasdtregistered = false;
    def hasplaceregistered = false;
            
    def needsrecalc = false;
    
    def orig_realpropertyav = 0.0;
    def orig_businessgross = 0.0;
    
    @PropertyChangeListener
    def listener = [
        'entity.newbusiness' :{
            entity.businessgross = 0.0;
        },
            
        'entity.hasadditional' : { 
            if ( entity.hasadditional == true ) { 
                entity.additional = [:];  

            } else if ( entity.hasadditional != true ){
                entity.additional = null;
                entity.items = [];
            } 

            needsrecalc = true; 
            updateBalances();             
        },

        'entity.realpropertyav|entity.businessgross' :{
            needsrecalc = true;
        }
    ]
    
    
    void init(){
        super.init();
        entity.realpropertyav = 0.0;
        entity.businessgross = 0.0;
        entity.newbusiness = false;
        entity.hasadditional = false;
    }
    
    public void validateBeforePost() {
        if ( ! entity.payer )
            throw new Exception('Payer is required.')
        if (needsrecalc)
            throw new Exception('Changes has been made. Recalculate tax before proceeding.')
    }
        
    public def getPayerType() { 
        return 'entityjuridical'; 
    }     
    
    public def payerChanged( o ) {
        if ( ! o.type.equalsIgnoreCase('JURIDICAL') )
            throw new Exception('Only Juridical entities are allowed.');
        
        hastin          = (o.tin != null);
        hasorgtype      = (o.orgtype != null);
        hasnature       = (o.nature != null);
        hasdtregistered = (o.dtregistered != null);
        hasplaceregistered = (o.placeregistered != null);
        
        hasbusinessinfo = false;
        haspropertyinfo = false;
        needsrecalc = true;
    
        payerdata = ctcSvc.getCtcRelatedPayerData(o);
        
        if (payerdata.businessgross != null) {
            entity.newbusiness = payerdata.newbusiness.equalsIgnoreCase('NEW')
            entity.businessgross = payerdata.businessgross;
            orig_businessgross = entity.businessgross;
            hasbusinessinfo = true;
        }
        
        if (payerdata.realpropertyav != null){
            entity.realpropertyav = payerdata.realpropertyav;
            orig_realpropertyav = entity.realpropertyav;
            haspropertyinfo = true;
        }
        entity.items = [];
        updateBalances();
        binding.refresh('.*')
    }
    
        
    void calculateTax(){
        entity.putAll( ctcSvc.calculateTax(entity) )
        updateBalances();
        needsrecalc = false;
    }
    
    List getOrgtypes(){
        return LOV.ORG_TYPES*.key
    }    
}