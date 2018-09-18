package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;

//called by cash receipt list
class CashReceiptListSummaryModel  { 

    @Service("QueryService")
    def queryService;
    
    @Caller
    def caller;
    
    @Script("User")
    def user;
    
    @FormTitle
    String formTitle;
    
    /*
    * states are as follows. Refer to CashReceiptListInterceptor for the source
    * unremitted
    * remitted
    * delegated 
    */
    def tag;
    def node;
    
    void init() {
        tag = caller.tag;
        node = caller.selectedNode;
        formTitle = "Cash Receipt Summary (" + node.title + ")";
    }
    
    def listHandler = [
        getColumns: {
            return [
                [ name:'formno', caption:'Form No' ],
                [ name:'amount', caption:'Amount', type:'decimal']
            ];
        },
        fetchList:  { pp ->
            if( !node ) return [];
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
            def m = [_schemaname: "cashreceipt"];
            m.where = [ qarr.join(" AND "), params ];
            m.select = "formno,amount:{SUM(amount)}";
            m.groupBy = "formno";
            return queryService.getList( m );
        }    
    ] as BasicListModel;
    
    
} 