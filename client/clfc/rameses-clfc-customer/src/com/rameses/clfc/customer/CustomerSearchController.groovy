package com.rameses.clfc.customer;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CustomerSearchController extends BasicLookupModel 
{
    //to be feed by the caller
    
    @Service('CustomerService')
    def service;
    
    def mode = 'search';
    
    void init() { 
        
    } 

    boolean show(String searchtext) {
        customerlistHandler.searchtext = searchtext; 
        return true; 
    }
    
    def getTitle() {
        def s = (mode == 'search'? 'Search Customers': 'Customer');
        return '<font color="#808080" size="5"><b>'+s+'</b></font><br>'; 
    } 
    
    def getValue() { 
        return customerlistHandler.selectedValue; 
    } 
    
    def selectedCustomer; 
    def customerlistHandler = [ 
        getRows: { return 15; },             
        fetchList: {o-> 
            return service.getList(o);  
        }, 
        onOpenItem: {item,colname-> 
            select();
        }
    ] as PageListModel;

    void search() { 
        customerlistHandler.reload(); 
    } 
    
    void moveFirstPage() {  
        customerlistHandler.moveFirstPage(); 
    } 
    
    void moveBackPage() { 
        customerlistHandler.moveBackPage(); 
    } 
    
    void moveNextPage() {  
        customerlistHandler.moveNextPage(); 
    } 
    
    void moveLastPage() {}     
   
    def createContextHandler() {
        def ctx = new CustomerSearchContext(this);
        ctx.selectHandler = {o-> 
            select(o);
            customerlistHandler.bindingObject.fireNavigation('_close');
        }
        return ctx;
    }
    
    def create() {
        def params = [callerContext: createContextHandler()];
        def opener = InvokerUtil.lookupOpener('customer:create', params);
        opener.target = 'self';
        return opener;
    }
    
    void view() {
        def params = [
            callerContext: createContextHandler(), 
            entity: selectedCustomer 
        ];
        params.callerContext.closeHandler = {
            customerlistHandler.reload(); 
        }         
        
        def opener = InvokerUtil.lookupOpener('customer:open', params);
        opener.target = 'self';
        customerlistHandler.bindingObject.fireNavigation(opener);
    }    
} 