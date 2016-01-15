package com.rameses.clfc.treasury.ledger.amnesty.preview

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.text.*;

abstract class AbstractAmnestyPreviewController
{
    @Binding
    def binding;
    
    abstract String getServiceName();
    abstract String getLedgerid();
    abstract String getAmnestyid();
    
    def getService() {        
        String name = getServiceName();
        if ((name == null) || (name.trim().length() == 0)) {
          throw new NullPointerException("Please specify a serviceName");
        }
        return InvokerProxy.getInstance().create(name);
    }
    
    def entity, currpageindex, pagenumber, pagecount;
    def data, list;
    
    void init() {
        //if (entity.amnesty) data = entity.amnesty;
        refresh();
    }
    
    void refresh() {        
        listHandler.reload();
        binding?.refresh('pagecount');
    }
    
    Map getColumnParams() {
        return [:];
    }
    
    int getRows() {
        return 30;
    }
    
    int getLastPageIndex() {
        return -1;
    }
    
    def listHandler = [        
        getRows: {
            return getRows(); 
        },
        getColumns: { 
            //def params = [type: data.amnestyoption]
            def params = getColumnParams();
            //return [];
            return service.getColumns(params); 
            
        },
        getLastPageIndex: { 
            /*
            if (!data.lastpageindex) data.lastpageindex = -1;
            return data.lastpageindex;
            */
            //if (!entity.lastpageindex) entity.lastpageindex = -1;
            //return entity.lastpageindex;
            return getLastPageIndex();
            
        },
        fetchList: { o->
            //println 'params ' + o;
            //if (entity) o.ledgerid = entity.objid; 
            //if (data) o.amnestyid = data.objid;
            o.ledgerid = getLedgerid();
            o.amnestyid = getAmnestyid();
            list = service.getList(o);
            //list = [];
            //buildHtmlview();
            return list;
        },
        onOpenItem: { itm, colName ->
            if (colName != 'remarks' || !itm.remarks) return null;
            
            return Inv.lookupOpener("remarks-preview", [remarks: itm.remarks]);
        }
    ] as PageListModel;
    
    void moveFirstPage() {
        listHandler.moveFirstPage();
        binding?.refresh('pagecount');
    }

    void moveBackPage() {
        listHandler.moveBackPage();
        binding?.refresh('pagecount');
    }

    void moveNextPage() {
        listHandler.moveNextPage();
        binding?.refresh('pagecount');
    }

    void moveLastPage() {
        currpageindex = listHandler.getLastPageIndex();
        listHandler.moveLastPage();
        binding?.refresh('pagecount');
    }
    
    def getPagecount() {
        //if (!list) return "Page 1 of ?";
        return "Page " + listHandler.getPageIndex() + " of " + listHandler.getLastPageIndex();
    }

    void goToPageNumber() {
        currpageindex = pagenumber
        if (pagenumber > listHandler.getLastPageIndex()) currpageindex = listHandler.getLastPageIndex();
        listHandler.moveToPage(currpageindex);
        binding?.refresh('pagecount');
    }
    
    def close() {
        return '_close';
    }
}

