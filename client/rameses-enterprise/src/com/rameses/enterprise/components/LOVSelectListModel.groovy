package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class LOVSelectListModel extends ComponentBean {

    @Service("LOVService")
    def lovService;
    
    String listName;
    
    boolean multiselect = true;
    def lovList;
    def list;
    
    void init() {
        if(!listName ) throw new Exception("Please specify an listName in LOVSelectListModel");
        lovList = lovService.getKeyValues( listName );
        list = [];
        if( multiselect ) {
            lovList.each { 
                list << [ text: it.value, type:'checkbox', name: it.key, showCaption:false ];
            }
        }
        else {
            lovList.each { 
                list << [ text: it.value, type:'radio', name: 'lov', optionValue:it.key, showCaption:false ];
            }
        }
    }
    
    public String getLov() {
        return getValue(); 
    }
    
    public void setLov(String s) {
        setValue( s ); 
    }
    
    def formControls = [
        updateBean: {name,value,item->
            println name + "=" + value;
            //item.bean.value = value;
        },
        getControlList: {
            return list;
        }
    ] as FormPanelModel;
    
}
