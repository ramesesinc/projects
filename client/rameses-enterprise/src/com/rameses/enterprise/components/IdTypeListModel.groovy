package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class IdTypeListModel extends ComponentBean {

   def idTypeList = LOV.ID_TYPES;
    
    public String getIdtype() {
        return getValue(); 
    }
    
    public void setIdtype(String s) {
        setValue( s ); 
    }
    
}