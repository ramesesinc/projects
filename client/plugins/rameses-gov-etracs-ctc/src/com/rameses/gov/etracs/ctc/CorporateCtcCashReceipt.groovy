package com.rameses.gov.etracs.ctc;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.enterprise.treasury.cashreceipt.*;

class  CorporateCtcCashReceipt extends AbstractCashReceipt {
    @Service('CorporateCTCService')
    def ctcSvc;
    
    @Service('PersistenceService') 
    def persistenceSvc;     

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
        entity.interest = 0;
        entity.interestdue = 0;
        entity.brgytaxshare = 0;
        entity.brgyinterestshare = 0;
    }
    
    public String getEntityType() {
        return "juridical";
    }
    
    public void validateBeforePost() {
        if ( ! entity.payer )
            throw new Exception('Payer is required.')
        if (needsrecalc)
            throw new Exception('Changes has been made. Recalculate tax before proceeding.')
    }
    
    boolean isAllowCreateEntity() {
        try { 
            def op = Inv.lookupOpener("juridicalentity:create", [:]); 
            return (op != null); 
        } catch(Throwable t) {
            return false; 
        }
    }
        
    def createEntity() { 
        def h = { o->
            o.type = 'JURIDICAL';
            entity.payer = o;
            entity.paidby = o.name;
            entity.paidbyaddress = o.address.text;
            binding.refresh("entity.(payer.*|paidby.*)");
            binding.refresh('createEntity|openEntity');
            payerChanged( o );
        }
        return Inv.lookupOpener("juridicalentity:create", [entity:[:], onselect:h]); 
    }

    public def payerChanged( o ) {
        if ( ! o.type.equalsIgnoreCase('JURIDICAL') )
            throw new Exception('Only Juridical entities are allowed.');
        
        def ent = persistenceSvc.read([ _schemaname: 'entityjuridical', findBy:[objid: o.objid]]); 
        if ( ent ) o.putAll(ent);
        
        hastin          = (o.tin != null);
        hasorgtype      = (o.orgtype != null);
        hasnature       = (o.nature != null);
        hasdtregistered = (o.dtregistered != null);
        hasplaceregistered = (o.placeregistered != null);
        
        hasbusinessinfo = false;
        haspropertyinfo = false;
        needsrecalc = true;
    
        payerdata = ctcSvc.getCtcRelatedPayerData(o);
        
        if ( payerdata.businessgross != null ) { 
            entity.newbusiness = payerdata.newbusiness.equalsIgnoreCase('NEW') 
            entity.businessgross = payerdata.businessgross; 
            orig_businessgross = entity.businessgross; 
            hasbusinessinfo = true; 
        } 
        
        if (payerdata.realpropertyav != null) { 
            entity.realpropertyav = payerdata.realpropertyav; 
            orig_realpropertyav = entity.realpropertyav; 
            haspropertyinfo = true; 
        } 
        entity.items = []; 
        updateBalances(); 
        binding.refresh('.*') 
    } 
    
    void calculateTax() { 
        entity.putAll( ctcSvc.calculateTax(entity) ); 
        updateBalances(); 
        needsrecalc = false; 
    } 
    
    List getOrgtypes(){
        return LOV.ORG_TYPES*.key
    } 
} 