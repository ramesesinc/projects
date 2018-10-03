package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class UnliquidatedRemittanceModel {

    @Binding
    def binding

    @Service("LiquidationService")
    def liqSvc;

    @Service("UnliquidatedRemittanceService")
    def svc;

    def nodes;
    def selectedNode;
    def selectedItem;
    def entity = [:];
    boolean captureMode;

    public String getTitle() {
        if ( captureMode ) {
            return "Unliquidated Capture Remittances"; 
        } else {
            return "Unliquidated Remittances"; 
        }
    }

    void search() {
        nodes = svc.getNodes();
        if ( nodes ) selectedNode = nodes[0];
    }

    void init() {
        captureMode = false;
        search();
    }
    
    void initCapture() {
        captureMode = true;
        search();
    }

    def listHandler = [
        fetchList: { o-> 
            def m = [:]; 
            if ( selectedNode ) m.putAll( selectedNode ); 
            
            m.capturemode = captureMode;
            entity = svc.getList( m );
            entity.remittances.each{ 
                def totalcash = (it.totalcash ? it.totalcash : 0.0);
                def totalnoncash = (it.totalnoncash ? it.totalnoncash : 0.0);
                it.totalamount = totalcash + totalnoncash;  
            } 
            binding.refresh("entity.*");
            return entity.remittances;
        },
        onOpenItem: { o,col->
            return viewRemittance();
        }
    ] as BasicListModel;

    def viewRemittance() {
        if(!selectedItem) throw new Exception('Select an Item first');
        def op = Inv.lookupOpener( "remittance:reportbyfund", [entity:selectedItem] );
        op.target = 'popup';
        op.properties.width = 800;
        op.properties.height = 650;
        return op;
    }

    def liquidate() { 
        def p = [ capturemode: captureMode ]; 
        if ( captureMode ) {
            p.pass =  false; 
            p.title = 'Enter Liquidation Date:';
            p.handler = { o-> 
                p.txndate = o; 
                p.pass = true; 
            } 
            Modal.show('date:prompt', p); 
            if ( !p.pass ) return null; 
        } 
        
        def h = { 
            listHandler.reload(); 
        } 
        return Inv.lookupOpener("liquidation:liquidate", [ entity: p, handler: h ]);
    }

    def refresh() {
        search();
        listHandler.load()
    }

    void hold() {
        if(!selectedItem) throw new Exception("Please select an item ");
        def p = MsgBox.prompt('Please enter reason for hold');
        if(!p) return;
        liqSvc.holdRemittance( [objid:selectedItem.objid, remarks: p ] );
        listHandler.reload();
    }

    void revert() {
        if(!selectedItem) throw new Exception("Please select an item ");
        liqSvc.unholdRemittance( [objid:selectedItem.objid ] );
        listHandler.reload();
    }

    void submitForLiquidation() {
        if(!selectedItem) throw new Exception("Please select an item ");
        liqSvc.submitForLiquidation( [objid:selectedItem.objid ] );
        listHandler.reload();
    }

    def getTotal() {
        def totalcash = (entity.totalcash ? entity.totalcash : 0.0);
        def totalnoncash = (entity.totalnoncash ? entity.totalnoncash : 0.0);
        return totalcash + totalnoncash;
    } 
}
