package gov.gsc.admin.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

class UserDivisionModel {
    @Caller
    def caller;
    
    def entity;
    
    void init() {
        println 'caller => ' + caller.initiator;
    }
}