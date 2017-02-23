package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
        
class PlatformCounterRegistrationModel {

    @Service("UserQueueService")
    def userQueueSvc;
    
    @Service("QueueCounterService")
    def queueCounterSvc;
    
    @Binding
    def binding;
    
    def entity;
    
    public def init() {
        entity = userQueueSvc.init();
        if( entity?.code ) return '_close'; 

        return 'default'; 
    }
    
    public def submit() { 
        queueCounterSvc.create( entity ); 
        return '_close'; 
    } 
}