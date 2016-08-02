package com.rameses.gov.etracs.bpls.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.rmi.server.UID;

class BusinessAddressModel extends ComponentBean {
        
    def addressTypeList = Inv.lookupOpener("");
    
    def addressType;
    
    
}