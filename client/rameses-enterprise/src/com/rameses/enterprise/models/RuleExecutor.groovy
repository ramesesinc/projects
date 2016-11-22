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
* Info must be with the ff. fields:
*    name, datatype, caption, category   
****/
public class RuleExecutor {
    
    def ruleExecutor;
    
    public RuleExecutor(def r ) {
        ruleExecutor = r;
    }
    
    public def execute(def params) {
        if(params == null) throw new Exception("params is required in RuleExecutor"); 
        def result = ruleExecutor(params);
        if( result.askinfos ) {
            def p = [:];
            p.params = params;
            p.infos = result.askinfos;
            p.ruleExecutor = ruleExecutor;
            p.resultHandler = { r->
                result = r;
            }
            Modal.show( "ask-infos", p );
        }
        return result;
    }
    
}