package com.rameses.clfc.borrower;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.borrower.*;
import com.rameses.clfc.util.*;

class BorrowerGeneralInfoController 
{
    //feed by the caller
    def borrowerContext;
    
    //local variables 
    def entity = [:];
    def occupancyTypes = LoanUtil.borrowerOccupancyTypes;
    def rentTypes = LoanUtil.rentTypes;
    
    @ChangeLog
    def changeLog;
    
    @PropertyChangeListener
    def listener = [
        "entity.residency.type": {o->
            if(o != 'RENTED') {
                entity.residency.renttype = null
                entity.residency.rentamount = null
            }
        },
        "entity.occupancy.type": {o->
            if(o != 'RENTED') {
                entity.occupancy.renttype = null
                entity.occupancy.rentamount = null
            }
        }
    ];
    
    void init() {
        entity = borrowerContext.borrower;
        borrowerContext.addBeforeSaveHandler('borrower', {
            if(entity.residency.type == 'RENTED') {
                if(!entity.residency.renttype) throw new Exception('Residency: Rent Type is required.');
                if(!entity.residency.rentamount) throw new Exception('Residency: Rent Amount is required.');
            }
            if(entity.occupancy.type == 'RENTED') {
                if(!entity.occupancy.renttype) throw new Exception('Lot Occupancy: Rent Type is required.');
                if(!entity.occupancy.rentamount) throw new Exception('Lot Occupancy: Rent Amount is required.');
            }
        });
    }
        
    def getLookupBorrower() {  
        def params = [
            'query.loanappid': borrowerContext.loanappid, 
            onselect: {o-> 
                def borrower = null; 
                try { borrower = borrowerContext.openBorrower([objid: o.objid]); } catch(Throwable t){;} 
                
                if (borrower == null) { 
                    entity.putAll(o); 
                } else {
                    entity.clear(); 
                    entity.putAll(borrower);
                } 
                def address = entity.address;
                if (address instanceof Map) {
                    def addresstext = entity.address.text;
                    entity.address = addresstext; 
                }
                /*
                println 'address ' + entity.address;
                if (entity.address) {
                    def addresstext = entity.address.text;
                    entity.address = addresstext; 
                } 
                */
                borrowerContext.dataChangeHandlers.each{k,v-> 
                    if (v != null) v(); 
                }
                borrowerContext.refresh(); 
            }, 
            onempty: { 
                entity.clear();
                entity.occupancy = [:];
                entity.residency = [:];
                borrowerContext.refresh(); 
            }
        ];
        return InvokerUtil.lookupOpener('customer:search', params);
    }
}