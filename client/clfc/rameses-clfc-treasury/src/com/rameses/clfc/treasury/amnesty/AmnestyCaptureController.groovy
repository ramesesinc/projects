package com.rameses.clfc.treasury.amnesty;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class AmnestyCaptureController extends AbstractAmnestyController
{
    String getServiceName() {
        return "AmnestyCaptureService";
    }
    
    def validate( data ) {
        def res = super.validate(data);
        if (!data.dtstarted) res.msg += 'Date Started is required.\n';
        if (res.msg) res.haserror = true;
        return res;
    }    
    
    def createEntity() {
        return [
            objid           : 'AMNSTYC' + new UID(),
            txnstate        : 'DRAFT',
            txnmode         : 'CAPTURE',
            //txndate         : dateSvc.getServerDateAsString().split(" ")[0],
            iswaivepenalty  : 0,
            iswaiveinterest : 0,
            version         : 1
        ];
    }    
    
    void closeAmnesty() {
        if (!MsgBox.confirm("You are about to close this document. Continue?")) return;
        
        entity = service.closeAmnesty(entity);
        binding.refresh('formActions');
    }
    
}

