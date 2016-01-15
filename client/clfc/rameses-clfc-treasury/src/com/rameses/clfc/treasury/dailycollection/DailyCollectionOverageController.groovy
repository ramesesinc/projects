package com.rameses.clfc.treasury.dailycollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class DaiylCollectionOverageController
{
    def entity, list, selectedItem;
    def allowEdit;

    void init() {
        if (!entity.overages) entity.overages = [];
    }

    def add() {
        def handler = { o->
            def item = entity.overages.find{ it.refid == o.objid }
            if (item) throw new Exception("Overage " + o.refno + " already selected.");

            item = [
                objid       : 'DCO' + new UID(),
                parentid    : entity.objid,
                refid       : o.objid,
                refno       : o.refno,
                txndate     : o.txndate,
                collector   : o.collector,
                amount      : o.amount,
                remarks     : o.remarks
            ];

            if (!entity._addedoverage) entity._addedoverage = [];
            entity._addedoverage.add(item);

            entity.overages.add(item);
            listHandler.reload();
        }
        return Inv.lookupOpener('overage:lookup', [onselect: handler, state: 'NOTED'])
    }

    void remove() {
        if (MsgBox.confirm("You are about to remove this item. Continue?")) {
            if (!entity._removedoverage) entity._removedoverage = [];
            entity._removedoverage.add(selectedItem);
            if (entity._addedoverage) entity._addedoverage.remove(selectedItem);
            entity.overages.remove(selectedItem);
            listHandler.reload();
        }
    }

    def listHandler = [
        fetchList: { o->
            if (!entity.overages) entity.overages = [];
            return entity.overages;
        }
    ] as BasicListModel;

    def getHtmlview() {
        if (!selectedItem) return "";
        def info = selectedItem;
        return """
            <html>
                <body>
                    <h1>Overage Information</h1>
                    <table>
                        <tr>
                            <td> <b>Date</b> </td>
                            <td> <b>:</b> </td>
                            <td> ${new java.text.SimpleDateFormat("MMM-dd-yyyy").format(info.txndate)} </td>
                        </tr>
                        <tr>
                            <td> <b>Ref. No.</b> </td>
                            <td> <b>:</b> </td>
                            <td> ${info.refno} </td>
                        </tr>
                        <tr>
                            <td> <b>Collector</b> </td>
                            <td> <b>:</b> </td>
                            <td> ${info.collector.name} </td>
                        </tr>
                        <tr>
                            <td> <b>Amount</b> </td>
                            <td> <b>:</b> </td>
                            <td> ${info.amount} </td>
                        </tr>
                        <tr>
                            <td> <b>Remarks</b> </td>
                            <td> <b>:</b> </td>
                            <td> ${info.remarks} </td>
                        </tr>
                    </table>
                </body>
            </html>
        """;
    }
}