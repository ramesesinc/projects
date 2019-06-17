package com.rameses.gov.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;

class CollectionSummaryModel {

    @Binding
    def binding;
    
    @Invoker 
    def invoker;
    
    @Service('CollectionSummaryService')
    def svc;
    
    @Service('DateService') 
    def dateSvc; 
    
    def entity;
    
    def receiptCount = 0;
    def totalCollection = 0.0;
    def cashTendered = 0.0;
    def change = 0.0;
    def items; 
    
    def query;
    boolean multi_select; 
    
    String title = 'Collection Summary Information'; 
    
    @PropertyChangeListener
    def listener = [
        'receiptCount' : { getReceiptSummary() },
        'cashTendered' : { calcChange() },
    ] 
    
    def init() { 
        query = [ fromdate: dateSvc.getBasicServerDate() ];
        query.todate = query.fromdate; 
        
        if (invoker?.properties?.tag) { 
            multi_select = true;
            return invoker.properties.tag; 
        }
        return 'default'; 
    }
    
    def listHandler = [
        isMultiSelect: { 
            return multi_select; 
        }, 
        fetchList : { 
            return items;  
        },
        afterSelectionChange: {
            updateTotals( listHandler.selectedValue ); 
            binding?.notifyDepends('totals'); 
        }
    ] as BasicListModel
    
    
    void updateTotals( list ) {
        totalCollection = 0.0; 
        cashTendered = 0.0;
        change = 0.0;
        
        if ( list ) { 
            totalCollection = list.sum{( it.amount ? it.amount : 0.0 )}
        }
    }
    
    void getReceiptSummary(){
        if (receiptCount == null || receiptCount == 0){
            clearInfo();
        }
        else {
            items = svc.getCollectionsByCount(receiptCount); 
            updateTotals( items ); 

            listHandler.load();
            binding?.notifyDepends('totals'); 
            binding?.focus('cashTendered');
        }
    }
    
    void clearInfo(){
        totalCollection = 0.0;
        cashTendered = 0.0;
        change = 0.0;
        items = [];
        listHandler.reload();
        binding?.refresh();
    }
    
    void calcChange(){
        change = 0.0
        if (cashTendered == null ) cashTendered = 0.0
        if ( cashTendered > totalCollection )
            change = cashTendered - totalCollection;
        binding?.refresh();
    }
        
    void search() {
        items = svc.getCollections( query ); 
        listHandler.load(); 
    }
    
    void clearSearch() { 
        items?.clear(); 
        query.clear(); 
        listHandler.load(); 
    }
}