package com.rameses.clfc.ledger.preview

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.text.*;

class LoanLedgerPreviewController 
{	
    @Binding
    def binding;

    @Service("LoanLedgerPreviewService")
    def service;

    String getTitle() {
        def str = "Ledger";
        if (entity?.loanamount) {
            def df = new DecimalFormat("#,##0.00");
            str += " - " + df.format(entity.loanamount);
        }
        return str;
    }
    
    def list, entity, htmlview, pagecount;
    def currpageindex, pagenumber, rows;

    void refresh() {
        //println 'ledger preview refresh';
        //def a = (entity.ledgercount/rows);
        //lastpageindex = new BigDecimal(a+'').setScale(0, BigDecimal.ROUND_CEILING);
        listHandler.reload();
        binding?.refresh('pagecount');
    }    
    
    def printLedger() {
        return Inv.lookupOpener('borrower-ledger:report', [ledgerid: entity.objid, appid: entity.appid]);
    }

    def listHandler = [
        getRows: { 
            if (!entity.rows) entity.rows = 30;
            return entity.rows; 
        },
        getColumns: { return service.getColumns(); },
        getLastPageIndex: { 
            if (!entity.lastpageindex) entity.lastpageindex = -1;
            return entity.lastpageindex; 
        },
        fetchList: { o->
            //println 'params ' + o;
            if (entity) o.ledgerid = entity.objid; 
            list = service.getList(o);
            buildHtmlview();
            return list;
        },
        onOpenItem: { itm, colName ->
            if (colName != 'remarks' || !itm.remarks) return null;
            
            return Inv.lookupOpener("remarks-preview", [remarks: itm.remarks]);
        }
    ] as PageListModel;

    void moveFirstPage() {
        listHandler.moveFirstPage();
        binding?.refresh('pagecount');
    }

    void moveBackPage() {
        listHandler.moveBackPage();
        binding?.refresh('pagecount');
    }

    void moveNextPage() {
        listHandler.moveNextPage();
        binding?.refresh('pagecount');
    }

    void moveLastPage() {
        currpageindex = listHandler.getLastPageIndex();
        listHandler.moveLastPage();
        binding?.refresh('pagecount');
    }
    
    def getPagecount() {
        //if (!list) return "Page 1 of ?";
        return "Page " + listHandler.getPageIndex() + " of " + listHandler.getLastPageIndex();
    }

    void goToPageNumber() {
        currpageindex = pagenumber
        if (pagenumber > listHandler.getLastPageIndex()) currpageindex = listHandler.getLastPageIndex();
        listHandler.moveToPage(currpageindex);
        binding?.refresh('pagecount');
    }
    
    void buildHtmlview() {
        htmlview = "<html><body><table border=1 cellspacing=0 cellpadding=2>";

        htmlview += buildHeader();
        htmlview += buildContent();

        htmlview += "</table></body></html>";
        binding?.refresh('htmlview');
    }

    def buildHeader() {
        return """
            <tr>
                <td width=150 align='center'><b>Schedule of Payment</b></td>
                <td width=150 align='center'><b>Partial Payment</b></td>
                <td width=150 align='center'><b>Balance</b></td>
                <td width=150 align='center'><b>Interest Paid</b></td>
                <td width=150 align='center'><b>Penalty Charges</b></td>
                <td width=150 align='center'><b>Total Payment</b></td>
                <td width=150 align='center'><b>OR No.</b></td>
                <td width=150 align='center'><b>Date Paid</b></td>
                <td width=150 align='center'><b>Remarks</b></td>
            </tr>
        """;
    }
    
    def buildContent() {
        def str = "";
        list.eachWithIndex{ o, idx->
            if (idx < rows) {
                str += """
                    <tr>
                        <td valign='top'>${formatDate(o.paymentschedule)}</td>
                        <td align='right' valign='top'>${formatAmount(o.partialpayment)}</td>
                        <td align='right' valign='top'>${formatAmount(o.balanceamount)}</td>
                        <td align='right' valign='top'>${formatAmount(o.interestpaid)}</td>
                        <td align='right' valign='top'>${formatAmount(o.penaltycharge)}</td>
                        <td align='right' valign='top'>${formatAmount(o.totalpayment)}</td>
                        <td valign='top'>${o.receiptno? o.receiptno : ''}</td>
                        <td valign='top'>${formatDate(o.datepaid)}</td>
                        <td valign='top'>${o.remarks? o.remarks : ''}</td>
                    </tr>
                """;
            }
        }
        return str;
    }
    
    def formatDate( d ) {
        if (!d) return '';
        
        def date;
        if (d instanceof Date) {
            date = d;
        } else {
            date = java.sql.Date.valueOf(d);
        }
        def df = new SimpleDateFormat("MMM. dd, yyyy");
        return df.format(date);
    }
    
    def formatAmount( amt ) {
        if (!amt) return '';
        
        //def str;
        StringBuffer sb = new StringBuffer();
        def df = new DecimalFormat("#,##0.00;(#,##0.00)");
        FieldPosition pos = new FieldPosition(NumberFormat.INTEGER_FIELD);
        return df.format(amt, sb, pos);
        
        /*if (amt instanceof BigDecimal) {
            amt = amt.setScale(2);
            println 'amt ' + amt;
            if (amt >= 0) {
                str = NumberFormat.getInstance().format(amt, sb, 0);
            } else {
                amt *= -1;
                str = "(" + NumberFormat.getInstance().format(amt, sb, 0) + ")";
            }
        } else {
            def df = new DecimalFormat("#,##0.00");
            if (amt >= 0) {
                str = df.format(amt, sb, 0);
            } else {
                amt *= -1;
                str = "(" + df.format(amt, sb, 0) + ")";
            }
        }*/
        //return str;
    }
}

