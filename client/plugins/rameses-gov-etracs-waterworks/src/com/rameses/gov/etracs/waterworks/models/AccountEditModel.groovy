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

    void detachMeter() {
        if( !MsgBox.confirm("You are about to detach this model. Proceed?")) return;
        def m = [_schemaname: 'waterworks_account' ];
        m.findBy = [objid: entity.objid ];
        m.meterid =  null;
        changeInfoSvc.execute(m);
        caller?.reload();
    }

    void attachMeter() {
        def h = { o->
            def m = [_schemaname: 'waterworks_account' ];
            m.findBy = [objid: entity.objid ];
            m.meterid =  o.objid;
            changeInfoSvc.execute(m);
            caller?.reload();
        }
        Modal.show("waterworks_meter_wo_account:lookup", [onselect: h] );
    }

    void disconnect() {
        if(!MsgBox.confirm("You are about to disconnect this account. Continue?")) return;
        def m = [_schemaname: 'waterworks_meter' ];
        m.findBy = [objid: entity.meterid ];
        m.state = "DISCONNECTED";
        changeInfoSvc.execute(m);
        caller?.reload();
    }

    void reconnect() {
        if(!MsgBox.confirm("You are about to reconnect this account. Continue?")) return;
        def m = [_schemaname: 'waterworks_meter' ];
        m.findBy = [objid: entity.meterid ];
        m.state = "ACTIVE";
        changeInfoSvc.execute(m);
        caller?.reload();
    }

}