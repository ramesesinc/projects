package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
        
class UserQueueSkipTicketModel {

    @Service("UserQueueService")
    def userQueueSvc;

    def serveditem = [:];
    def counter;
    def counterid;
    
    void init() { 
        def o = userQueueSvc.getCounter(); 
        if ( !o ) throw new Exception('This terminal is not registered for queueing'); 
        
        counter = o.code; 
        counterid = o.objid; 
    } 

    def selectedItem;
    def listHandler = [
        fetchList: { 
            def m = [ counterid: counterid ];
            return userQueueSvc.getSkipTicketsForTheDay( m ); 
        } 
    ] as BasicListModel; 

    void requeue() { 
        if ( !selectedItem ) throw new Exception('Please select an item first'); 
        if ( MsgBox.confirm('You are about to requeue the selected ticket. Continue?' )) { 
            userQueueSvc.requeue([ objid: selectedItem.objid ]); 
            listHandler.reload(); 
        } 
    } 

    def retake() {
        if ( !selectedItem ) throw new Exception('Please select an item first'); 
        if ( MsgBox.confirm('You are about to process the selected ticket. Continue?' )) { 
            def m = [ objid: selectedItem.objid, counterid: counterid ]; 
            def result = userQueueSvc.retake( m ); 
            listHandler.removeSelectedItem(); 
            if ( !result ) return null; 

            m.title = result.section?.title; 
            m.groupid = result.group?.objid; 
            m.groupname = result.group?.title; 
            m.ticketno = result.ticketno;
            return showServingTicket( m );  
        } 
        
        return null; 
    } 
    
    def showServingTicket( item ) { 
        if ( !item ) return null; 

        serveditem.clear(); 
        serveditem.putAll( item ); 
        return 'view'; 
    }     
    
    void buzz() {
        userQueueSvc.buzzNumber([ counterid: counterid ]);
    }
    
    def skip() {
        userQueueSvc.skipNumber([ counterid: counterid ]);
        return "default";
    }
    
    def finish() {
        userQueueSvc.consumeNumber([ counterid: counterid ]);
        return "default";
    }
    
    def forward() {
        return null; 
    }        
} 