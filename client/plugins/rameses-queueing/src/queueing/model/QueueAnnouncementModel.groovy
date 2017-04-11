package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
        
class QueueAnnouncementModel {

    @Service("PersistenceService")
    def svc;

    def mode;
    def entity;

    void open() { 
        def params = [_schemaname:'queue_announcement', objid: 'announcement']; 
        entity = svc.read( params ); 
        if ( entity ) {
            mode = 'update'; 
        } else { 
            mode = 'create';
            entity = [ objid: 'announcement', name:'default', state:'APPROVED' ]; 
        } 
        entity._schemaname = params._schemaname; 
    } 

    def doOk() {
        if ( mode == 'create' ) {
            svc.create( entity );  
        } else if ( mode == 'update' ) {
            svc.update( entity ); 
        } 
        return '_close';
    } 

    def doCancel() {
        return '_close';
    } 
} 