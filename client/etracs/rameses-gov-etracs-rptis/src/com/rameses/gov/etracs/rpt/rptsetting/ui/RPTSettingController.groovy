package com.rameses.gov.etracs.rpt.rptsetting.ui;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class RPTSettingController
{
    @Service('RPTSettingService')
    def svc 
    
            
    @Invoker
    def invoker 
            
            
    def settings;
    def entity;
    
    def category
    int page = 1;
    int rowcount = 20;
    boolean last;
    
    String getTitle(){
        return invoker.caption 
    }
            
    void init(){
        category = invoker.properties.category
        settings = loadSettings()
    }
    
    void next(){
       saveSettings();
       page++;
       def list = loadSettings()
       if (list){
           settings = list;
       }
       else {
           page--;
       }
    }
    
    void back(){
        saveSettings()
        page--;
        if (page == 0) {
            page = 1;
        }
        else{
            settings = loadSettings();
        }
        
    }
    
    void saveSettings(){
        entity.each{k,v ->
            def setting = settings.find{it.name == k}
            setting?.value = v;
        }
        svc.updateSettings(settings)
    }

    def loadSettings(){
        settings = svc.getSettings([category:category, page:page, rowcount:rowcount])
        entity = [:]
        settings.each{
            entity[it.name] = it.value
        }
    }
    
    
    def buildFormControls(){
        def controls = [];
        def type = null;
        settings.eachWithIndex{ item, idx ->
            type = (item.datatype ? item.datatype : 'string');
            controls.add(
                new FormControl(type, [caption:item.name, name:'entity.' + item.name, captionWidth:225, textCase:'NONE'] )
            )
        }
        return controls;
    }
    
    def formControl = [
        getFormControls: { return buildFormControls() },
    ] as FormPanelModel;
}
