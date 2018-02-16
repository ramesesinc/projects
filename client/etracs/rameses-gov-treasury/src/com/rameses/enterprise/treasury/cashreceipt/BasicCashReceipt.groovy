package com.rameses.enterprise.treasury.cashreceipt; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.enterprise.models.*;
        
public class BasicCashReceipt extends AbstractCashReceipt {

    @Service('RevenueSharingService')
    def sharingSvc;
    
    void init() {
        super.init();
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
        return InvokerUtil.lookupOpener("cashreceiptitem:lookup",[
            "query.txntype" : "cashreceipt",
            "query.collectorid" : entity.collector.objid,
            "query.fund" : entity.collectiontype.fund,
            "query.collectiontype": entity.collectiontype, 
            onselect:{ o->
                selectedItem.item = o;
                selectedItem.amount = o.defaultvalue;
                if(o.valuetype == "FIXEDUNIT") {
                    def m = MsgBox.prompt( "Enter qty" );
                    if( !m || m == "null" ) throw new Exception("Please provide qty"); 
                    if( !m.isInteger() ) throw new Exception("Qty must be numeric"); 
                    selectedItem.amount = Integer.parseInt( m )*o.defaultvalue; 
                    selectedItem.remarks = "qty@"+Integer.parseInt( m ); 
                } 
            } 
        ]); 
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
    
    def rProcessor = new RuleProcessor( { params-> collectionRuleService.execute(params) } );
    void fireRules() {
        boolean pass = false;
        def params = [:];
        def h = { o->
            params.collectiongroup = o;
            pass = true;
        }
        Modal.show("collectiongroup:lookup", [onselect:h]);
        if( pass ) {
            def result = rProcessor.execute( params );
            if(!result?.billitems) {
                 throw new Exception("No results fired");
            }
            else {
                result.billitems.each { itm->
                    entity.items << [item: itm.item, amount: itm.amount, remarks:itm.remarks];
                }
                itemListModel.reload();
                updateBalances();
            }

        }
    }
    
    void viewSharing() {
        def sharing = entity.sharing; 
        if (!sharing) sharing = sharingSvc.execute( entity ); 
        if (!sharing) throw new Exception('No sharing rules defined'); 
        
        def lh = [
            getColumnList: {
                return [
                    [name:'refaccount.title',caption:'Item Account'],
                    [name:'payableaccount.title',caption:'Payable Account'],
                    [name:'share',caption:'Share'],
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