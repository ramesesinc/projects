package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

public class StuboutNodeModel extends CrudFormModel {

    void afterCreate() {
        if( !caller.selectedStubout ) throw new Exception("Please select a stubout");
        entity.stubout = caller.selectedStubout;
        entity.stuboutid = entity.stubout.objid;
    }
    
    void afterSave() {
        caller.stuboutNodeListHandler.reload();
    }
    
    /*
    @Service('PersistenceService')
    def persistenceSvc; 
    
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
    */
    /*
    void reload() { 
        nodes.clear();
        nodes  = null;
        listHandler.reload();
    }
    */

    /*
    def listHandler = [
        fetchList: { o-> 
           return svc.getNodes([stuboutid: entity.objid]);
        }
    ] as BasicListModel;
    
    void addNodes() { 
        def p = [:]
        p.fields = [
            [name:'nodecount', type:'integer', caption:'No. of Nodes'],
            [name:'startnode', type:'integer', caption:'Start No'],
            [name:'interval', type:'integer', caption:'Interval'],
        ];
        p.data = [nodecount:1, startnode: 1, interval:1];
        p.handler = { o->
            o.stuboutid = entity.objid;
            svc.addNodes( o );
            listHandler.reload();
        }
        Modal.show("dynamic:form", p, [title:'Enter Nodes']);
    } 
    */
   
    /*
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
    listHandler.reload();
    */
    
    /*
    void editIndexNo() {
        if(!selectedItem) throw new Exception("Please select an item");
        def p = [:]
        p.fields = [
            [name:'value', type:'integer', caption:'Enter Index No']
        ];
        p.data = [value: selectedItem.indexno];
        p.handler = { o->
            svc.updateIndexNo( [objid:selectedItem.objid, indexno: o.value ] );
            listHandler.reload();
        }
        Modal.show("dynamic:form", p, [title:'Edit Index No']);
    }
    
    void removeNode() {
        if(!selectedItem) throw new Exception("Please select an item");
        if(!MsgBox.confirm('Are you sure you want to remove this entry?')) return;
        def m = [_schemaname: 'waterworks_stubout_node'];
        m.objid = selectedItem.objid;
        persistenceSvc.removeEntity( m );
        listHandler.reload();
    }
    */
    
    /*
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
    */
}