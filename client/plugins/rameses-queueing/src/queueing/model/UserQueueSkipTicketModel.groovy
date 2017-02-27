package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
        
class UserQueueSkipTicketModel {

    @Service("UserQueueService")
    def userQueueSvc;

    @Caller 
    def caller; 

    void init() {
    } 

    def selectedItem;
    def listHandler = [
        fetchList: { 
            def m = [ counterid: caller.counterid ];
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

    void retake() {
        if ( !selectedItem ) throw new Exception('Please select an item first'); 
        if ( MsgBox.confirm('You are about to process the selected ticket. Continue?' )) { 
            def m = [ objid: selectedItem.objid, counterid: caller.counterid ]; 
            def result = userQueueSvc.retake( m ); 
            listHandler.removeSelectedItem(); 
            if ( !result ) return; 

            m.title = result.section?.title; 
            m.groupid = result.group?.objid; 
            m.groupname = result.group?.title; 
            m.currentnumber = result.ticketno;
            caller.showServingTicket( m ); 
        } 
    } 
} 