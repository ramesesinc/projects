package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

class MarketCapturePaymentModel extends CrudFormModel {

    @Service("MarketCapturePaymentService")
    def service;
    
    def selectedItem;
    
    public void afterCreate() {
        entity.items = [];
        entity.acctid = caller.masterEntity.objid;
    }
    
    def buildItems() {
        def param = [:];
        def h = [
            getFields: { 
                return [
                    [name:'billdate', caption:'Bill Date', type:'date'],
                    [name:'fromdate', caption:'From Date', type:'date'],
                    [name:'days', caption:'No. of Days', type:'integer'],
                ];
            },
            onUpdate: { name,value,item->
                param[(name)] = value;
            },
            onComplete: {
                param.acctid = entity.acctid;
                entity.items = service.getBilling( param );
                itemListModel.reload();
            }
        ];
        return Inv.lookupOpener( "dynamic:prompt", [handler:h] );
    }
    
    def zeroIfEmpty(  def x ) {
        if(x==null) return 0;
        return x;
    }
    
    void updateAmount(item) {
        item.rate = zeroIfEmpty(item.rate);
        item.extrate = zeroIfEmpty(item.extrate);
        item.surcharge = zeroIfEmpty(item.surcharge);
        item.interest = zeroIfEmpty(item.interest);
        item.linetotal = item.rate + item.extrate  + item.surcharge + item.interest;
        entity.amount = 0;
        entity.amount = entity.items.sum{x-> x.linetotal};
        binding.refresh("entity.amount");
    }
    
    def itemListModel = [
        isAllowAdd : {
            return false;
        },
        fetchList: {
            return entity.items;
        },
        onAddItem: { o->
            addItem("items", o);
            updateAmount(o);
        },
        onRemoveItem: { o->
            removeItem("items", o);
            updateAmount(o);
        },
        onColumnUpdate: { o,colName ->
            if(colName.matches('rate|extrate|surcharge|interest') ) updateAmount( o );
        }
    ] as EditorListModel;
    
}