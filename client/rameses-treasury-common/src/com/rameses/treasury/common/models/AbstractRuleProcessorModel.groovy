package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;

public abstract class AbstractRuleProcessorModel  {

    abstract def getRuleExecutor();
    
    def handler;
    
    String rulename;
    def infoStack = new Stack();
    def infos;
    def defaultInfos;
    def params;
    def formInfos = [];
    
    void init() {
        if(!rulename) throw new Exception("Please specify rulename in ruleprocesssor");
        if(!handler) throw new Exception("handler is required in ruleprocessor");
        if(params == null) throw new Exception("params is required in RuleExecutor"); 
        
        //we must first clear this when doing initial rules
        params.infos = [];
        def result = ruleExecutor.execute([rulename:rulename, params:params]);
        
        if(result.askinfos) {
            infos = result.askinfos;
            buildFormInfos();
        }
        else {
            handler( result );
            throw new BreakException();
        }
    }

    //comprator is overridable. By default it compares two names
    def comparator = {item,bean-> (item.category==bean.category) && (bean.name == item.name) };

    public def getDefaultValue( def item ) {
        if( defaultInfos) {
            def rv = defaultInfos.find{ comparator(it, item) };
            if( rv ) return rv.value;
        }
        else if( item.value !=null ) {
            return item.value;
        }
        return item.defaultvalue;
    }
    
    boolean getHasBack() {
        return !infoStack.empty();
    }
    
    def createStackedInfos() {
        def pInfos = [];
        for( st in infoStack ) {
            pInfos.addAll( st );
        }
        pInfos.addAll(infos);
        return pInfos;
    }
    
    def doNext() {
        //build all infos and send for processing...
        def p = [:];
        p.putAll(params);
        p.infos = createStackedInfos();
        def result = ruleExecutor.execute( [rulename:rulename, params:p] );
        if(result.askinfos) {
            if(infos) infoStack.push( infos );
            infos = result.askinfos;
            buildFormInfos();
            return null;
        }
        else {
            //This is the ending
            def infos = createStackedInfos();
            if( result.infos ) {
                infos.addAll( result.infos );
            }	            
            result.infos = infos.unique();
            handler( result );
            return "_close";
        }
    }
    
    def doBack() {
        if(infoStack.empty()) return "_close";
        infos = infoStack.pop();
        //println infoStack.size();
        buildFormInfos();
        return null;
    }
    
    def doCancel() {
        infoStack.clear();
        if(infos) infos.clear();
        handler(null);
        return "_close";
    }
    
    def buildFormInfos() {
        formInfos.clear();
        infos = infos.sort{ it.sortorder }
        infos.each {x->
            x.value = getDefaultValue( x );
            def i = [
                type:x.datatype, 
                caption:x.caption, 
                categoryid: x.category,
                name:x.name, 
                bean: x,
                required: true,
                properties: [:],
                value: x.value
            ];
            //fix the datatype
            if(x.datatype.indexOf("_")>0) {
                x.datatype = x.datatype.substring(0, x.datatype.indexOf("_"));
            }
            if(i.type == "boolean") {
                i.type = "yesno";
            }
            else if(i.type == "string_array") {
                i.type = "combo";
                i.preferredSize = '150,20';
                i.itemsObject = x.arrayvalues;
            }
            else if( i.type == 'decimal' ) {
                i.preferredSize = '150,20';
            }
            else if( i.type == 'integer' ) {
                i.preferredSize = '150,20';
            }
            else if( i.type == "string" ) {
                i.type = "text";
            }
            formInfos << i;
        }
     }
     
     def formPanel = [
        updateBean: {name,value,item->
            item.bean.value = value;
        },
        getControlList: {
            return formInfos;
        }
    ] as FormPanelModel;
        
    
}