package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
import com.rameses.seti2.models.*;

class JuridicalEntityNameMatchWarningModel  {
    
    def selectedItem;
    def list;
    def handler;
    
    def doCancel() {
        return "_close";
    }
    
    def doProceed() {
        if(list.find{it.match == 100 }) 
            throw new Exception("Name not valid. There is an exact match");
        //place the necessary flags here you want to include in the request.
        handler( [ignore_warning: true] );
        return "_close";
    }
}
