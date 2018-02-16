package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class RemittanceModel extends CrudFormModel {

    @Service("RemittanceService")
    def remSvc;    
    
    def summaryList;
    def fundList = [];
    
    void afterOpen() {
        //build summaryList
        def m = [_schemaname: 'remittance_af' ];
        m.findBy = [ remittanceid: entity.objid ];
        summaryList = queryService.getList( m );
        
        //list of voided
        m = [_schemaname: 'remittance_fund' ];
        m.findBy = [ remittanceid: entity.objid ];
        fundList = queryService.getList( m );
        entity.amount = fundList.sum{ it.amount };
    }
    
    def afSummaryHandler = [
        fetchList: { o->
            return summaryList;
        },
        onOpenItem: {o,col->
            return Inv.lookupOpener("cashreceipt_list:remitted", [entity: o] );
        }
    ] as BasicListModel;
    
    def fundSummaryHandler = [
        fetchList: { o->
            return fundList;
        },
        onOpenItem: {o,col->
            return Inv.lookupOpener("remittance_fund:open", [entity: o] );
        }
    ] as BasicListModel;
    
    
    
}    