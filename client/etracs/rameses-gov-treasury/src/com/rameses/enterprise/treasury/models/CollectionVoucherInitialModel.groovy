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
    def selectedRemittance;
    
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
        m.where = [" NOT(state = 'DRAFT') AND collectionvoucherid IS NULL"];
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
            m.select = "objid,posted:{CASE WHEN state='POSTED' THEN 1 ELSE 0 END},controlno,controldate,collector.name,amount,totalcash,totalcheck,totalcr,state"
            m.where = [" NOT(state = 'DRAFT') AND collectionvoucherid IS NULL AND controldate =:cdate", [cdate:controldate] ];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            viewRemittance();
        }
    ] as BasicListModel;
    
    void viewRemittance() {
        if(!selectedRemittance) throw new Exception("Please select a remittance");
        def h = { 
            buildDatesList();
            remittanceListHandler.reload();
        };
        def op = Inv.lookupOpener("remittance:preview", [entity: selectedRemittance, onAccept: h, onReject: h ]);
        op.target = "popup";
        Modal.show( op );
    }
    
    def submitForLiquidation() {
        def p = [controldate: controldate]
        p = collSvc.create(p);
        remittanceListHandler.reload();
        def op = Inv.lookupOpener( "collectionvoucher:open", [entity: p ]);
        op.target = 'self';
        buildDatesList();
        return op;
    }

    
}    