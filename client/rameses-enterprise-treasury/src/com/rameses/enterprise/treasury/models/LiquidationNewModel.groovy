package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class LiquidationNewModel  { 
  
    @Binding
    def binding

    @Service("LiquidationService")
    def liqSvc;

    @Service("UnliquidatedRemittanceService")
    def svc;

    @Service("RemittanceService")
    def remittanceSvc;
    
    def params=[:];
    def list;
    def nodes;
    def selectedNode;
    def selectedItem;
    def entity = [:];

    String title = "Unliquidated Remittances";

    
    
   
    void init() {
        search();
    }

    def listHandler = [
        fetchList: { o ->
            entity = svc.getList(selectedNode);
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

    def search() {
        nodes = svc.getNodes();
        if(nodes) selectedNode = nodes[0];
    }

    def viewRemittance() {
        if(!selectedItem) throw new Exception('Select an Item first');
        //def rem = remittanceSvc.open( [objid:selectedItem.objid ] );
        //def op = Inv.lookupOpener( "remittance:rcd", [entity:rem] );
        def op = Inv.lookupOpener( "remittance:open", [entity:selectedItem] );
        op.target = 'popup';
        op.properties.width = 800;
        op.properties.height = 650;
        return op;
    }

    def liquidate() {
        def h = { 
            listHandler.reload();
        }
        return Inv.lookupOpener("liquidation:liquidate", [entity: entity, handler: h] );
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