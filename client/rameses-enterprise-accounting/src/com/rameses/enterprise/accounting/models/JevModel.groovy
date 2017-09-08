package com.rameses.enterprise.accounting.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class JevModel  extends CrudFormModel { 

   def itemListModel = [
       fetchList : { o->
           def drlist = entity.items.findAll{ it.dr > 0 };
           def crlist = entity.items.findAll { it.cr > 0 };
           return drlist + crlist; 
       }
        
   ] as BasicListModel;

   def viewReport() {
       def rh = [
           getReportName : { 
               return "com/rameses/enterprise/accounting/reports/jev.jasper";
           },
           getData : {
               return entity;
           } 
       ]
       return Inv.lookupOpener("simple_form_report", [reportHandler:rh, title:'Jev Report']) 
   }  
   
    
} 