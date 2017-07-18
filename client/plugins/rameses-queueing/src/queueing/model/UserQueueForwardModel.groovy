package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
        
class UserQueueForwardModel {

    @Service("UserQueueService")
    def userQueueSvc;
        
    @Binding
    def binding;
    
    def handler;
    def sections;
    def selectedSection;
    
    void init() {
        
    }
    
    def doCancel() {
        return '_close'; 
    }
    
    def doOk() { 
        if ( handler ) handler( selectedSection ); 
        
        return '_close'; 
    }
}