package com.rameses.clfc.treasury.dailycollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class DailyCollectionEncashmentController
{
    def entity, selectedItem;
    def allowEdit;

    void init() {
        if (!entity.encashments) entity.encashments = [];
    }

    def add() {
        def handler = { o->
            def item = entity.encashments.find{ it.refid == o.objid }
            if (item) throw new Exception("Encashment with CheckNo " + o.check.checkno + " already selected.");

            item = [
                objid       : 'DCE' + new UID(),
                parentid    : entity.objid,
                refid       : o.objid,
                txndate     : o.txndate,
                amount      : o.amount,
                checkdate   : o.check.txndate,
                checkno     : o.check.checkno,
                bank        : o.check.bank,
                passbook    : o.check.passbook,
                info        : o
            ];

            if (!entity._addedencashment) entity._addedencashment = [];
            entity._addedencashment.add(item);
            
            entity.encashments.add(item);
            listHandler.reload();
        }
        return Inv.lookupOpener('encashment:lookup', [onselect: handler, state: 'APPROVED']);
    }

    void remove() {
        if (MsgBox.confirm("You are about to remove this item. Continue?")) {
            if (!entity._removedencashment) entity._removedencashment = [];
            entity._removedencashment.add(selectedItem);
            if (entity._addedencashment) entity._addedencashment.remove(selectedItem);
            entity.encashments.remove(selectedItem);
            listHandler.reload();
        }
    }

    def listHandler = [
        fetchList: { o->
            if (!entity.encashments) entity.encashments = [];
            return entity.encashments;
        } 
    ] as BasicListModel;

    def getHtmlview() {
        if (!selectedItem) return "";
        def info = selectedItem.info;
        return """
            <html>
                <body>
                    <h1>Encashment Information</h1>
                    <table>
                        <tr>
                            <td> <b>Date</b> </td>
                            <td> <b>:</b> </td>
                            <td>${new java.text.SimpleDateFormat("MMM-dd-yyyy").format(info.txndate)}</td>
                        </tr>
                        <tr>
                            <td> <b>Amount</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.amount}</td>
                        </tr>
                        <tr>
                            <td> <b>Check No.</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.check.checkno}</td>
                        </tr>
                        <tr>
                            <td> <b>Check Date</b> </td>
                            <td> <b>:</b> </td>
                            <td>${new java.text.SimpleDateFormat("MMM-dd-yyyy").format(info.check.txndate)}</td>
                        </tr>
                        <tr>
                            <td> <b>Bank</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.check.bank.objid}</td>
                        </tr>
                        <tr>
                            <td> <b>Overage</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.overage}</td>
                        </tr>
                        <!--
                        <tr>
                            <td> <b>PassbookNo</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.check.passbook.passbookno}</td>
                        </tr>
                        -->
                    </table>
                </body>
            </html>
        """;
    }
}