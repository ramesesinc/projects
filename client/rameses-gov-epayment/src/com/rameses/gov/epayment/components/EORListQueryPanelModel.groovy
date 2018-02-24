package com.rameses.enterprise.treasury.components;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.common.ComponentBean;

/****
* This component is used in the ff. cases:
*     Billing Panel in Account
*     Cash Receipt payment
*     Capture Amount Payment
*      
*     getValue() here refers to the entity 
*/
public class EORListQueryPanelModel extends ComponentBean {

   def itemList = ["noe", "two", "three"]
    
}