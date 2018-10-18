/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.enterprise.accounts

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
       
public abstract class AccountRevenueMapping {
	
    public abstract def getService();
    public abstract String getLookupServiceName();
    
    def searchtext; 
    def selectedItem;

    def listModel = [
        fetchList: { o->
            o.searchtext = searchtext;
            return service.getRevenueItemList(o);
        }
    ] as EditorListModel;   
            
    void search() {
        listModel.reload();
    }
            
    def getLookupAccount() {
        def p = [:];
        boolean islov = false;
        p.onselect = { o->
            def z = [objid:selectedItem.objid];
            z.revenueitemid = selectedItem.objid; 
            z.account = o;
            service.create( z );
            selectedItem.account = z.account;
            return "_close";
        };
        p.onempty = { o->
            service.removeEntity( [objid: selectedItem.objid] );
            selectedItem.account = [:];
        }
        p.serviceName = lookupServiceName;
        
        return Inv.lookupOpener( "account:lookup", p ); 
     }
     
}

