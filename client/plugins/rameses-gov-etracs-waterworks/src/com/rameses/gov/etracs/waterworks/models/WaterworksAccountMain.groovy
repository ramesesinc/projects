package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*
import java.rmi.server.UID;

class WaterworksAccountMain {

   @Service('WaterworksAccountService')
   def svc;

   @Service("EntityService")
   def entitySvc;

   @Service('WaterworksClassificationService')
   def classSvc;

   @Service('WaterworksReadingGroupService')
   def readingGroupSvc;

   @Binding
   def binding;

   @FormId
   def formId;

   @FormTitle
   def formtitle;

   def entity; 
   def sections;
   def selectedSection;
   def title;

   void open(){
        entity = svc.open(entity);
        title = entity.acctno + " (" + entity.acctname + ")";
        formId = entity.objid;
        formtitle = entity.acctno;
        sections = Inv.lookupOpeners("waterworks_account:section",[entity:entity]);
   }

   def edit(){
        title = entity.acctno + " (" + entity.acctname + ")";
        return Inv.lookupOpener("waterworks_account:edit",[entity:entity])
   }

   void preview(){
       def list = readingGroupSvc.getListByAssignee([assigneeid:'USR-2e68d039:152b3cdd128:-7f7d']);
       list.each{
           println it;
           println '';
       }
   }

}