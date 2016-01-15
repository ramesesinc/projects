package com.rameses.clfc.borrower;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.borrower.*;
import com.rameses.clfc.util.*;

class BorrowerSpouseInfoController 
{
    @Binding
    def binding;
    
    //feed by the caller
    def borrowerContext;
    
    //declare variables
    def entity = [:];
    def occupancyTypes = LoanUtil.borrowerOccupancyTypes;
    def rentTypes = LoanUtil.rentTypes;
        
    void init() {        
        initEntity();
        borrowerContext.addDataChangeHandler('spouseinfo', { initEntity() });
        borrowerContext.addBeforeSaveHandler('spouseinfo', {
            println 'entity.objid != null = '+(entity.objid != null);
            if(entity.objid != null) {
                if(entity.residency.type == 'RENTED') {
                    if(!entity.residency.renttype) throw new Exception('Residency: Rent Type is required.');
                    if(!entity.residency.rentamount) throw new Exception('Residency: Rent Amount is required.');
                }
                if(entity.occupancy.type == 'RENTED') {
                    if(!entity.occupancy.renttype) throw new Exception('Lot Occupancy: Rent Type is required.');
                    if(!entity.occupancy.rentamount) throw new Exception('Lot Occupancy: Rent Amount is required.');
                }
            }
        });
    } 
    
    void initEntity() {
        entity = borrowerContext.borrower.spouse;
        if (entity == null) {
            borrowerContext.borrower.spouse = [:]; 
            entity = borrowerContext.borrower.spouse;
        }
    }
    
    def getLookupBorrower() { 
        def params = [ 
            'query.loanappid': borrowerContext.loanapp.objid, 
            onselect: {o-> 
                entity.putAll(o); 
                if (entity.address) {
                    def addresstext = entity.address.text;
                    entity.address = addresstext; 
                } 
                binding.refresh('entity.residency.*|entity.occupancy.*');
            },
            onempty: {
                entity.clear();
                entity.occupancy = [:];
                entity.residency = [:];
                binding.refresh('entity.residency.*|entity.occupancy.*');
            }
        ]; 
        return InvokerUtil.lookupOpener('customer:search', params); 
    } 
}
