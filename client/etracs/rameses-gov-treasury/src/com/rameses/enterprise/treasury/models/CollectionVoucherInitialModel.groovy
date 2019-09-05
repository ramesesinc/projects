package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class CollectionVoucherInitialModel extends CrudListModel {

    @Service("CollectionVoucherService")
    def collSvc;    
    
    @Service('Var') 
    def varSvc; 
    
    def controldate;
    def datesList;
    def selectedRemittance;
    
    def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
    def totalAmount = 0.0;
    def remBalance = 0.0; 
    
    boolean liquidate_remittance_as_of_date; 
    
    @PropertyChangeListener
    def listener = [
        "controldate" : { o-> 
            buildRemittanceList(); 
            remittanceListHandler.reload();
        }
    ]
    
    def remittancelist = null; 
    void buildRemittanceList() {
        totalAmount = 0.0; 
        remBalance = 0.0;
        remittancelist = null; 
        if ( !controldate ) return;

        def db = new com.rameses.util.DateBean( controldate ); 
        def startdate = db.format("yyyy-MM-dd"); 
        
        db.add("1d"); 
        def enddate = db.format("yyyy-MM-dd"); 
        
        def m = [_schemaname: 'remittance' ];
        m.select = "objid,posted:{CASE WHEN state='POSTED' THEN 1 ELSE 0 END},controlno,controldate,dtposted,collector.name,amount,totalcash,totalcheck,totalcr,state"
        
        def qstr = " controldate >= :startdate AND controldate < :enddate "; 
        if ( liquidate_remittance_as_of_date ) qstr = " controldate < :enddate "; 
        
        m.where = [""+ qstr +" AND collectionvoucherid IS NULL AND state <> 'DRAFT' ", [ startdate: startdate, enddate: enddate ]];
        m.orderBy = "controldate, collector_name, dtposted";
        remittancelist = queryService.getList( m );
    }
    
    void buildDatesList() {
        def m = [_schemaname:'remittance'];
        m.select = "controldate";
        m.where = [" collectionvoucherid IS NULL AND state <> 'DRAFT' "];
        m.groupBy = "controldate";
        m.orderBy = "controldate"; 
        def xlist  = queryService.getList(m);
        if ( xlist ) datesList = xlist*.controldate.collect{ df.format(it) }.unique();
    }
    
    void afterInit() {
        def opt = varSvc.getProperty('liquidate_remittance_as_of_date', '0'); 
        liquidate_remittance_as_of_date = opt.toString().toLowerCase().matches('1|true');  

        buildDatesList();
        if ( !datesList ) throw new Exception("There are no remittances to liquidate"); 
        
        controldate = datesList.first();
        buildRemittanceList(); 
    }
    
    
    def remittanceListHandler = [
        fetchList: { o->
            if( !controldate ) return [];
            
            buildTotals(); 
            return remittancelist;
        },
        onOpenItem: {o,col->
            viewRemittance();
        }
    ] as BasicListModel;
    
    void buildTotals() {
        def liqamt = 0.0; 
        def remamt = 0.0; 
        remittancelist.each{ 
            if ( it.posted.toString().matches('1|true')) {
                liqamt += (it.amount ? it.amount : 0.0); 
            } else {
                remamt += (it.amount ? it.amount : 0.0); 
            }
        } 
        remBalance = remamt;
        totalAmount = liqamt;
        binding.notifyDepends('totals'); 
    }
    
    void viewRemittance() {
        if(!selectedRemittance) throw new Exception("Please select a remittance");
        def h1 = { 
            selectedRemittance.posted = 1;
            selectedRemittance.state = 'POSTED';
            remittanceListHandler.reload();
            binding.notifyDepends('totals'); 
        };
        def h2 = { 
            buildDatesList();
            binding.refresh('controldate'); 
            
            buildRemittanceList(); 
            remittanceListHandler.reload(); 
            binding.notifyDepends('totals'); 
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