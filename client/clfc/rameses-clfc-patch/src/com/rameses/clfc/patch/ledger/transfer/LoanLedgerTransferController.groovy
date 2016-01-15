package com.rameses.clfc.patch.ledger.transfer;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class LoanLedgerTransferController {
    @Binding
    def binding;

    def service;
    def routeList;
    def entity;
    def route;

    LoanLedgerTransferController() {
        try {
            service = InvokerProxy.instance.create("LedgerRouteTransferService");
        } catch(ConnectException ce) {
            ce.printStackTrace();
            throw ce;
        }   
    }

    def transfer() {
        def msg = "<html>You are about to transfer <b>" + entity.borrower.name + "</b>" +
                    " with App. No. <b>" + entity.appno + "</b> from <b>" + entity.route.description + "</b>" +
                    " to <b>" + route.description + "</b>. Continue?</html>";
        if (MsgBox.confirm(msg)) {
            service.transferLedger([appid: entity.appid, routecode: route.code]);
            msg = "Successfully transfered <b>" + entity.borrower.name + "</b> from <b>" + entity.route.description + "</b> to <b>" + route.description + "</b>."
            MsgBox.alert(msg);
            entity.route = route;
            binding.refresh();
        }
    }

    def getRouteList() {
        return service.getList([routecode: entity.route.code]);
    }

    def close() {
        return "_close";
    }
}
