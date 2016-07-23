package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class BooleanYesNoModel extends ComponentBean {

    def trueValue;
    def falseValue;
    
    public Object getVal() {
        if( getValue() == trueValue ) {
            return true;
        }
        else if( getValue() == falseValue ) {
            return false;
        }    
        return null;
    }
    
    public void setVal(Object s) {
        println "boolean value is " +s;
        if( s == true ) {
            setValue( trueValue );
        }
        else if(s == false) {
            setValue( falseValue );
        }    
    }
    
}
