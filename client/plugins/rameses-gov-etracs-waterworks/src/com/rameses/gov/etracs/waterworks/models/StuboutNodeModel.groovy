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
    def nodes;
    def selectedItem;
    
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
        if(!i) return;
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
        Modal.show( "waterworks_account:lookup", [onselect: h] );
    }
    
    void unassignAccount() {
        if( !selectedItem?.account?.acctname  ) return;
        svc.unassignAccount( [objid:selectedItem.objid] );
        reload();
    }
    
    void swapUp() {
        if(!selectedItem?.objid) return;
        int idx = selectedItem.indexno;
        if(idx==1) return;
        def m = [:];
        def z = nodes[idx-1];   //current item bec. it is base 1
        def z1 = nodes[idx-2];  //before item 
        m.item1 = [objid: z.objid, acctid: z.account.objid];
        m.item2 = [objid: z1.objid, acctid: z1.account.objid];
        svc.swap( m );
        def acct = z.account;
        z.account = z1.account;
        z1.account = acct;
        listHandler.reload();
        listHandler.setSelectedItem(idx-2);
    }
    
    void swapDown() {
        if(!selectedItem?.objid) return;
        int idx = selectedItem.indexno;
        if(idx==nodes.size()) return;
        def m = [:];
        def z = nodes[idx-1];   //current item
        def z1 = nodes[idx];    //after item
        m.item1 = [objid: z.objid, acctid: z.account.objid];
        m.item2 = [objid: z1.objid, acctid: z1.account.objid];
        svc.swap( m );
        def acct = z.account;
        z.account = z1.account;
        z1.account = acct;
        listHandler.reload();
        listHandler.setSelectedItem(idx);
    }

}