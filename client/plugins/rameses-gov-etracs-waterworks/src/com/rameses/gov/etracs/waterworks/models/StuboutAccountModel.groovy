package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class StuboutSectionAccountModel {
    
    @Service('WaterworksStuboutService')
    def svc; 
    
    @Service("QueryService")
    def queryService;
    
    @Caller 
    def caller;

    def schemaName = 'waterworks_stubout';
    def title = 'Accounts'; 
    def accounts = [];
    def selectedItem;
    
    void init() {
        //do nothing 
    }
    
    def getEntity() {
        return caller?.entity; 
    } 
    
    void reload() {      
        accounts = null;
        listHandler.reload();
    }

    def listHandler = [
        fetchList: { o-> 
            if ( !accounts ) { 
                def m = [_schemaname:'waterworks_account'];
                m.findBy = [stuboutid: entity.objid];
                m.orderBy = "stuboutindex";
                accounts = queryService.getList(m);
            } 
            return accounts; 
        }        
    ] as BasicListModel;
    
    void changeIndexNo() { 
        if ( !selectedItem ) return; 
        
        def res = MsgBox.prompt("Enter the new index number:"); 
        if ( res == null || res.length()==0 ) return; 
        
        def newindexno = 0; 
        try {
            newindexno = res.toInteger(); 
        } catch(Throwable t){ 
            MsgBox.alert("Please enter an integer value"); 
            return; 
        } 
        
        svc.changeIndexNo([ stuboutid: entity.objid, stuboutindex: newindexno, accountid: selectedItem.objid ]); 
        accounts.clear();
        listHandler.reload(); 
    }    
}