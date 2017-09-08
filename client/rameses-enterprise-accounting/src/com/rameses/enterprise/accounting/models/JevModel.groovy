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
           def drlist = entity.items.findAll{ it.dr > 0 };
           def crlist = entity.items.findAll { it.cr > 0 };
           entity.items = drlist + crlist;
           return entity.items; 
       }
        
   ] as BasicListModel;
} 