package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class LiquidationModel extends CrudFormModel {

    @Service("LiquidationService")
    def liqSvc;    
    
    @Service("DepositService")
    def depositSvc; 
    
    def remittanceListHandler = [
        fetchList: { o->
            def m = [_schemaname:'remittance'];
            m.findBy = [liquidationid: entity.objid ];
            return queryService.getList( m );
        },
        onOpenItem: { o,col->
            def op = Inv.lookupOpener("remittance:open", [entity: o]);
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    def fundSummaryHandler = [
        fetchList: { o->
            def m = [_schemaname:'liquidation_fund'];
            m.findBy = [liquidationid: entity.objid ];
            return queryService.getList( m );
        },
        onOpenItem: { o,col->
            def op = Inv.lookupOpener("liquidation_fund:open", [entity: o]);
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    def submitForPosting() {
        liqSvc.post( entity );
    }

    
}    