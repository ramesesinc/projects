package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;


public class BatchReadingModel extends CrudFormModel {
    
   @Service("WaterworksComputationService")
   def compSvc;
    
    @Service("WaterworksBatchReadingService")
   def batchReadingSvc;
    
    boolean updateItem( def item,  def colName, def value  ) {
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
            
            def m = [_schemaname: 'waterworks_consumption'];
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
    
   def selectedItem;
   def filterMap = [_limit:20, _start: 0];
    
   void  moveBackPage() {
        if( filterMap._start - 1 < 0 ) filterMap._start = 0;
        else filterMap._start = filterMap._start - 1;
        listHandler.reload();
   };
    
   void moveNextPage() {
        //if( filterMap._start + 1 > ) filterMap._start = 0;
        //else filterMap._start = filterMap._start - 1;
        filterMap._start = filterMap._start  + 1;
        listHandler.reload();
   };
       
    void moveFirstPage() {
        filterMap._start = 0;
        listHandler.reload();
    };
       
    int getPageIndex() {
         return filterMap._start; 
    };
    
   

   def listHandler = [
       fetchList: { o->
           def m = [_schemaname:'waterworks_consumption'];
           m.findBy = [batchid: entity.objid];
           m.putAll( filterMap );
           m.orderBy = "account.stuboutnode.indexno";
           return queryService.getList(m);
       },
       beforeColumnUpdate: { item, colName, newValue ->
           if(colName == "reading") {
               return updateItem( item, colName, newValue );
           }
           else if(colName == "volume") {
               return updateItem( item, colName, newValue );
           }
       },
       onOpenItem: { o,colName ->
           def popupMenu = new PopupMenuOpener();
           popupMenu.add( new Opener(caption:"disconnect"));
           popupMenu.add( new Opener(caption:"hold"));
           return popupMenu;
       }
   ] as EditorListModel;
   
    
   public void post() {
       if( !MsgBox.confirm("You are about to finalize this transaction for billing. Proceed?")) return;
       batchReadingSvc.post( [objid: entity.objid ]);
   } 
    
}