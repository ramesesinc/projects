package com.rameses.gov.etracs.bpls.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.rmi.server.UID;

public class BusinessLessorLookupModel extends ComponentBean {
       
    def onselect;
    
    public def getLookupLessor() {
        def h = { o->
            setValue( o );
            if(onselect) onselect(o);
        }
        return Inv.lookupOpener("business_lessor:lookup", [onselect: h]);
    }

}