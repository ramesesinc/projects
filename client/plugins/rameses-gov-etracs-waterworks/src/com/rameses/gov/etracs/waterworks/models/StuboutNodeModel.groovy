package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class StuboutNodeModel {
    
    @Service('WaterworksStuboutService')
    def svc; 
    
    @Caller 
    def caller;

    def title = 'Nodes'; 
    def selectedItem;
    def nodeStat;
    def nodes;

    void init() {
        //do nothing 
    }
    
    def getEntity() {
        return caller?.entity; 
    } 
    
    void reload() { 
        nodes.clear();
        nodes  = null;
        listHandler.reload();
    }

    def listHandler = [
        fetchList: { o-> 
            if ( !nodes ) { 
                nodes = svc.getNodes([stuboutid: entity.objid]);
            } 
            return nodes; 
        }
    ] as BasicListModel;
    
    void addNodes() { 
        def i = MsgBox.prompt("Enter Number of Nodes to add");
        if ( !i ) return; 

        try {
            i.toInteger(); 
        } catch(Throwable t) {
            MsgBox.alert('Please enter a correct number value'); 
            return;  
        } 

        def m = [stuboutid: entity.objid, nodecount: i.toInteger() ];
        svc.addNodes(m); 
        reload(); 
    } 
    
    void assignAccount() {
        if( !selectedItem  ) return;
        def h = { o->
            svc.assignAccount( [objid:selectedItem.objid, acctid: o.objid] );
            reload();
        }
        Modal.show( "waterworks_account:lookup", [
            onselect: h, 
            sectorid: entity.zone?.sector?.objid, 
            stuboutid: entity.objid 
        ]);
    }
    
    void unassignAccount() {
        if( !selectedItem?.account?.acctname  ) return;
        svc.unassignAccount( [objid:selectedItem.objid] );
        reload();
    }
    
    boolean isAllowSwapUp() {
        if ( !selectedItem ) return false; 

        int idx = nodeStat.index;
        return ( idx > 0 && idx < nodes.size()); 
    }
    void swapUp() {
        if ( !selectedItem?.objid ) return; 

        int idx = nodeStat.index;
        if ( idx <= 0 ) return; 

        def m = [:];
        // store the source node as item1
        def n = nodes[idx]; 
        m.item1 = [ objid: n.objid, acctid: n.account?.objid ]; 
        // store the target node as item2
        n = nodes[idx-1];
        m.item2 = [ objid: n.objid, acctid: n.account?.objid ]; 
        svc.swap( m ); 

        def acct = nodes[idx].account; 
        nodes[idx].account = nodes[idx-1].account;
        nodes[idx-1].account = acct; 
        listHandler.reload();
        listHandler.setSelectedItem(idx-1);
    }
    
    boolean isAllowSwapDown() {
        if ( !selectedItem ) return false; 

        int idx = nodeStat.index;
        return ( idx >= 0 && (idx+1) < nodes.size()); 
    }     
    void swapDown() {
        if ( !selectedItem?.objid ) return;

        int idx = nodeStat.index;
        if ( (idx+1) >= nodes.size() ) return; 

        def m = [:];
        // store the source node as item1
        def n = nodes[idx]; 
        m.item1 = [ objid: n.objid, acctid: n.account?.objid ]; 
        // store the target node as item2
        n = nodes[idx+1];
        m.item2 = [ objid: n.objid, acctid: n.account?.objid ]; 
        svc.swap( m ); 

        def acct = nodes[idx].account; 
        nodes[idx].account = nodes[idx+1].account;
        nodes[idx+1].account = acct; 
        listHandler.reload();
        listHandler.setSelectedItem(idx+1);
    } 

    boolean isAllowSwapTo() {
        if ( !selectedItem ) return false; 

        return (nodes.size() > 1); 
    }
    void swapTo() {
        if ( !selectedItem ) return; 

        int idx = nodeStat.index;
        if ( idx < 0 || idx >= nodes.size()) return; 

        def o = MsgBox.prompt("Enter the node index number:");
        if ( !o ) return; 

        try {
            o.toInteger(); 
        } catch(Throwable t) {
            MsgBox.alert('Invalid number value'); 
            return;  
        } 

        def targetindexno = o.toInteger();
        def targetnode = nodes.find{ it.indexno==targetindexno } 
        if ( !targetnode ) throw new Exception('Index number not available. Please specify another number.'); 

        targetindexno = nodes.indexOf( targetnode ); 

        def m = [:];
        // store the source node as item1
        def n = nodes[idx]; 
        m.item1 = [ objid: n.objid, acctid: n.account?.objid ]; 

        // store the target node as item2
        m.item2 = [ objid: targetnode.objid, acctid: targetnode.account?.objid ]; 
        svc.swap( m ); 

        def acct = nodes[idx].account; 
        nodes[idx].account = nodes[targetindexno].account;
        nodes[targetindexno].account = acct; 

        listHandler.reload(); 
        listHandler.setSelectedItem( targetindexno ); 
    } 
}