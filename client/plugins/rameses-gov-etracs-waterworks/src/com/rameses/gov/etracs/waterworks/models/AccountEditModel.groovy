package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.treasury.common.models.*;

class AccountEditModel extends ChangeInfoModel {

    @PropertyChangeListener
    def flisten = [
        "data.stuboutnode": {o->
            action = (!o?.objid ) ? "detach-node" : "change-node";
            data.stuboutnodeid = o?.objid;
        },
        "data.meter" : { o->
            action = (!o?.objid) ? "detach-meter" : "change-meter";
            data.meterid = o?.objid; 
        }
    ];
}