package com.rameses.clfc.treasury.ledger.amnesty.fix

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class FixLumpSumController 
{
    @Binding
    def binding;
    
    def entity, mode = 'read';
    def daysList, maxDay = 15;
    
    void init() {
        resetDaysList();
    }
    
    void resetDaysList() {
        daysList = [];
        for (def i = 1; i <= maxDay; i++) { daysList.add(i); }
        binding?.refresh();
    }
}

