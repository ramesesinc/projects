package com.rameses.clfc.treasury.ledger.amnesty.fix

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class FixTermController 
{
    @Binding
    def binding;
    
    def entity, mode = 'read';
    def daysList, maxDay = 31;
    
    void init() {
        resetDaysList();
        binding?.refresh();
    }
    
    void resetDaysList() {
        daysList = [];
        for (int i=0; i <= maxDay; i++) { daysList.add(i); }
    }
}

