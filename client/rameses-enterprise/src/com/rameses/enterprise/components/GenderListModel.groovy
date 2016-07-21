package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class GenderListModel extends ComponentBean {

    def genderList = [
        [key: 'M', value:'Male'],
        [key: 'F', value:'Female'],
    ];
    
    public String getGender() {
        return getValue(); 
    }
    
    public void setGender(String s) {
        setValue( s ); 
    }
    
}