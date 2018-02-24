package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*

public class CashReceiptPartialModel {
    @Binding
    def binding
    
    def amount
    def partial = 0.0
    
    def onpartial //handler 
    
    void init() {
    }
    
    def ok() {
        if( partial <= 0 ) throw new Exception('Partial Payment must be greater than zero.')
        if( partial > amount ) throw new Exception('Partial Payment must not exceeed amount due.')
        if( partial == amount ) throw new Exception('Partial Payment must be less than amount due.')
        
        if( onpartial) onpartial( partial )
        return '_close' 
    }
    
    
}


