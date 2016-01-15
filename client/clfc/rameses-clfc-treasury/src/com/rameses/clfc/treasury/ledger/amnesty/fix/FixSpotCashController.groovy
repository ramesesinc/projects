package com.rameses.clfc.treasury.ledger.amnesty.fix

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class FixSpotCashController 
{
    @Binding
    def binding;
    
    def entity, mode = 'read';
    def daysList, maxDay = 31;
    
    @PropertyChangeListener
    def listener = [
        'entity.usedate': { o->
            if (o==1) {
                entity.months = 0;
                entity.days = null;
            } else if (o==0) {
                entity.date = null;
            }
            binding?.refresh();
        }
    ];
    
    void init() {
        entity.usedate = 0;
        resetDaysList();
        binding?.refresh();
    }
    
    void resetDaysList() {
        daysList = [];
        for (def i = 1; i <= maxDay; i++) { daysList.add(i); }
    }
}

