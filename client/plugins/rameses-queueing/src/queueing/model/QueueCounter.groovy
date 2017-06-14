package queueing.model;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.common.*;

class QueueCounter {
    
    @Service("QueueCounterService")
    def svc;
    
    @Script("User")
    def user;
    
    def entity = [:];
    def selectedItem;
    def mode = "create";
    def handler;
    
    void init() { 
        def o = svc.getCounter(); 
        if ( o ) { 
            entity = o; 
            mode = 'edit'; 
        } else { 
            entity = [objid: user.env.TERMINALID, sections:[]];
            mode = 'create';
        } 
    }
    
    def itemListModel = [
        fetchList: { o->
            return entity.sections;
        }
    ] as BasicListModel;

    def doOk() { 
        if( mode == "create") {
            svc.create( entity );
        } else {
            svc.update( entity );
        }
        if ( handler ) { 
            handler(entity); 
        }        
        AbstractUserQueueHandler.fireNotify( 'counter-data-changed', entity );        
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
    def addSection() {
        def h = { o-> 
            if( entity.sections.find{ it.objid == o.objid } )
                throw new Exception("Section already added");
                
            if ( mode == 'create' ) {
                entity.sections << o; 
            } else {
                def resp = svc.addSection([ sectionid: o.objid ]);
                if ( resp ) entity.sections.add( resp );
            } 
            itemListModel.reload(); 
            AbstractUserQueueHandler.fireNotify( 'add-section', entity );
        } 
        return Inv.lookupOpener( "queue_section:lookup", [onselect:h]);
    }
    
    def removeSection() {
        if(!selectedItem) return null; 
        if ( mode != 'create' ) {
            svc.removeSection( [counterid: entity.objid, sectionid: selectedItem.objid] );
        }
        
        def o = entity.sections.find{ it.objid==selectedItem.objid } 
        if ( o ) entity.sections.remove( o ); 
        
        itemListModel.reload();
        
        AbstractUserQueueHandler.fireNotify( 'remove-section', entity );
    }
    
    void updateName() {
        def x = MsgBox.prompt("Enter Counter Name");
        if ( !x ) return; 
        
        svc.update([ objid:entity.objid, code:x ]); 
        entity.code = x;
        
        AbstractUserQueueHandler.fireNotify( 'change-counter-code', entity );
    }
    
}