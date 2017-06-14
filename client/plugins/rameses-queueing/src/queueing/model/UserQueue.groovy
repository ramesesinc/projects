package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;
        
class UserQueue extends AbstractUserQueueHandler {

    @Service("UserQueueService")
    def userQueueSvc;
        
    @Binding
    def binding;
    
    def entityCounter;
    def counter;
    def counterid;
    
    def serveditem = [:];
    def controlList = [];
    
    def formControls = [
        getControlList: {
            return controlList;
        }
    ] as FormPanelModel;

    public def init() {
        AbstractUserQueueHandler.add('user-queue-main', this); 
        def z = userQueueSvc.init();
        if( ! z?.code ) {
            boolean pass = false;
            def h = { o->
                counter = o;
                pass = true;
            }
            Modal.show("queue_counter:show", [handler:h, entity:z]);
            if ( !pass ) throw new BreakException(); 
        } 
        //detemine if serving
        counter = z.code;
        counterid = z.objid;
        return reload( z ); 
    }
    
    def reload( def z ) {
        //detemine if serving
        controlList.clear();
        
        def takehandler = { 
            take( it ); 
        }        
        z.sections.each{ o-> 
            def item = [:];
            item.putAll( o ); 
            item.sectionid = o.objid; 
            item.counterid = z.objid; 
            controlList << [
                type: 'qsectionitem', caption: item.title, 
                actionText: 'Take', item: item, handler: takehandler, 
                showCaption:false, categoryid: item.groupid 
            ]; 
        }

        entityCounter = z;  
        if ( z.current ) { 
            def m = [:]; 
            m.title = z.current.title; 
            m.groupid = z.current.groupid;
            m.groupname = z.current.groupname; 
            m.ticketno = z.current.currentno;            
            return showServingTicket( m ); 
            
        } else {
            return "default";
        }
    }
    
    void take( item ) { 
        if ( !item ) return; 

        def m = [ sectionid: item.sectionid, counterid: item.counterid ];
        def ticketno = userQueueSvc.takeNextNumber( m ); 
        if ( !ticketno ) {
            MsgBox.alert('Queue is empty'); 
            return; 
        }

        m.title = item.title;
        m.groupid = item.groupid;
        m.groupname = item.group?.title;
        m.ticketno = ticketno;         
        def outcome = showServingTicket( m ); 
        if ( outcome ) binding.fireNavigation( outcome ); 
    }

    def showServingTicket( item ) { 
        if ( !item ) return null; 

        serveditem.clear(); 
        serveditem.putAll( item ); 
        return 'view'; 
    } 
    
    def selectNumber() {
        return "view";
    }
        
    /*def edit() { 
        def h = { o-> 
            reload( o ); 
            formControls.reload(); 
        }
        def op = Inv.lookupOpener("queue_counter:show", [entity: entityCounter, handler: h]);
        op.target = "popup";
        return op;
    }*/ 
    
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
    
    public void notify( String action, def data ) { 
        if ( action == 'change-counter-code' && data?.code ) {
            counter = data.code; 
            binding.refresh(); 
        } else if ( action == 'remove-section' ) {
            init(); 
            formControls.reload(); 
            binding.refresh(); 
        } else if ( action == 'add-section' ) {
            init(); 
            formControls.reload();  
            binding.refresh(); 
        } 
    }
    
    def waitListHandler = [
        getRefreshInterval: {
            return 2000; 
        }, 
        fetchList: { 
            try { 
                return userQueueSvc.getWaitingList(); 
            } catch(Throwable t) {
                return null; 
            } 
        } 
    ] as ListPaneModel; 
}