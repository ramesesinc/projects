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

    void select() {
        if (!tdno) throw new Exception('TD No. is required.');
        
        def ledger = items.find{ it.tdno == tdno}
        if (!ledger) throw new Exception('TD No. does not exist from the list of ledgers.');
        if (ledger.selected) throw new Exception('Ledger has already been selected.');

        ledger.bill = true;
        onselect(ledger);
        tdno = null;
        binding.refresh('tdno');
    }

} 