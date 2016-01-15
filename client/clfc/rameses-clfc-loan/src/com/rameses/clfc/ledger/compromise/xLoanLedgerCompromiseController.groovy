package com.rameses.clfc.ledger.compromise;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.LoanUtil;
import java.rmi.server.UID;

class xLoanLedgerCompromiseController
{
    @Binding
    def binding;

    @Service("LoanLedgerCompromiseService")
    def svc;

    @PropertyChangeListener
    def listener = [
        "data.compromisetype": {o->
            data.isfixedamount = 0;
            data.iswaivepenalty = 0;
            data.iswaiveinterest = 0;
            if (o == 'FIXED AMOUNT') data.isfixedamount = 1;
            else {
                data.fixedamount = 0;
                data.dteffectivefrom = null;
                data.dteffectiveto = null;
                if (o == 'WAIVE PENALTY') data.waivepenalty = 1;
                else if (o == 'WAIVE INTEREST') data.iswaiveinterest = 1;
            }
        }
    ]

    String title = "Compromise";
    def entity;
    def data;
    def compromiseTypes = LoanUtil.compromiseTypes;
    def mode = 'read';

    def createEntity() {
        return [
            objid: 'COMP'+new UID(),
            fixedamount: 0
        ];
    }

    void init() {
        data = createEntity();
        mode = 'create';
    }
    void open() {
        data = svc.open([objid: entity.compromiseid]);
    }

    void save() {
        if (data.compromisetype == 'FIXED AMOUNT') {
            if (data.fixedamount == 0) throw new Exception("Fixed amount must be greater than 0.");
            if (data.fixedamount <= entity.balance) throw new Exception("Fixed amount must be greater than balance.");
        }
        data.balance = entity.balance;
        data.ledgerid = entity.ledgerid;
        if (mode == 'create') svc.create(data);
        else if (mode == 'edit') svc.update(data);
        mode == 'read'
        entity.compromiseid = data.objid;
        binding.refresh('data');
    }

    def close() {
        return '_close';
    }
}