package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
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
            buildRemittanceList(); 
            remittanceListHandler.reload();
        }
    ]
    
    def remittancelist = null; 
    void buildRemittanceList() {
        remittancelist = null; 
        if ( !controldate ) return;
        def m = [_schemaname: 'remittance' ];
        m.select = "objid,posted:{CASE WHEN state='POSTED' THEN 1 ELSE 0 END},controlno,controldate,collector.name,amount,totalcash,totalcheck,totalcr,state"
        m.where = [" NOT(state = 'DRAFT') AND collectionvoucherid IS NULL AND controldate =:cdate", [cdate:controldate] ];
        remittancelist = queryService.getList( m );
    }
    
    void buildDatesList() {
        def m = [_schemaname:'remittance'];
        m.select = "controldate";
        m.where = [" NOT(state = 'DRAFT') AND collectionvoucherid IS NULL"];
        m.groupBy = "controldate";
        m.orderBy = "controldate"; 
        def xlist  = queryService.getList(m);
        if ( xlist ) datesList = xlist*.controldate.collect{ df.format(it) }.unique();
    }
    
    void afterInit() {
        buildDatesList();
        if(!datesList) 
            throw new Exception("There are no remittances to liquidate"); 
            
        controldate = datesList.first();
        buildRemittanceList(); 
    }
    
    
    def remittanceListHandler = [
        fetchList: { o->
            if( !controldate ) return [];
            return remittancelist;
        },
        onOpenItem: {o,col->
            viewRemittance();
        }
    ] as BasicListModel;
    
    void viewRemittance() {
        if(!selectedRemittance) throw new Exception("Please select a remittance");
        def h1 = { 
            selectedRemittance.posted = 1;
            selectedRemittance.state = 'POSTED';
            remittanceListHandler.reload();
        };
        def h2 = { 
            buildDatesList();
            binding.refresh('controldate'); 
            
            buildRemittanceList();
            remittanceListHandler.reload();
        } 
        def op = Inv.lookupOpener("remittance:preview", [entity: selectedRemittance, onAccept: h1, onReject: h2 ]);
        op.target = "popup";
        Modal.show( op );
    }
    
    def submitForLiquidation() {
        def p = [controldate: controldate]; 
        p.ids = remittancelist.findAll{( it.posted == 1 )}*.objid; 
        if ( !p.ids ) throw new Exception('No available posted remittances'); 
        
        p = collSvc.create(p);
        remittanceListHandler.reload();
        def op = Inv.lookupOpener( "collectionvoucher:open", [entity: p ]);
        op.target = 'self';
        buildDatesList();
        return op;
    }

    
}    