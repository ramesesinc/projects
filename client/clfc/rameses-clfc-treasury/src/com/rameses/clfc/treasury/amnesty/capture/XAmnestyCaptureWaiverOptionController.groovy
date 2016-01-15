package com.rameses.clfc.treasury.amnesty.capture

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class XAmnestyCaptureWaiverOptionController 
{	
    def entity;
    def allowEdit;
    
    void init() {
        if (!entity) entity = [:];
    }
    
}

