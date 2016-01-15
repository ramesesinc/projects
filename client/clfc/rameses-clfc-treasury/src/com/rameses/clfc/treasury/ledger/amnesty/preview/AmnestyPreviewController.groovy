package com.rameses.clfc.treasury.ledger.amnesty.preview

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class AmnestyPreviewController extends AbstractAmnestyPreviewController 
{
    String serviceName = 'LedgerAmnestyPreviewService';
    
    String getLedgerid() {
        return item?.ledger?.objid;
    }
    
    String getAmnestyid() {
        return item?.objid;
    }
    
    def getTitle() {
        def str = 'Amnesty';
        if (item?.availed) {
            str += ': ' + item?.availed?.description;
        }
        return str;
    }
    
    int getRows() {
        if (!item.rows) item.rows = 30;
        return item.rows;
    }
    
    int getLastPageIndex() {
        if (!item.lastpageindex) item.lastpageindex = -1;
        return item.lastpageindex;
    }
    
    def item;
    void init() {
        /*
        if (entity) {
            if (!entity.rows) entity.rows = 30;
            data.lastpageindex = service.getPageIndex([objid: data.objid, rows: entity.rows]);
        }
        refresh();
        */
        item = service.openAmnesty(data);
        super.init();
    }
    
    Map getColumnParams() {
        return [type: item?.amnestytype?.value];
    }
}

