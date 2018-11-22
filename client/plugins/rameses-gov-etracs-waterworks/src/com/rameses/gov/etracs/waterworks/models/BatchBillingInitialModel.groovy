package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.report.*;
import com.rameses.rcp.framework.*;
import java.text.*;
import com.rameses.util.*;

public class BatchBillingInitialModel extends CrudFormModel {
    
   @Service("WaterworksBatchBillingService")
   def batchSvc;
    
   @Service("DateService")
   def dateSvc;

   @FormTitle
   String title = "Batch Billing";
    
   def selectedItem;
   def listHandler;
   def query = [:];
    
   void afterCreate() {
       entity.year = dateSvc.getServerYear();
       entity.month = dateSvc.getServerMonth();
       query.yearmonth = getFilterValue();
   } 
    
   public def getFilterValue() {
       return (entity.year * 12)+entity.month;
   } 
    
   @PropertyChangeListener
   def listener = [
       "entity.(year|month)" : { o->
            query.yearmonth = getFilterValue();
            listHandler.reload();
       }
   ];
   
   void processBill() {
       if(!selectedItem) throw new Exception("Please select an item first");
       entity.zone = selectedItem;
       super.save();
       if( !entity.state  ) return;
       create();
       listHandler.reload();
       MsgBox.alert("batch " + entity.objid + " was created");
   } 
    
    
}
