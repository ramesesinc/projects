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

    @Script("User")
    def user;
    
    @Binding
    def binding;
    
    def entity;
    
    public def init() {
        def env = [ 
            SESSIONID : "GUEST"+ new java.rmi.server.UID(), 
            USERID: "GUEST", USER: "GUEST", NAME: "GUEST", FULLNAME: "GUEST", 
            ROLES: ["ALLOWED": "system.*", "QUEUE.USER": null],
            TERMINALID: user.env.TERMINALID, 
            
            "toolbar.type" : "queue-toolbar", 
            "statusbar.type" : "queue-statusbar"  
        ]; 
        def usr = [ USERID: env.USERID, env: env ];     
        OsirisContext.env.putAll( env );
        OsirisContext.clientContext.properties.PROFILE = usr; 
        
        entity = userQueueSvc.init();
        if( entity?.code ) return '_close'; 

        return 'default'; 
    }
    
    public def submit() { 
        queueCounterSvc.create( entity ); 
        return '_close'; 
    } 
}