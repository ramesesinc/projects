package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;
import java.rmi.server.*;

//called by cash receipt list
class CashReceiptListSummaryModel  { 

    @Service("QueryService")
    def queryService;
    
    @Caller
    def caller;
    
    @Script("User")
    def user;
    
    def title;
    
    /*
    * states are as follows. Refer to CashReceiptListInterceptor for the source
    * unremitted
    * remitted
    * delegated 
    */
    def tag;
    def node;    
    def total = 0.0;
    def list;
    def selectedItem;
    
    def conditions;
    def openerName;
    
    void init() {
        openerName = "cashreceipt_list:view";
        tag = caller.tag;
        node = caller.selectedNode;
        title = "Cash Receipt Summary (" + node.title + ")";
        
        //updated list handler
        if( !node ) return;
        def qarr = [];
        def params = [userid: user?.userid ];
        if( tag == "collector") {
            qarr << " collector.objid = :userid ";
            if ( node.id == 'unremitted' ) { 
                qarr << " remittanceid is null AND state = 'POSTED' ";
            } else if ( node.id == 'delegated' ) { 
                qarr << " remittanceid is null and subcollector.objid is not null and subcollector.remittanceid is null "; 
            } else { 
                qarr << " remittanceid is not null "; 
            }            
        }
        else if( tag == "subcollector" ) {
            qarr << " subcollector.objid = :userid ";
            qarr << " subcollector.remittanceid is " + (node.id == 'unremitted' ? 'NULL' : 'NOT NULL');
        }
        else if ( node.id == 'unremitted' ) { 
            qarr << " remittanceid IS NULL "; 
        } else if( node.id == 'remitted' ){
            qarr << " remittanceid IS NOT NULL ";
        }
        
        conditions = [ qarr.join(" AND "), params ];
        
        def m = [_schemaname: "cashreceipt"];
        m.where = conditions;
        m.select = "formno,amount:{SUM(amount)}";
        m.groupBy = "formno";
        list = queryService.getList( m );
        total = list.sum{ it.amount };
    }
    
    void initRemittance() {
        openerName = "cashreceipt_list:remitted:view";
        conditions = [ "remittanceid = :remid", [remid: caller.entity.objid ] ];
        title = "Cash Receipt Summary";
        def m = [_schemaname: "cashreceipt"];
        m.where = conditions;
        m.select = "formno,amount:{SUM(amount)}";
        m.groupBy = "formno";
        list = queryService.getList( m );
        total = list.sum{ it.amount };
    }
    
    def listHandler = [
        fetchList:  { pp ->
            return list;
        },
        onOpenItem: { o,col->
            return viewReceipts(o);
        }
    ] as BasicListModel;
    
    def viewReceipts(def o) {
        def newcond = conditions[0]+" AND formno = :formno";
        def newparms = [:];
        newparms.putAll( conditions[1] );
        newparms.formno = o.formno;
        def op = Inv.lookupOpener(openerName, [customFilter: [ newcond, newparms ] ] );
        op.target = "popup";
        op.id = "RCT" + new UID();
        return op;
    }
    
    def viewReceipts() {
        if( !selectedItem ) throw new Exception("Please select an item");
        return viewReceipts(selectedItem);
    }
    
    def doClose() {
        return "_close";
    }
    
} 