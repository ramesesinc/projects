package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class CollectionVoucherInitialModel extends CrudListModel {

    @Service("CollectionVoucherService")
    def collSvc;    
    
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
        m.where = [" collectionvoucherid IS NULL"];
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
            m.where = ["collectionvoucherid IS NULL AND controldate =:cdate", [cdate:controldate] ];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("remittance:preview", [entity: o] );
            op.target = "popup";
            Modal.show( op );
            remittanceListHandler.reload();
        }
    ] as BasicListModel;
    
    def submitForLiquidation() {
        def p = [controldate: controldate]
        p = collSvc.create(p);
        return Inv.lookupOpener( "collectionvoucher:open", [entity: p ]);
    }

    
}    