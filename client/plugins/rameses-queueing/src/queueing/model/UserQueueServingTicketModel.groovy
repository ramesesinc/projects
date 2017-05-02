package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
        
class UserQueueServingTicketModel {

    @Service("UserQueueService")
    def userQueueSvc;

    @Service("QueueService")
    def queueSvc;
    
    @Binding
    def binding;
    
    def serveditem;
    def counterid;     
    
    void init() {
        counterid = user.env.TERMINALID; 
    }
    
    void buzz() {
        userQueueSvc.buzzNumber([ counterid: counterid ]);
    }
    
    def skip() {
        userQueueSvc.skipNumber([ counterid: counterid ]);
        return "_close";
    }
    
    def finish() {
        userQueueSvc.consumeNumber([ counterid: counterid ]);
        return "_close";
    }
    
    def forward() {
        return null; 
    }
    
}