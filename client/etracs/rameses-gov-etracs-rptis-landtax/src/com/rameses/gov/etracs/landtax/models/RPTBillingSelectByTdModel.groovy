package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

class RPTBillingSelectByTdModel
{
    @Binding
    def binding;

    String title = 'Add ledgers to Bill';

    def onselect = {};
    def items;
    def tdno;
    def lasttdno;

    void add() {
        if (!tdno) throw new Exception('TD No. is required.');
        
        def ledger = items.find{ it.tdno == tdno}
        if (!ledger) throw new Exception('TD No. does not exist from the list of ledgers.');
        if (ledger.selected) {
            MsgBox.alert('Ledger has already been selected.');
        } else {
            ledger.bill = true;
            lasttdno = tdno;
            onselect(ledger);
        }
    }

    def addNext() {
        if (!tdno) throw new Exception('TD No. is required.');
        def delimiter = '';
        if (tdno.matches('.*-.*')) delimiter = '-';
        def tokens = tdno.split('-');
        def sseq = tokens[tokens.size()-1];
        def nextseq = new java.math.BigDecimal(sseq).longValue() + 1 ;
        tdno = nextseq.toString().padLeft(sseq.length(), '0');

        if (delimiter != '') {
            tokens[tokens.size() - 1] = nexttdseq;
            tdno = tokens.join(delimiter);
        }
        binding.refresh('tdno');
        add();
    }

} 