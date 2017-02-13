package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class BatchCaptureListModel extends ListController {

    String serviceName = 'BatchCaptureCollectionService'
    String entityName = 'cashreceipt:batchcapture'
    String formTarget = 'window'
    String tag = 'list'

    def status
    def statuslist = ['DRAFT', 'FORPOSTING', 'POSTED'];
    def searchtext


    @PropertyChangeListener
    def listener = [
        "status" : {
            search();
        }
    ]   

    def f = "all"; 
    boolean allow_create; 
    
    public boolean isAllowCreate() {
        return allow_create; 
    }
    
    boolean hasCreateOption() {
        def opts = Inv.lookup('batchcapture:list:option:allowCreate'); 
        return ( opts? true : false ); 
    }
    
    void initCollector() { 
        f = "bycollector"; 
        allow_create = hasCreateOption(); 
    }

    void initSubcollector() {
        f = "bysubcollector"; 
        allow_create = hasCreateOption(); 
    }

    protected void onbeforeFetchList(Map m) { 
        m.listtype = f;
    }
}
