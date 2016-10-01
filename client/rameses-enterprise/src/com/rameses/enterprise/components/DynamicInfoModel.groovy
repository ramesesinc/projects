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
            handler.onUpdate( name, value, item );
        },
        getControlList: { o->
            return formControls;
        }
    ] as FormPanelModel;
    
    void init() {
        if( !handler ) throw new Exception("handler must be set ");
        if( !handler.onUpdate ) throw new Exception("handler.onUpdate(name,value,item) must be set");
        if( !handler.getControls ) throw new Exception("handler.getControlList(list) must be set");
        if( !handler.onComplete ) throw new Exception("handler.onComplete must be set");
        if( !handler.getValue ) throw new Exception("handler.getValue(name,item) must be set");
        buildControls();
    }
    
    void buildControls() {
        def pInfos = [];
        for( st in infoStack ) {
            pInfos.addAll( st );
        };
        def infos = handler.getControls(pInfos);
        if(!infos) return;
        
        infos = infos.sort{ [it.categoryid, it.sortorder] }
        def xForms = [];
        infos.each {x->
            def i = [:];
            i.putAll(x);
            if(i.required == null ) i.required= true;
            i.bean = x;
            i.value = x.value;
            i.properties = [:];
            
            if(i.type == "string_array") {
                i.type = "combo";
                i.preferredSize = '150,20';
                i.itemsObject = x.arrayvalues;
            }
            else if(i.type == "boolean") {
                i.type = "yesno";
            }
            else if( i.type.matches('decimal|integer|date') ) {
                i.preferredSize = '150,20';
            }
            else if( i.type == "string" ) {
                i.type = "text";
            }
            xForms << i;
        }
        infoStack.push(xForms);
        formControls = xForms;
     }
     
     void doBack() {
         if(!handler.pushBack) throw new Exception("Please implement a handler.pushBack method");
         handler.pushBack();
         infoStack.pop();
         formControls = infoStack.peek();
         formPanel.reload();
     }   
    
     void doNext() {
         buildControls();
         formPanel.reload();
     }   
}
