package com.rameses.gov.etracs.police.models; 

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class PoliceClearanceAskInfoModel {

    @Caller 
    def caller; 
    
    def mode;
    def handler;
    def data = [:];
    
    void create() {
        mode = 'create'; 
    }
    void open() {
        mode = 'open'; 
    }
    
    def doOk() { 
        try {
            return '_close'; 
        } finally {
            if ( handler ) handler( data );  
        } 
    }
    
    def doCancel() {
        return '_close';
    }
}   