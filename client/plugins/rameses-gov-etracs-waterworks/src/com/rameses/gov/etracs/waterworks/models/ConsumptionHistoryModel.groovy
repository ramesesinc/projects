package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

public class ConsumptionHistoryModel {
    
    @Binding
    def binding;
  
    
    @Service("QueryService")
    def qryService;
    
    int months = 3;
    def acctid;
    int year;
    int month;
    
    int average;
    def minimum;
    def maximum;
    
    def list;
    
    @PropertyChangeListener
    def listener = [
        "months" : { o->
            buildList();
            listHandler.reload();
            binding.refresh();
        }
    ];
    
    void buildList() {
        def m = [_schemaname: 'vw_waterworks_consumption'];
        m.findBy = [acctid: acctid];
        m.where = ["((year * 12)+month) < :ym", [ym: ((year * 12)+month)]];
        m.orderBy = "year DESC, month DESC";
        m._start = 0;
        m._limit = months;
        list = qryService.getList( m );
        average = 0;
        minimum = 0;
        maximum = 0;
        if(!list) return;
        average = (int) Math.ceil(list.sum{ it.volume } / list.size() );
        minimum = list.min{ it.volume }?.volume;
        maximum =  list.max{ it.volume }?.volume;
    }
    
    def listHandler = [
        fetchList: { o->
            return list;
        }
    ] as BasicListModel;
    
    def doClose() {
        return "_close";
    }

            
}