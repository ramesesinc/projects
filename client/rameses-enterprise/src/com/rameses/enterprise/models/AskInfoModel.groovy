package com.rameses.enterprise.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;

/***
* This class runs in conjunction with RuleExecutor. 
* This displays the info
* 
****/
public class AskInfoModel {
    
    def ruleExecutor;
    def infoStack = new Stack();
    def infos;
    def resultHandler;
    
    def formInfos = [];
    
    void init() {
        if(!ruleExecutor) throw new Exception("ruleExecutor is required in AskInfoModel");
        if(!resultHandler) throw new Exception("resultHandler is required in AskInfoModel");
        buildFormInfos();
    }

    boolean getHasBack() {
        return !infoStack.empty();
    }
    
    def doNext() {
        //build all infos and send for processing...
        def pInfos = [];
        for( st in infoStack ) {
            pInfos.addAll( st );
        }
        pInfos.addAll( infos );
        
        def result = ruleExecutor( pInfos );
        if(!result.askinfos) {
            resultHandler( result );
        } 
        else {
            infoStack.push( result.askinfos );
            infos = r.askinfos;
            buildFormInfos();
            return null;
        }
    }
    
    def doBack() {
        infos = infoStack.pop();
        buildFormInfos();
        return null;
    }
    
    def doCancel() {
        return "_close";
    }
    
    def buildFormInfos() {
        formInfos.clear();
        infos = infos.sort{ [it.category, it.sortorder] }
        infos.each {x->
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