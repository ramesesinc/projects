package com.rameses.enterprise.treasury.models; 

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class BatchCaptureCollectionEntryModel extends com.rameses.seti2.models.CrudFormModel {

    //to be feed by the caller
    boolean openForEditing;
    def fund;

    @Service('DateService') 
    def dateSvc; 

    def selectedItem;
    def selectedCheck;

    boolean isInEditingMode() { 
        return mode.toString().matches('create|edit'); 
    } 
    
    void resolvePaymentItems() {
        if ( entity.paymentitems == null ) entity.paymentitems = []; 
    }
    
    void afterCreate() { 
        if ( caller ) {
            entity.putAll( caller.createItem()); 
        }
        resolvePaymentItems(); 
        rebuildTotals(); 
    } 
    
    def open() {
        super.open();
        resolvePaymentItems(); 
        if ( openForEditing == true ) {
            edit(); 
        } 
        return null; 
    }

    def getTotalAmount() {
        rebuildTotals(); 
        return entity.amount;  
    } 

    def getLookupAccount() { 
        def parentEntity = caller.entity; 
        return InvokerUtil.lookupOpener("cashreceiptitem:lookup", [ 
            "query.txntype"     : "cashreceipt", 
            "query.collectorid" : parentEntity.collector.objid,
            "query.collectiontype": parentEntity.collectiontype,
            "query.fund" : parentEntity.collectiontype?.fund,
            onselect: { o->
                selectedItem.item = o;
                selectedItem.fund = o.fund;
                selectedItem.amount = o.defaultvalue; 
                selectedItem.valuetype = o.valuetype; 
            }
        ]);
    }

    def listModel = [
        fetchList: { o->
            if (entity.items == null) entity.items = []; 

            return entity.items;
        },
        isColumnEditable: { o,colname-> 
            if ( !isInEditingMode() ) return false; 
            if ( colname != 'amount' ) return true; 

            def valuetype = o.valuetype.toString().toUpperCase();
            return ( valuetype == 'FIXED'? false : true ); 
        }, 
        validateItem: { o-> 
            validateReceiptItem( o ); 
        }, 
        addItem: {o-> 
            o.uid = "ITM" + new java.rmi.server.UID();
            entity.items << o;
        },
        commitItem:{o-> 
            rebuildTotals(); 
        }, 
        onRemoveItem:{o-> 
            if ( !MsgBox.confirm("You are about to remove this entry. Proceed?") ) return false;

            entity.items.remove( o ); 
            entity.amount = entity.items.sum{( it.amount? it.amount: 0.0 )} 
            binding.refresh("entity.amount"); 
            return true; 
        } 
    ] as EditorListModel;

    void validateReceiptItem( item ) {
        if ( !item.item?.objid ) throw new Exception('Account is required     '); 
        if ( item.amount==null ) throw new Exception('Amount is required      '); 
    }

    void rebuildTotals() {
        entity.amount = entity.items?.sum{( it.amount? it.amount: 0.0 )} 
        if ( entity.amount == null ) entity.amount = 0.0;

        entity.totalnoncash = entity.paymentitems.sum{( it.amount? it.amount: 0.0 )} 
        if ( entity.totalnoncash == null ) entity.totalnoncash = 0.0; 
        
        if ( entity.totalnoncash > entity.amount ) {
            throw new Exception('Your check amount must be equal to total amount'); 
        } 

        entity.totalcash = entity.amount - entity.totalnoncash;     
    } 

    void validateCheckPayment() { 
        def totalnoncash = 0.0; 
        entity.paymentitems.each{ o-> 
            if ( !o.bank?.objid  ) throw new Exception("Bank is required.");
            if ( !o.refno ) throw new Exception("Check No is required.");
            if ( !o.refdate ) throw new Exception("Check Date is required.");
            if ( !o.amount) throw new Exception("Check Amount is required.");

            if ( !(o.amount > 0.0) ) 
                throw new Exception("Check Amount must be greater than zero       ");

            totalnoncash += o.amount; 
        } 
        entity.totalnoncash = totalnoncash;
        
        def amount = entity.items?.sum{( it.amount? it.amount: 0.0 )} 
        if ( amount == null ) amount = 0.0;
        
        if ( totalnoncash > amount ) 
            throw new Exception("Check Amount must not be greater than amount to be paid.  "); 
    } 
    
    def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
    def formatDate = { o-> 
        if ( o == null ) return null; 
        if ( o instanceof java.util.Date ) {
            return df.parse( df.format( o ));
        } 
        return df.parse( o );     
    }

    void beforeSave( mode ) { 
        def rdate = formatDate( entity.receiptdate ); 
        if ( !rdate ) throw new Exception("Please enter a correct receipt date ");

        def serverDate = dateSvc.getBasicServerDate(); 
        if ( rdate.after( serverDate)) 
            throw new Exception('Receipt date must not be greater than the current date'); 
        
        def defaultdate = formatDate(caller.entity.defaultreceiptdate);         
        if ( rdate.before(defaultdate)) 
           throw new Exception("Receipt date must not be less than the default date."); 

        if ( caller.entity.batchitems.isEmpty()) { 
            if ( rdate != defaultdate ) 
                throw new Exception("Receipt date must be equal to the default date"); 
        
        } else {
            def lastdate = formatDate( caller.entity.batchitems.last().receiptdate ); 
            if ( rdate.before( lastdate)) {
                throw new Exception("Receipt date must not be lesser than the previous entry date"); 
            }
        }
        
        
        if ( !entity.items ) 
            throw new Exception("Please specify at least one item"); 
        
        entity.items.each { 
            if ( !it.item ) throw new Exception("Account is required     ");  
            if ( !it.amount ) throw new Exception("Amount must not be equals to zero ");  
        } 

        validateCheckPayment();
        entity.totalcash = entity.amount - entity.totalnoncash;
    }
    
    protected boolean isShowConfirm() { 
        return false; 
    }
    
    def save() {
        super.save(); 

        caller.updateBatchItem( entity ); 
        if ( openForEditing ) return '_close'; 
        
        entity.items.clear(); 
        create(); 
        listModel.reload(); 
        binding.focus('entity.receiptdate'); 
        return null;
    }

    def doCancel() {
        return "_close";
    }

    def getCollectionGroupHandler() {
        return InvokerUtil.lookupOpener("collectiongroup:lookup", [ 
            selectHandler: { items-> 
                items.each{ it.fund = it.item.fund } 
                if ( fund?.objid ) { 
                    items = items.findAll{ it.fund?.objid==fund.objid }
                } 
                entity.items.addAll( items );
                rebuildTotals(); 
                listModel.reload(); 
                if (binding) binding.refresh(".*");
            }
        ]);
    }

    def getLookupBank(){
        return InvokerUtil.lookupOpener('bank:lookup', [
            onselect : {
                payment = [:]
                payment.bankid = it.objid;
                payment.bank   = it.code;
                payment.deposittype = it.deposittype;
            },

            onempty  : {
                payment = [:] 
                binding.refresh("payment.*");
            }
        ]); 
    }

    def clearAll() {
        entity.items = []; 
        entity.amount = 0; 
        rebuildTotals(); 
        listModel.reload();
        binding.refresh("entity.*");
    } 
    
    def summarizeByFund() { 
        def fg = [];
        entity.items.groupBy{ it.fund }.each{ k,v->
            fg << [fund:k, amount: v.sum{ it.amount }];
        }
        return fg;
    } 
    
    void addCheck() {
        def handler = { o-> 
            def totalnoncash = 0.0; 
            o.paymentitems.each{ itm-> 
                totalnoncash += itm.amount; 
                itm.bank = itm.check?.bank; 
            }
            
            entity.totalnoncash = totalnoncash;  
            entity.totalcash = o.totalcash; 
            entity.paymentitems = o.paymentitems; 
            entity.checks = o.checks; 
            checkListHandler.reload(); 
        }
        Modal.show( "cashreceipt:payment-check", [entity: entity, saveHandler: handler, fundList:summarizeByFund() ] ); 
    }
    
    void removeCheck() {
        if( selectedCheck ) {
            entity.paymentitems.remove( selectedCheck );  
            checkListHandler.reload(); 
        }
    }
    
    boolean isAllowRemoveCheck() {
        if ( !isInEditingMode()) return false; 
        if ( !selectedCheck ) return false; 
        return true; 
    }
    
    def checkListHandler = [
        fetchList: { o-> 
            return entity.paymentitems; 
        }
    ] as BasicListModel; 
}
