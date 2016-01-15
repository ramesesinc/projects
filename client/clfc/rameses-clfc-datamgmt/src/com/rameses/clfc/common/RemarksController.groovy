package com.rameses.clfc.common;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class RemarksController 
{
    def mode = 'create'; 
    def remarks;
    def handler;
    
    def title;
    
    @FormTitle
    def getFormTitle() {
        if (title) return title; 
        
        return 'Remarks'; 
    }
    
    void create() {
        mode = 'create'; 
        remarks = null;
    }
    
    void open() { 
        mode = 'read'; 
    }    
    
    def doCancel() {
        return '_close'; 
    }
    
    def doOk() {
        if (handler) {
            EventQueue.invokeLater(handler, remarks);
        }        
        return '_close'; 
    }
}
