package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class RemittanceInitialModel extends CrudListModel {

    @Service("RemittanceService")
    def remSvc;    
    
    def amount = 0; 
    def totalvoid = 0;
    
    def summaryList;
    def voidList = [];
    
    void init() {
        //build summaryList
        def m = [_schemaname: 'cashreceipt_af_summary' ];
        m.where = ["collector.objid =:collectorid AND remittanceid IS NULL", [collectorid: user.objid ] ];
        summaryList = queryService.getList( m );
        if(!summaryList)
            throw new Exception("No cash receipts to remit")
        amount = summaryList.sum{ it.amount };
        
        //list of voided
        m = [_schemaname: 'cashreceipt' ];
        m.where = ["collector.objid =:collectorid AND remittanceid IS NULL AND NOT(void.objid IS NULL)", [collectorid: user.objid ] ];
        voidList = queryService.getList( m );
        if( voidList ) {
            totalvoid = voidList.sum{ it.amount };
        }
    }
    
    def afSummaryHandler = [
        fetchList: { o->
            return summaryList;
        },
        onOpenItem: {o,col->
            return Inv.lookupOpener("cashreceipt_list:unremitted", [entity: o] );
        }
    ] as BasicListModel;
    
    def voidReceiptHandler = [
        fetchList: { o->
            return voidList;
        },
        onOpenItem: {o,col->
            return Inv.lookupOpener("cashreceipt:open", [entity: [objid: o.objid ]] );
        }
    ] as BasicListModel;
    
    
    def submitForRemittance() {
        def p = [:];
        p.items = summaryList
        p = remSvc.create(p);
        return Inv.lookupOpener( "remittance:open", [entity: p ]);
    }
}    