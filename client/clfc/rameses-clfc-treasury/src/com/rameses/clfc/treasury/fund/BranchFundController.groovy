package com.rameses.clfc.treasury.fund;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*
import java.rmi.server.UID;

class BranchFundController 
{
    @Service('BranchFundService') 
    def service; 
    
    @Binding
    def binding; 
    
    @Caller
    def caller;
    
    def entity = [:]; 
    
    void open() {
        entity = service.open(); 
    }
    
    def getData() {
        return entity.data; 
    }
    
    def getAvailableBalance() {
        try { 
            return entity.amtbalance - entity.amtuse; 
        } catch(Throwable t) {
            return 0.0;
        }
    }
    
    def selectedItem;
    def collectorListHandler = [
        fetchList:{params-> 
            entity.collectors.each{ 
                def col = it.collector;
                col.name = (col.lastname + ', ' + col.firstname + (col.middlename? ' '+col.middlename: '')); 
                it.remainingbalance = it.threshold-it.amtuse; 
            } 
            return entity.collectors; 
        } 
    ] as BasicListModel;    
 
    
    def close() {
        return '_close';
    }
    
    def viewLogs() {
        return Inv.lookupOpener('branchfund_items:open', [fundid: entity.fundid]); 
    } 
    
    def closeFund() {
        if (!MsgBox.confirm('You are about to close this fund. Continue?')) return;
        
        def handler = { remarks-> 
            try {
                def result = service.close([fundid: entity.fundid, remarks: remarks]);
                if (result) entity.putAll(result);

                EventQueue.invokeLater({ caller?.reload(); }); 
                binding.refresh(); 
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        } 
        return Inv.lookupOpener('remarks:create', [title: 'Remarks for Closing Fund',  handler: handler]); 
    }
}
