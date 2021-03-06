package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class DynamicInfoModel extends ComponentBean {

     def handler;
     private def formControls = [];
     Stack infoStack = new Stack();  
    
     def formPanel = [
        updateBean: {name,value,item->
            item.value = value;
            if(!handler.onUpdate) 
                println "No handler.onUpdate found:->"+name + " "+value;
            else 
                handler.onUpdate( name, value, item );
        },
        getControlList: {
            return formControls;
        }
    ] as FormPanelModel;
    
    void init() {
        if( !handler ) throw new Exception("handler must be set ");
        if( !handler.getControls ) throw new Exception("handler.getControlList(list) must be set");
        buildControls();
    }
    
    void buildControls() {
        def pInfos = [];
        for( st in infoStack ) {
            pInfos.addAll( st );
        };
        def infos = handler.getControls(pInfos);
        if(!infos) return;
        
        infos = infos.sort{ it.sortorder }
        def xForms = [];
        infos.each {x->
            def i = [:];
            i.putAll(x);
            if(i.required == null ) i.required= true;
            i.bean = x;
            i.properties = [:];
            if(i.type == "string_array") {
                i.type = "combo";
                i.preferredSize = '120,20';
                i.itemsObject = x.arrayvalues;
            }
            else if(i.type == "boolean") {
                i.type = "yesno";
                i.value = i.boolvalue;
            }
            else if( i.type.matches('decimal|integer|date') ) {
                i.preferredSize = '120,20';
            }
            else if( i.type == "string" ) {
                i.type = "text";
            }
            if(handler.getValue) {
                i.value = handler.getValue(i.name);
            }
            else {
                i.value = x.value;
            }
            xForms << i;
        }
        infoStack.push(xForms);
        formControls = xForms;
     }
     
     void doBack() {
         if(handler.pushBack) {
             handler.pushBack();
         }
         infoStack.pop();
         formControls = infoStack.peek();
         formPanel.reload();
     }   
    
     void doNext() {
         buildControls();
         formPanel.reload();
     }   
}
