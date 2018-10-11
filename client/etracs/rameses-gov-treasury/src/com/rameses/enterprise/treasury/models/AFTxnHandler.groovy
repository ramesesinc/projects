package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.rcp.framework.ValidatorException;


class AFTxnHandler {

    @Binding
    def binding;

    @Invoker
    def invoker;
    
    @Service("AFTxnService")
    def svc;
    
    @Service("QueryService")
    def queryService;
    
    def entity;
    def handler;
    
    String title;
    
    def doClose() {
        handler.back();
        return "_close";
    }
    
    def doForward() {
        handler.forward();
        return "_close";
    }
    
    String getTitle() {
        return invoker.caption;
    }
    
}    