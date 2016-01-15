package com.rameses.clfc.rules

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

class LoanPostingAnalyzerController 
{
    @Binding
    def binding;
    
    @Service("LoanPostingRuleService")
    def service;
    
    def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
    
    @PropertyChangeListener
    def listener = [
        "entity.term": { o->
            if (entity.dtreleased) {
                computeMaturityDate();
            }
        },
        "entity.dtreleased": { o->
            if (entity.term) {
                computeMaturityDate();
            }
        },
        "entity.paymentmethod": { o->
            if (o != 'over') {
                entity.avgamt = 0;
                binding?.refresh('entity.avgamt');
            }
        }
    ];
    
    private void computeMaturityDate() {        
        def cal = Calendar.getInstance();
        cal.setTime(parseDate(entity.dtreleased));
        cal.add(Calendar.DATE, entity.term);
        entity.dtmatured = df.format(cal.getTime());
        binding?.refresh('entity.dtmatured');
    }
    
    def parseDate( date ) {
        if (date instanceof Date) {
            return date;
        } else {
            return java.sql.Date.valueOf(date);
        }
    }
    def entity;
    def paymentMethodList = [
        [caption: 'Schedule/Regular', value: 'schedule'],
        [caption: 'Overpayment', value: 'over'],
    ]
    
    void init() {
        entity = [
            principal   : 5000,
            interest    : 8.35,
            absentrate  : 0.03,
            term        : 120,
            underpytrate: 0.03,
        ];
    }
    
    def paymentHandler = [
        fetchList: { o->
            if (!entity.payments) entity.payments = [];
            return entity.payments;
        },
        onAddItem: { o->
            if (!entity.payments) entity.payments = [];
            entity.payments.add(o);
            paymentHandler?.reload();
        }
    ] as EditorListModel;
    
    def list;
    def listModel = [
        fetchList: { o->
            if (!list) list = [];
            return list;
        }
    ] as BasicListModel;
    
    void runTest() {
        list = service.postPayment(entity);
        listModel?.reload();
    }
}

