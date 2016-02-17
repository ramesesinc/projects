package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*
import java.rmi.server.UID;

class WaterworksConsumptionCapture {

   @Service("DateService")
   def dtSvc;

   @Service("WaterworksComputationService")
   def computeSvc;

   @Service("WaterworksAccountService")
   def acctSvc;

   def entity;
   def title;
   def months;
   def handler;

   void init(){
       title = "Capture Consumption";
       if(entity) entity.year = dtSvc.getServerYear();
       months = [
            [id: 1, name: "JANUARY"],
            [id: 2, name: "FEBRUARY"],
            [id: 3, name: "MARCH"],
            [id: 4, name: "APRIL"],
            [id: 5, name: "MAY"],
            [id: 6, name: "JUNE"],
            [id: 7, name: "JULY"],
            [id: 8, name: "AUGUST"],
            [id: 9, name: "SEPTEMBER"],
            [id: 10, name: "OCTOBER"],
            [id: 11, name: "NOVEMBER"],
            [id: 12, name: "DECEMBER"]
        ];
   }

   def save(){
       if(!MsgBox.confirm('You are about to create this record. Continue?')) return;
       entity.account = [objid : entity.objid];
       entity.volume = entity.reading - entity.prevreading;
       entity.month = entity.selectedMonth.id;
       if(entity.volume < 1) throw new Exception("Last reading must be greater than previous reading!");
       def bill = computeSvc.compute(entity);
       entity.readingmethod = 'CAPTURE';
       entity.amount = bill.total + entity.balance;
       entity.amtpaid = 0.00;
       acctSvc.createConsumption(entity);
       MsgBox.alert("Record successfully saved!");
       handler();
       return '_close';
   }
}