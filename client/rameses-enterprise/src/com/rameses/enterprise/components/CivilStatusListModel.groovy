package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class CitizenshipSuggestModel extends ComponentBean {

   def civilStatusList = LOV.CIVIL_STATUS;
    
    public String getCivilStatus() {
        return getValue(); 
    }
    
    public void setCivilStatus(String s) {
        setValue( s ); 
    }
    
}
