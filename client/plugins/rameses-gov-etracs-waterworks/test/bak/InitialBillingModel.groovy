package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class InitialBillingModel extends PageFlowController {
    
    @Service("QueryService")
    def querySvc;
    
    @Caller
    def caller;
    
    def bomList;
    def bomtotal = 0.0;
    def payOption = [type:'fullpayment'];
    def feeList;
    def billTotal;
    def matBalance;
    
    def entity;
    def handler;
    
    void fetchBomList() {
        def m = [_schemaname:'waterworks_application_bom'];
        m.findBy = [parentid: entity.objid];
        m.where = ["cwdsupplied IS NULL OR cwdsupplied=0"];
        bomList = querySvc.getList(m);
        bomtotal = bomList.sum{ it.linetotal };
        payOption.amount = bomtotal;
    }
    
    def bomListModel = [
        fetchList: { o->
            return bomList;
        }
    ] as BasicListModel;
    
    def feeListModel = [
        fetchList: { o->
            return feeList;
        }
    ] as BasicListModel;
    
    void finalizeBilling() {
        feeList = [];
        feeList.addAll( entity.fees );
        def z = [:]
        z.item = [objid:'WATER_BOM', code:'-', title:'WATERWORKS MATERIALS'];
        if( payOption.type == 'fullpayment' ) {
            z.amount = payOption.amount;
        }
        else {
            z.amount = payOption.amortization;
        }
        payOption.account = z.item;
        feeList << z;
        billTotal = feeList.sum{ it.amount };
        matBalance = payOption.amount - z.amount;
        feeListModel.reload();
        binding.refresh();
    }
    
    @PropertyChangeListener
    def updater = [
        "payOption.term": {
            payOption.amortization = payOption.amount / payOption.term;
            binding.refresh("payOption.amortization");
        }
    ]
    
    void postBilling() {
        handler( payOption );
    }
    
}