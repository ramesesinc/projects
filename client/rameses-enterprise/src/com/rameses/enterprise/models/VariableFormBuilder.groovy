package com.rameses.enterprise.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

/*******************************************************************
* This is used for building dynamic controls in variable
* This returns a list of controls to be used by the xform panel
********************************************************************/
public class VariableFormBuilder {
    
    public static def build( def list ) {
        def xForms = [];
        def infos = list.sort{ it.sortorder };
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
            i.value = x.value;
            xForms << i;
        }
        return xForms;
    }
    
}