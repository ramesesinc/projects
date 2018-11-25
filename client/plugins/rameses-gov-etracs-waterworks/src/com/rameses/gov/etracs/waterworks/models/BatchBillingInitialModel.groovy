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
       query.where = "nextscheduleid IS NULL OR ((nextschedule.year * 12) + nextschedule.month <= :yearmonth)";
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
       def z = [_schemaname:schemaName];
       z.putAll( entity );
       z.objid = null;
       z = persistenceService.create(z);
       if( !z.objid  ) return;
       MsgBox.alert( "Batch " + z.objid +  " is created" );
       create();
       listHandler.reload();
   } 
    
    
}
