package com.rameses.clfc.rules

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;
import java.text.SimpleDateFormat;

class LoanBillingAnalyzerController {
    @Binding
    def binding;

    @Service("SampleBillingRuleService")
    def service;
    
    @Service("DateService")
    def dateSvc;
    
    @FormTitle    
    String title = "Loan Billing Analyzer"

    def df = new SimpleDateFormat("yyyy-MM-dd");
    
    def entity = [:];
    
    void init() {
        entity = [
            ledgerid        : 'LEDER' + new UID(),
            appid           : 'APP' + new UID(),
            intrate         : 0.05,
            principal       : 5000,
            absentpenalty   : 3,
            overduepenalty  : 9.25,
            interest        : 8.30,
            noofholidays    : 0,
            noofdaysexempted: 0,
            underpytrate    : 0.03,
            schedule        : 100,
            term            : 120,
            overpayment     : 0,
            avgamount       : 150,
            amnesty         : [:],
            lackinginterest : 0,
            lackingpenalty  : 0,
            billdate        : dateSvc.getServerDateAsString().split(" ")[0]
            //payments    : []
        ];
    }
    
    def paymentMethods = [
        [key: 'schedule', value: 'Schedule/Regular'],
        [key: 'over', value: 'Overpayment']
    ]
    
    @PropertyChangeListener
    def listener = [
        "entity.term": { o->
            if (entity.dtrelease) {
                Calendar c = Calendar.getInstance();
                c.setTime(parseDate(entity.dtrelease));
                c.add(Calendar.DATE, o);
                entity.dtmatured = df.format(c.getTime()).toString();
                binding.refresh('entity.dtmatured');
            }
        },
        "entity.dtrelease": { o->
            if (entity.term) {
                Calendar c = Calendar.getInstance();
                c.setTime(parseDate(o));
                c.add(Calendar.DATE, entity.term);
                entity.dtmatured = df.format(c.getTime()).toString();
                binding.refresh('entity.dtmatured');
            }
        }
    ]
    
    def parseDate( date ) {
        if (date instanceof Date) {
            return date;
        } else {
            return java.sql.Date.valueOf(date);
        }
    }
    
    def billing = [:];
    
    void runTest() {
        billing = service.execute(entity);
        binding.refresh('billing');
    }
}

