package com.rameses.clfc.treasury.dailycollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class DailyCollectionDepositSlipController
{
    def entity, selectedItem;
    def allowEdit;

    void init() {
        if (!entity.depositslips) entity.depositslips = [];
    }

    def add() {
        def handler = { o->
            def item = entity.depositslips.find{ it.refid == o.objid }
            if (item) throw new Exception("Deposit Slip with ControlNo " + o.controlno + " already selected.");

            item = [
                objid       : 'DCDS' + new UID(),
                parentid    : entity.objid,
                refid       : o.objid,
                txndate     : o.txndate,
                controlno   : o.controlno,
                amount      : o.amount,
                passbook    : o.passbook,
                info        : o
            ];

            if (!entity._addeddepositslip) entity._addeddepositslip = [];
            entity._addeddepositslip.add(item);
            
            entity.depositslips.add(item);
            listHandler.reload();
        }
        return Inv.lookupOpener('depositslip:lookup', [onselect: handler, state: 'ACTIVE']);
    }

    void remove() {
        if (MsgBox.confirm("You are about to remove this item. Continue?")) {
            if (!entity._removeddepositslip) entity._removeddepositslip = [];
            entity._removeddepositslip.add(selectedItem);
            if (entity._addeddepositslip) entity._addeddepositslip.remove(selectedItem);
            entity.depositslips.remove(selectedItem);
            listHandler.reload();
        }
    }

    def listHandler = [
        fetchList: { o->
            if (!entity.depositslips) entity.depositslips = [];
            return entity.depositslips;
        }
    ] as BasicListModel;

    def getHtmlview() {
        if (!selectedItem) return "";
        def info = selectedItem.info;
        return """
            <html>
                <body>
                    <h1>Deposit Slip Information</h1>
                    <table>
                        <tr>
                            <td> <b>Date</b> </td>
                            <td> <b>:</b> </td>
                            <td>${new java.text.SimpleDateFormat("MMM-dd-yyyy").format(info.txndate)}</td>
                        </tr>
                        <tr>
                            <td> <b>Control No.</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.controlno}</td>
                        </tr>
                        <tr>
                            <td> <b>Amount</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.amount}</td>
                        </tr>
                        <tr>
                            <td> <b>Passbook No.</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.passbook.passbookno}</td>
                        </tr>
                        <tr>
                            <td> <b>Acct. No.</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.passbook.acctno}</td>
                        </tr>
                        <tr>
                            <td> <b>Acct. Name</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.passbook.acctname}</td>
                        </tr>
                        <tr>
                            <td> <b>Currency Type</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.currencytype?.name? info.currencytype.name : ''}</td>
                        </tr>
                        <tr>
                            <td> <b>Account Type</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.accounttype?.name? info.accounttype.name : ''}</td>
                        </tr>
                        <tr>
                            <td> <b>Deposit Type</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.deposittype?.name? info.deposittype.name : ''}</td>
                        </tr>
                    </table>
                </body>
            </html>
        """;
    }
}