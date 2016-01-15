package com.rameses.clfc.treasury.amnesty;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class WaiveOptionController 
{
    def entity, mode = 'read';
    
    void init() {
        if (!entity) entity = [:];
    }
    
}

