package com.rameses.clfc.billinggroup

import com.rameses.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import java.util.*;

abstract class BillingGroupListController extends BasicListController
{
    abstract String getType();
    //abstract Object getService();
    abstract String getServiceName();
    
    public Object getService() {
        String name = getServiceName();
        if ((name == null) || (name.trim().length() == 0)) {
          throw new NullPointerException("Please specify a serviceName");
        }
        return InvokerProxy.getInstance().create(name);
    }

    def selectedLedgerToAdd = [:];
    private Map query = [:];
    
    public void setSelectedEntity( selectedEntity ) {
        super.setSelectedEntity(selectedEntity);
        this.selectedLedgerToAdd.clear();
        if (selectedEntity) {
            this.selectedLedgerToAdd.putAll(selectedEntity);
        }
    }
    
    public Map getQuery() {
        return this.query;
    }
    
    public void setQuery( query ) {
        this.query = query;
    }
    
    List fetchList(Map params) {
        def qrymap = getQuery();
        if (qrymap) params.putAll(query);
        params.type = getType();
        return getService().getList(params);
    }
    
    def getList( params ) {
        def qrymap = getQuery();
        if (qrymap) params.putAll(query);
        params.type = getType();
        return getService().getList(params);
    }
        
    void beforeGetColumns( Map params ) {
    }
    
    public List<Map> getColumnList() {
        Map params = new HashMap();
        
        params.type = getType();
        beforeGetColumns(params);
        return getService().getColumns(params);
    }
    
    /*
    void beforeGetColumns( Map params ) {
    }
    
    def getColumns() {
        def params = [:];
        beforeGetColumns(params);
        return getService().getColumns(params);
    }
    */
    
    /*
    void refresh() {
        listHandler?.reload();
    }
    
    def listHandler = [
        getColumns: {
            return getColumns();
        },
        fetchList: { o->
            return getList(o);
        }
    ] as BasicListModel;
    */
   
    public Opener getQueryForm() {
        Opener o = new Opener();
        o.setOutcome("queryform");
        return o;
    }

    /*
    private class CRUDServiceProxy  {
        Object serviceObj;
        MethodResolver resolver;
        private CRUDServiceProxy() { this.resolver = MethodResolver.getInstance(); }

        Object invoke(String methodName, Object data) {
          try {
            if (this.serviceObj == null)
                throw new NullPointerException("No available service object");
                def list = [];
                list.add(data);
            return this.resolver.invoke(this.serviceObj, methodName, list);
          }
          catch (Exception re) {
            Exception e = ExceptionManager.getOriginal(re);
            if (e instanceof RuntimeException) {
              throw ((RuntimeException)e);
            }
            throw new RuntimeException(e.getMessage(), e);
          }
        }
    }
    */
}

