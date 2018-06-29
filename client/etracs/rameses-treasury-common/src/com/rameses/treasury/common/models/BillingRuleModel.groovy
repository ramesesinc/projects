package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;

public class BillingRuleModel extends AbstractRuleProcessorModel  {

    @Service("BillingRuleService")    
    def ruleExecutor;

    //by default retrieve the billitems
    boolean include_billitems = true;

    //set this true for cash receipts 
    boolean include_items = false;

    void init() {
        options = [:];
        options.include_billitems = include_billitems;
        options.include_items = include_items; 
        super.init();
    }

}