package com.rameses.gov.etracs.bpls.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.rmi.server.UID;

/************************************************************
* This fires the rule and loops until there is no data found
*************************************************************/
class BusinessInfoPanelModel extends ComponentBean {
       
    @Service("BPInfoRuleService")
    def infoService;
            
    @Service("BusinessAssessmentService")
    def assessmentService;
    
    def handler;
    def formControls = [];
    def mode = "info";      //info or assessment.
    def stack = new Stack();
    
    //info is the beginning record;
    def getParams() {
        return getValue();
    }
    
    void setParams( def ent ) {
        setValue( ent );
    }

    def formPanel = [
        getCategory: { key->
            if(!key) return "";
            def lobname = params.lobs.find{ it.lobid == key }?.name    
            return ((lobname) ? lobname : key);
        },
        updateBean: {name,value,item->
            println "update "+name+" value " + value;
            item.bean.value = value;
        },
        getControlList: {
            return formControls;
        }
    ] as FormPanelModel;
    
    def findValue( info ) {
        def existingInfos = [];
        stack.each {
            existingInfos.addAll(it);
        }
        if(info.lob?.objid!=null) {
            def filter = existingInfos.findAll{ it.lob?.objid!=null };
            def m = filter.find{ it.lob.objid==info.lob.objid && it.attribute.objid == info.attribute.objid };
            if(m) return m.value;
        }
        else {
            def filter = existingInfos.findAll{ it.lob?.objid==null };
            def m = filter.find{ it.attribute.objid == info.attribute.objid };
            if(m) return m.value;
        }
        return null;
    }
    
    void buildControls(items) { 
        //sort first the elements before saksakin in the stack;
        def list = items.findAll{it.lob?.objid==null && it.attribute.category==null}?.sort{it.attribute.sortorder};
        def catGrp = items.findAll{it.lob?.objid==null && it.attribute.category!=null};
        if(catGrp) {
            def grpList = catGrp.groupBy{ it.attribute.category };
            grpList.each { k,v->
                v.sort{ it.attribute.sortorder }.each{z->
                    list.add( z );
                }
            }
        }
        list = list + items.findAll{ it.lob?.objid!=null }?.sort{ [it.lob.name, it.attribute.sortorder] }; 
        
        formControls.clear();
        list.each {x->
            def i = [
                type:x.attribute.datatype, 
                caption:x.attribute.caption, 
                categoryid:  ((x.lob?.objid!=null) ? x.lob.objid : x.attribute.category),
                handler: x.attribute.handler,
                name:x.attribute.name, 
                bean: x,
                properties: [:],
                value: x.value
            ];
            /*
            x.datatype = x.attribute.datatype;
            if(x.datatype.indexOf("_")>0) {
                x.datatype = x.datatype.substring(0, x.datatype.indexOf("_"));
            }
            */
            if(i.type == "boolean") {
                i.type = 'yesno';
            }
            else if( i.type == "string" ) {
                i.type = "text";
            }
            else if(i.type == "string_array") {
                i.type = "combo";
                i.preferredSize = '150,20';
                i.itemsObject = x.attribute.arrayvalues;
            }
            else if( i.type.matches('decimal|integer')) {
                i.preferredSize = '150,20';
            }
            i.required = true;
            formControls << i;
        } 
    }
    
    def executeRule(def p) {
        def result = [:];
        if( mode == "info" ) {
            result = infoService.execute(p);
        }
        else if(mode == "assess") {
            result = assessmentService.assess(p);
        }
        if(result.phase > 1) {
            result.completed = true;
        }
        else {
            if(!result.infos) 
                throw new Exception("There must be at least one info");
            result.infos.each {
                it.value = findValue(it);
            };            
            stack.push( result.infos );
            buildControls( result.infos );
            result.completed = false;
        }
        return result;
    }
    
    void init() {
        executeRule(params);
    }
    
    void doNext() {
        def m = [:];
        m.putAll( params );
        m.infos = [];
        stack.each {
            m.infos.addAll(it);
        }
        def r = executeRule(m);
        if( r.completed == true ) {
            if( mode=="info" ) {
                mode = "assess";
                r = executeRule(m);
                if(!r.completed) return;
            }
            if(!handler) throw new Exception("Handler is required in BusinessInfoPanelModel");
            if( r.infos ) m.infos.addAll(r.infos );
            if( r.taxfees ) m.taxfees = r.taxfees;
            def z = [:];
            z.putAll( r );
            z.infos = m.infos;
            z.taxfees = m.taxfees;
            handler( z );
        }
    }
    
    def doBack() {
        if( stack.empty() ) return;
        stack.pop();
        buildControls(stack.peek());
    }
    
}