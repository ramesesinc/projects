package com.rameses.clfc.customer

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CustomerListController extends BasicListController
{
    @Service('CustomerService')
    def service;
    
    private StringBuffer recordCountInfo, pageCountInfo;
    
    String invName = 'customer';
    
    public List fetchList(Map params) {
        if (!params) params = [:];
        return service.getList(params);
    }
    
    public List getColumnList() {
        def params = [:];
        return service.getColumns(params);
    }
    
    public Object getRecordCountInfo() { return this.recordCountInfo; } 
    public Object getPageCountInfo() { return this.pageCountInfo; }

    void dataChanged(Object stat)
    {
      this.recordCountInfo = new StringBuffer();
      this.pageCountInfo = new StringBuffer();
      if (stat instanceof ListItemStatus) {
        ListItemStatus lis = (ListItemStatus)stat;
        this.recordCountInfo.append(lis.getTotalRows());
        this.recordCountInfo.append(" Record(s)    ");

        this.pageCountInfo.append("Page  " + lis.getPageIndex() + "  of  ");
        if (lis.isHasNextPage())
          if (lis.getPageIndex() < lis.getPageCount())
            this.pageCountInfo.append(lis.getPageCount());
          else
            this.pageCountInfo.append("?");
        else
          this.pageCountInfo.append(lis.getPageCount());
      }
    }
    
    int getRows() { return 20; }
    
    def close() { return '_close'; }
    
    def createContextHandler() {
        def ctx = new CustomerSearchContext(this);
        return ctx;
    }
    
    def createOpenerParams() {
        def params = [
            listModelHandler: this,
            entity          : selectedEntity,
            callerContext   : createContextHandler()
        ];
        return params;
    }
    
    def open() {
        def params = createOpenerParams();
        def opener = Inv.lookupOpener(invName + ':open', params);
        if (!opener) return null;
        opener.target = 'popup';
        return opener;
    }
    
    def create() {
        def params = createOpenerParams();
        def opener = Inv.lookupOpener(invName + ':create', params);
        if (!opener) return null;
        
        opener.target = 'popup';
        return opener;
    }
    
    /*
    @Service('CustomerService')
    def svc;

    String serviceName = 'CustomerService';
    String entityName = 'customer';

    Map createPermission = [role: 'ENTITY_ENCODER', domain: 'ENTITY'];

    def createContextHandler() {
        def ctx = new CustomerSearchContext(this);
        return ctx;
    }
    Map createOpenerParams() {
        def params = super.createOpenerParams();
        params.callerContext = createContextHandler();
        return params;
    }
    */
}

