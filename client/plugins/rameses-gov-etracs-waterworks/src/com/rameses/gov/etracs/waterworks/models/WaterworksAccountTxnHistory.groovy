package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*
import java.rmi.server.UID;

class WaterworksAccountTxnHistory {

   @Service('WaterworksAccountService')
   def svc;

   def txns;
   def title;
   def entity;

   void init(){
       println 'id : ' + entity.objid;
       title = "Transaction History";
       txns = svc.getConsumption(entity);
       println 'txns : ' + txns;
   }

   def selectedItem;
   def tableHandler = [
        fetchList: {
            return txns;
        }
   ] as BasicListModel;

}