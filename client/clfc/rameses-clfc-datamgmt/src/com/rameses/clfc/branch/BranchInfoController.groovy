package com.rameses.clfc.branch;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
        
public class BranchInfoController 
{
    @Service('BranchService')
    def service;
    
    @Binding 
    def binding; 

    def mode = 'read';
    def entity = [:]; 
    String title = 'Branch Information'; 
    
    void open() { 
        entity = service.open([:]); 
        if (!entity) entity = [:]; 
    } 
    
    void edit() {
        mode = 'edit'; 
        binding.focus('entity.objid');
    }
    
    def close() {
        return '_close';
    }
    
    void cancel() {
        mode = 'read'; 
        open();
    } 
    
    void save() {
        if(!MsgBox.confirm('You are about to submit this information. Continue?')) return;
        
        entity.code = entity.objid;
        service.save(entity); 
        mode = 'read'; 
    } 
} 
