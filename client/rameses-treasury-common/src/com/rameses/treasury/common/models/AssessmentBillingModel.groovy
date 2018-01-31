package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*;
import com.rameses.util.*;

public class AssessmentBillingModel extends AbstractRuleProcessorModel  {

    @Service("AssessmentBillingService")    
    def ruleExecutor;
    
}