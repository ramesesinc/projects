package com.rameses.clfc.ledger.fixpreview

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.text.*;

class LoanLedgerFixPreviewController
{
    @Binding
    def binding;
    
    @Service("LoanLedgerFixPreviewService")
    def service;
    
    String getTitle() {
        return 'Notes';
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
    
    def listHandler = [        
        getRows: { 
            if (!entity.rows) entity.rows = 30;
            return entity.rows; 
        },
        getColumns: { 
            def params = [type: data.amnestyoption]
            return service.getColumns(params); 
        },
        getLastPageIndex: { 
            if (!data.lastpageindex) data.lastpageindex = -1;
            return data.lastpageindex;
        },
        fetchList: { o->
            //println 'params ' + o;
            if (entity) o.ledgerid = entity.objid; 
            if (data) o.amnestyid = data.objid;
            list = service.getList(o);
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
}

