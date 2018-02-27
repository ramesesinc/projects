package com.rameses.enterprise.accounting.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class JevModel  extends CrudFormModel { 

   String printFormName = 'jev';
    
   def itemListModel = [
       fetchList : { o->
           def m = [_schemaname:'jevitem'];
           m.findBy = [jevid: entity.objid ];
           def list = queryService.getList( m ); 
           list.each {
               if(it.side == "CR" ) it.acctname = (" "*10) + it.acctname;
           }
           def dr = list.sum{ it.dr };
           def cr = list.sum{ it.cr };
            
           list.add( [ acctname: (" "*10) + ' ------- T O T A L S ------- ', dr: dr , cr: cr ] ); 
           return list; 
       }
   ] as BasicListModel;
} 