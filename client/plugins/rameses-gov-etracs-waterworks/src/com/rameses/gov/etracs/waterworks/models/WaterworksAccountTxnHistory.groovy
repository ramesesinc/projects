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
       title = "Transaction History";
       txns = svc.getTxnHistory(entity);
       txns.each{ println it; }
   }

   def capture(){
        def handler = {
            txns = svc.getConsumption(entity);
            tableHandler.reload();
        }
        return Inv.lookupOpener("waterworks_consumption:capture",[entity:[acctid:entity.objid, classificationid:entity.classificationid], handler:handler]);
   }

   def selectedItem;
   def tableHandler = [
        fetchList: {
            return txns;
        }
   ] as BasicListModel;

}