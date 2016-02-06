package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*
import java.rmi.server.UID;

class WaterworksAccountGeneralInformation {

   @Caller
   def caller;

   def entity;
   def title;

   void init(){
       title = "General Information";
       entity = caller.entity;
       entity.each{k,v -> 
            println k + ' = ' + v;
       }
   }

   def selectedItem;
   def tableHandler = [
        fetchList: {
            return [];
        }
   ] as BasicListModel;

}