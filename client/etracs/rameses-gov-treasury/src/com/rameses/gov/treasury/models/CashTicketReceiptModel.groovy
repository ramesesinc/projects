package com.rameses.gov.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.enterprise.treasury.cashreceipt.*;

class CashTicketReceiptController {

    @Service("CashReceiptService")
    def service;

    @Binding
    def binding;

    String title = "Cash Ticket " ;
    def itemAmount = 0.0;
    def entity;

    def mode = "init";
    def infoHtml;

    @PropertyChangeListener
    def listener = [
        "entity.amount": { o->
            if( ( (double)o % (double)entity.denomination)!=0) {
                MsgBox.err("Amount must be exact by denomination");
                entity.amount = 0.0
                binding.refresh("entity.amount")
                return false;
            }    
            entity.qtyissued = (int)  ((double)o / (double)entity.denomination);
            binding.refresh("entity.qtyissued");
        },
        "entity.qtyissued": { o->
            entity.amount = o * entity.denomination;
            binding.refresh("entity.amount");
        },
    ]

    def itemListModel = [
        fetchList: { o->
            return entity.items;
        },
        onAddItem: {o-> 
            entity.items << o;
            itemAmount += o.amount;
        },
        onColumnUpdate: {o,name-> 
            if(entity.items) {
                itemAmount = entity.items.sum{ it.amount };
            }
        },
        onRemoveItem: { o->
            if(!MsgBox.confirm("You are about to remove this entry. Proceed?")) 
                return false;
            entity.items.remove( o );
            itemAmount = 0;
            if( entity.items ) {
               itemAmount = entity.items.sum{ it.amount };
            }
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
                if( entity.items.find{ it.item.objid == o.objid }!=null )
                    throw new Exception("This item has already been added");
                selectedItem.item = o;
            }
        ]); 
    }

    def save() {
        if(!entity.items)
            throw new Exception("Please specify at least one item");
        if(itemAmount!= entity.amount)
            throw new Exception("Total of items must be equal to amount collected");

        if( MsgBox.confirm('You are about to post this transaction. Continue?')) { 
            if ( !entity.paidby ) entity.paidby = entity.collector?.name;
            if ( !entity.paidbyaddress ) entity.paidbyaddress = "-";

            entity = service.post( entity );
            mode = "completed"; 
            MsgBox.alert( "Receipt has been successfully saved.");
            infoHtml = TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/cashreceipt/cashreceipt.gtpl", [entity:entity] );
            binding.refresh("infoHtml");
            return "completed";
        }
    }

    def report;     
    def print() {
        //check first if form handler exists.
        def o = Inv.lookupOpener( "cashreceipt-form:"+entity.formno, [ reportData: entity ] );
        if ( !o ) throw new Exception("Handler not found");
        
        if ( entity.receiptdate instanceof String ) { 
            // this is only true when txnmode is OFFLINE 
            try {
                def YMD = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
                def dateobj = YMD.parse( entity.receiptdate ); 
                entity.receiptdate = dateobj; 
            } catch( Throwable t ) {;} 
        } 

        def handle = o.handle;
        def opt = handle.viewReport(); 
        if ( opt instanceof Opener ) { 
            // 
            // possible routing of report opener has been configured 
            // 
            handle = opt.handle; 
            handle.viewReport(); 
        } 
        
        if ( handle instanceof com.rameses.osiris2.reports.ReportModel ) {
            report = handle; 
        } else { 
            report = handle.report; 
        } 
        
        if ( report instanceof com.rameses.osiris2.reports.ReportModel ) { 
            report.print(); 
        } else { 
            ReportUtil.print(report, true); 
        } 
        return null; 
    } 
} 
