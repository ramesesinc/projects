package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class BatchGRListModel extends com.rameses.seti2.models.CrudListModel {
    def open() {
        def res = super.open();
        if ('APPROVED'.equalsIgnoreCase(selectedItem.state)) {
            def param = [ schemaName:'batchgr', adapter:adapter, entity: selectedItem];
            return Inv.lookupOpener('batchgr:closedwf:open', param);
        } else {
            return res;
        }
    }
            
}


