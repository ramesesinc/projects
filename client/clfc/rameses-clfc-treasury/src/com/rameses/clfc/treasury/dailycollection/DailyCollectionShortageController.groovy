package com.rameses.clfc.treasury.dailycollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class DailyCollectionShortageController
{
    def entity, selectedItem;
    def allowEdit

    void init() {
        if (!entity.shortages) entity.shortage = [];
    }

    def add() {
        def handler = { o->
            def item = entity.shortages.find{ it.refid == o.objid }
            if (item) throw new Exception("Shortage " + o.refno + " already selected.");

            item = [
                objid       : 'DCS' + new UID(),
                parentid    : entity.objid,
                refid       : o.objid,
                refno       : o.refno,
                cbsno       : o.cbsno,
                txndate     : o.txndate,
                collector   : o.collector,
                amount      : o.amount,
                remarks     : o.remarks
            ];

            if (!entity._addedshortage) entity._addedshortage = [];
            entity._addedshortage.add(item);
            entity.shortages.add(item);
            listHandler.reload();
        }
        return Inv.lookupOpener('shortage:lookup', [onselect: handler, state: 'NOTED']);
    }

    void remove() {
        if (MsgBox.confirm("You are about to remove this item. Continue?")) {
            if (!entity._removedshortage) entity._removedshortage = [];
            entity._removedshortage.add(selectedItem);
            if (entity._addedshortage) entity._addedshortage.remove(selectedItem);
            entity.shortages.remove(selectedItem);
            listHandler.reload();
        }
    }

    def listHandler = [
        fetchList: { o->
            if (!entity.shortages) entity.shortages;
            return entity.shortages;
        }
    ] as BasicListModel;

    def getHtmlview() {
        if (!selectedItem) return "";
        def info = selectedItem;
        return """
            <html>
                <body>
                    <h1>Shortage Information</h1>
                    <table>
                        <tr>
                            <td> <b>Date</b> </td>
                            <td> <b>:</b> </td>
                            <td>${new java.text.SimpleDateFormat("MMM-dd-yyyy").format(info.txndate)}</td>
                        </tr>
                        <tr>
                            <td> <b>Ref. No.</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.refno}</td>
                        </tr>
                        <tr>
                            <td> <b>CBS No.</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.cbsno}</td>
                        </tr>
                        <tr>
                            <td> <b>Collector</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.collector.name}</td>
                        </tr>
                        <tr>
                            <td> <b>Amount</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.amount}</td>
                        </tr>
                        <tr>
                            <td> <b>Remarks</b> </td>
                            <td> <b>:</b> </td>
                            <td>${info.remarks}</td>
                        </tr>
                    </table>
                </body>
            </html>
        """;
    }
}