package com.rameses.enterprise.accounting.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class JevModel  extends CrudFormModel { 

   String printFormName = 'jev';
   
   def fixSubList( def list ) {
       def subList = list.findAll{ it.itemrefid };
       def newList = list.findAll{ !it.itemrefid };
       def arr = [];
       newList.each { v->
           arr << v;
           def sl = subList.findAll{ it.itemrefid == v.objid  };
           if( sl ) {
               sl.each { sv ->
                   sv.acctname = (" "*8) + sv.acctname;
                   arr << sv;
               }
           }
       }
       return arr;
   } 
    
   def itemListModel = [
       fetchList : { o->
           def m = [_schemaname:'jevitem'];
           m.findBy = [jevid: entity.objid ];
           def list = queryService.getList( m ); 
           
           def drList = [];
           def crList = [];
           list.each {
               if( it.dr > it.cr ) {
                   drList << it;
               }
               else {
                   it.acctname = (" "*8) + it.acctname;
                   crList << it;
               }
           }
           def dr = drList.sum{ it.dr };
           def cr = crList.sum{ it.cr };
           def flist = fixSubList(drList) + fixSubList(crList);
           flist.add( [ acctname: (" "*8) + ' ------- T O T A L S ------- ', dr: dr , cr: cr ] ); 
           return flist; 
       }
   ] as BasicListModel;
   
   def viewRef() {
       def op = Inv.lookupOpener(entity.reftype + ":open", [entity: [objid: entity.refid] ] );
       op.target = "popup";
       return op;
   } 
    
   def post() {
       def h = { o->
           entity.putAll(o);
           binding.refresh();
       }
       return Inv.lookupOpener("jevno_entry", [entity: [objid: entity.objid ], handler: h])
   } 
    
} 