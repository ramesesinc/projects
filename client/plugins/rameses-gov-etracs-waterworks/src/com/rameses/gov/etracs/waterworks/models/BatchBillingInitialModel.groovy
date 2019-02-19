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

   @PropertyChangeListener
   def listener = [
       "entity.zone" : { o->
            if ( o.nextschedule?.objid ) {
                entity.year = o.nextschedule?.year;
                entity.month = o.nextschedule?.month;
                entity.readingdate = o.nextschedule?.readingdate;
            }
            else {
                entity.year = dateSvc.getServerYear();
                entity.month = dateSvc.getServerMonth();
                entity.readingdate = null; 
            }
       }
   ];
    
   def processBill() {
       def z = [_schemaname:schemaName];
       z.putAll( entity );
       z.objid = null;
       z = persistenceService.create(z);
       if( !z.objid  ) return;
       
       MsgBox.alert( "Batch " + z.objid +  " is created" );
       
       def op = Inv.lookupOpener("waterworks_batch_billing:open", [entity: [objid:z.objid]]); 
       op.target = "topwindow";
       return op;
   } 
    
    
}
