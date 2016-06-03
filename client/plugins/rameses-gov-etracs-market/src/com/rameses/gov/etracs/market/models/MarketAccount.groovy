package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;
import com.rameses.seti2.models.*;

public class MarketAccount extends CrudFormModel {

    @PropertyChangeListener
    def listener = [
        'entity.unit' : { o->
            entity.rate = o.rate;
            entity.term = o.term;
            binding.refresh("entity.(rate|term)");
        }
    ];        

    def ledgerList = [
        fetchList: { o->
            def m = [_schemaname: 'market_ledger' ];
            m.findBy = [acctid: entity.objid];
            m.orderBy = "year,month";
            return queryService.getList(m);
        }
    ] as BasicListModel;
   
}