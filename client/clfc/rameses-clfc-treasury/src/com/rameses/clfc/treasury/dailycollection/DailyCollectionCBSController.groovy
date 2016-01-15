package com.rameses.clfc.treasury.dailycollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class DailyCollectionCBSController 
{
    def entity, selectedItem;
    def allowEdit;

    void init() {
        if (!entity.cbs) entity.cbs = [];
    }

    void refresh() {
        listHandler.reload();
    }

    def listHandler = [
        fetchList: { o->
            if (!entity.cbs) entity.cbs = [];
            return entity.cbs;
        }
    ] as BasicListModel;

    def add() {
        def handler = { o->
            def item = entity.cbs.find{ it.refid == o.objid }
            if (item) throw new Exception("CBS No. " + item.cbsno + " already selected.");

            item = [
                objid       : 'DCC' + new UID(),
                parentid    : entity.objid,
                refid       : o.objid,
                txndate     : o.txndate,
                cbsno       : o.cbsno,
                amount      : o.amount,
                isencashed  : o.isencashed,
                info        : o
            ];
                        
            if (!entity._addedcbs) entity._addedcbs = [];
            entity._addedcbs.add(item);

            entity.cbs.add(item);
            listHandler.reload();
        }
        return Inv.lookupOpener('cbswithencashment:lookup', [onselect: handler, state: 'FOR_DAILYCOLLECTION']);
    }

    def remove() {
        if (MsgBox.confirm("You are about to remove this item. Continue?")) {
            if (!entity._removedcbs) entity._removedcbs = [];
            entity._removedcbs.add(selectedItem);
            if (entity._addedcbs) entity._addedcbs.remove(selectedItem);
            entity.cbs.remove(selectedItem);
            listHandler.reload();
        }
    }

    def getHtmlview() {
        if (!selectedItem) return "";
        def info = selectedItem.info;
        return """
            <html>
                <body>
                    <h1>Cash Breakdown Sheet Information</h1>
                    <table>
                        <tr>
                            <td> <b>CBS No.</b> </td>
                            <td> <b>:</b> </td>
                            <td> ${info.cbsno} </td>
                        </tr>
                        <tr>
                            <td> <b>Date</b> </td>
                            <td> <b>:</b> </td>
                            <td> ${new java.text.SimpleDateFormat("MMM-dd-yyyy").format(info.txndate)} </td>
                        </tr>
                        <tr>
                            <td> <b>Collector</b> </td>
                            <td> <b>:</b> </td>
                            <td> ${info.collector.name} </td>
                        </tr>
                        <tr>
                            <td> <b>Amount:</b> </td>
                            <td> <b>:</b> </td>
                            <td> ${info.amount} </td>
                        </tr>
                        <!--
                        <tr>
                            <td> <b>Is Encashed</b> </td>
                            <td> <b>:</b> </td>
                            <td> ${selectedItem.isencashed == 1? 'true' : 'false'} </td>
                        </tr>
                        -->
                    </table>
                </body>
            </html>
        """;
    }
}