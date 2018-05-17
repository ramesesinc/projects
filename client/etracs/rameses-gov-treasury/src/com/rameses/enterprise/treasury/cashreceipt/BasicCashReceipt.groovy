package com.rameses.enterprise.treasury.cashreceipt; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.enterprise.models.*;
        
public class BasicCashReceipt extends AbstractCashReceipt {

    @Script("User")
    def user;
    
    @Service("QueryService")
    def queryService;
    
    @Service('RevenueSharingService')
    def sharingSvc;
    
    def queryFilter = [:];
    
    void init() {
        super.init();
        //check if there are collection type items.
        def m = [_schemaname:'collectiontype_account'];
        m.findBy = [collectiontypeid: entity.collectiontype.objid ];
        m.select = "account.objid";
        queryFilter.acctids = queryService.getList( m )*.account.objid;
        
        //MsgBox.alert( "org is " + user.env.ORGROOT );
    }

    def itemListModel = [
        fetchList: { o->
            return entity.items;
        },
        onAddItem: {o-> 
            o.objid = 'RCTI' + new java.rmi.server.UID();
            entity.items << o;
            entity.amount += o.amount;
            updateBalances();
        },
        isColumnEditable: { o,name-> 
            if ( name == 'amount' ) { 
                def valuetype = o?.item?.valuetype.toString().toUpperCase();
                if ( valuetype == 'FIXED' ) return false; 
            }
            return true; 
        }, 
        onColumnUpdate: {o,name-> 
            if(entity.items) {
                entity.amount = entity.items.sum{ it.amount };
                updateBalances();
            }
        },
        onRemoveItem: { o->
            if(!MsgBox.confirm("You are about to remove this entry. Proceed?")) 
                return false;
            entity.items.remove( o );
            if( entity.items ) {
                entity.amount = entity.items.sum{ it.amount };
            }
            else {
                entity.amount = 0;
            }
            updateBalances();
            return true;
        }
    ] as EditorListModel;
            
    def selectedItem;
    def getLookupItems() {
        def p = [:];
        p.put("query.txntype", "cashreceipt");
        p.put( "query.collectorid" , entity.collector.objid );
        p.put( "query.collectiontype" , entity.collectiontype );
        if( entity.collectiontype.fund?.objid ) {
            p.fundid = entity.collectiontype.fund?.objid;
        }
        p.queryFilter = queryFilter;    
        p.onselect = { o->
            selectedItem.item = o;
            selectedItem.amount = o.remove("amount");
            selectedItem.remarks = o.remove("remarks");
            /*
            selectedItem.amount = o.defaultvalue;
            if(o.valuetype == "FIXEDUNIT") {
                def m = MsgBox.prompt( "Enter qty" );
                if( !m || m == "null" ) throw new Exception("Please provide qty"); 
                if( !m.isInteger() ) throw new Exception("Qty must be numeric"); 
                selectedItem.amount = Integer.parseInt( m )*o.defaultvalue; 
                selectedItem.remarks = "qty@"+Integer.parseInt( m ); 
            } 
            */
        } 
        return InvokerUtil.lookupOpener("cashreceiptitem:lookup",p );
    }
    
    def getCollectionGroupHandler() {
        return InvokerUtil.lookupOpener("collectiongroup:lookup", [ 
            "query.txntype" : "cashreceipt", 
            "query.fund" : entity.collectiontype.fund, 
            "query.collectiontype": entity.collectiontype, 
            selectHandler: { o-> 
                    entity.items.addAll(o);
                    itemListModel.reload();
                    entity.amount = entity.items.sum{it.amount}
                    super.updateBalances(); 
            }]  );
    }
            
    def getTotalAmount() {
        return NumberUtil.round( entity.items.sum{ it.amount } );  
    }   
    
    
    //def rProcessor = new RuleProcessor( { params-> collectionRuleService.execute(params) } );
    
    void fireRules() {
        def params = [billdate:entity.receiptdate];
        boolean pass = false;
        def h = { o->
            params.collectiongroup = o;
            pass = true;
        }
        Modal.show("collectiongroup:lookup", [onselect:h]);
        if(!pass) return;
        def h1 = { x->
            MsgBox.alert("see result " + x);
        }
        def op = Inv.lookupOpener("assessment", [rulename:"collection", params: params, handler: h1 ] );
        Modal.show( op );
    }
    
   
    void viewSharing() {
        def sharing = entity.sharing; 
        if (!sharing) sharing = sharingSvc.execute( entity ); 
        if (!sharing) throw new Exception('No sharing rules defined'); 
        
        def lh = [
            getColumnList: {
                return [
                    [name:'refitem.title',caption:'Item Account'],
                    [name:'payableitem.title',caption:'Payable Account'],
                    [name:'amount',caption:'Amount', type:'decimal']
                ]
            },
            fetchList : {
                return sharing;
            }
        ] as BasicListModel;
        Modal.show("basiclist:view", [listHandler: lh])
    }
    
    
}