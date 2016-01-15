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

class LoanSegregationAnalyzerController
{
    @Binding
    def binding;
    
    @Service("SampleSegregationRuleService")
    def service;
    
    def entity;
    def segregationtypes;
    
    String title = "Segregation Rules Analyzer"
    
    @PropertyChangeListener
    def listener = [
        "entity.term": { o->
            println 'term ' + o;
            if (entity.dtrelease) {
                Calendar c = Calendar.getInstance();
                c.setTime(parseDate(entity.dtrelease));
                c.add(Calendar.DATE, o);
                entity.dtmatured = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()).toString();
                binding.refresh('entity.dtmatured');
            }
        },
        "entity.dtrelease": { o->
            println 'dtrelease ' + o;
            if (entity.term) {
                Calendar c = Calendar.getInstance();
                c.setTime(parseDate(o));
                c.add(Calendar.DATE, entity.term);
                entity.dtmatured = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()).toString();
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
    
    void init() {
        entity = [term: 120];
    }
    
    void runTest() {
        segregationtypes = service.execute(entity);
        listHandler.reload();
    }
    
    def listHandler = [
        fetchList: { o->
            if (!segregationtypes) segregationtypes = [];
            return segregationtypes;
        }
    ] as BasicListModel;
}