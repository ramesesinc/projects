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
   def months;

   void init(){
       title = "General Information";
       entity = caller.entity;
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
        months.each{
            if(it.id == entity.lastreadingmonth) entity.lastreadingmonth = it.name;
        }
   }
}