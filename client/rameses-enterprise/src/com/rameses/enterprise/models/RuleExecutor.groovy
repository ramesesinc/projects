package com.rameses.enterprise.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

/***
* This class runs the rules and displays info if necessary. 
* The ruleExecutor is passed
* ex: def r = new RuleExecutor(executor)
*     def result = r.execute( params );
****/
public class RuleExecutor {
    
    def ruleExecutor;
    
    public RuleExecutor(def r ) {
        ruleExecutor = r;
    }
    
    public def execute(def params) {
        def result = ruleExecutor(params);
        if( result.askinfos ) {
            def rh = { r->
                result = r;
            }
            Modal.show( "ask-infos", [infos: result.askinfos, resultHandler: rh ]);
        }
        return result;
    }
    
}