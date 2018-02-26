package com.rameses.gov.epayment.models;


import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
 
/*******************************************************************************
* This class is used for Rental, Other Fees and Utilities
********************************************************************************/
public class EORListModel extends CrudListModel  {

     def partner;
     
     def partnerList;
     
     void afterInit() {
         def m = [_schemaname: 'paymentpartner'];
         m.where = ["1=1"];
         partnerList = queryService.getList(m);
         if(partnerList) partner = partnerList[0];
     }
    
     @PropertyChangeListener
     def listener = [
         "partner" : {
            reload();
         }
     ]
    
     def getCustomFilter() {
         return ["partnerid =:partnerid", [partnerid: partner.objid ] ];
     }   
    
    
}