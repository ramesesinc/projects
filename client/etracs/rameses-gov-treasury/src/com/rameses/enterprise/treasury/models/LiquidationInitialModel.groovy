package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class LiquidationInitialModel extends CrudListModel {

    @Service("LiquidationService")
    def liqSvc;    
    
    def controldate;
    def datesList;
    
    def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
    
    @PropertyChangeListener
    def listener = [
        "controldate" : { o->
            remittanceListHandler.reload();
        }
    ]
    
    void buildDatesList() {
        def m = [_schemaname:'remittance'];
        m.select = "controldate";
        m.where = ["state = 'OPEN' AND liquidationid IS NULL"];
        m.groupBy = "controldate";
        def xlist  = queryService.getList(m);
        if(xlist ) datesList = xlist*.controldate.collect{ df.format(it) };
    }
    
    void afterInit() {
        buildDatesList();
        if(!datesList) 
            throw new Exception("There are no remittances to liquidate")
    }
    
    def remittanceListHandler = [
        fetchList: { o->
            if( !controldate ) return [];
            def m = [_schemaname: 'remittance' ];
            m.where = ["liquidationid IS NULL AND controldate =:cdate", [cdate:controldate] ];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("remittance:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    def submitForLiquidation() {
        def p = [controldate: controldate]
        p = liqSvc.create(p);
        return Inv.lookupOpener( "liquidation:open", [entity: p ]);
    }

    
}    