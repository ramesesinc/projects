package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;


public class BatchBillingModel extends CrudFormModel {
    
   @Service("WaterworksComputationService")
   def compSvc;
    
   @Service("WaterworksBatchBillingService")
   def batchSvc;
    
    
   def selectedItem;
   def getQuery() {
        return [batchid: entity.objid];
   } 
    
   def updateHandler = [
        beforeColumnUpdate: { item, colName, value ->
            if(colName.matches("reading|volume")) return false;
            try {
                def r = [:];
                if(colName == "reading") {
                    def p = [:];
                    p.volume = value - item.prevreading;
                    if(p.volume<0) throw new Exception("Current reading must be greater than prev reading");
                    p.objid = item.acctid; 
                    r.volume = p.volume;
                    r.amount = compSvc.compute(p);
                }
                else if(colName == "volume") {
                    def p = [:];
                    p.objid = item.acctid; 
                    r.volume = value;
                    r.amount = compSvc.compute(p);
                }

                def m = [_schemaname: 'waterworks_billing'];
                m[(colName)] = value;
                m.putAll( r );
                m.findBy = [objid: item.objid ];
                persistenceService.update( m );
                item.volume = m.volume;
                item.amount = m.amount;
                return true;
            }
            catch(e) {
                MsgBox.err(e);
                return false;
            }
        }
    ];
    
   public void post() {
       if( !MsgBox.confirm("You are about to finalize this transaction for billing. Proceed?")) return;
       batchSvc.post( [objid: entity.objid ]);
   } 
    
}