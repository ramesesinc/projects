package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;

public class CollectionRuleModel extends com.rameses.treasury.common.models.AbstractRuleProcessorModel  {

    @Service("CollectionRuleService")    
    def ruleExecutor;
    
}