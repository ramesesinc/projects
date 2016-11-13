package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;
        
class UserQueue {

    @Service("UserQueueService")
    def userQueueSvc;

    @Service("QueueCounterService")
    def counterSvc;

    @Service("QueueService")
    def queueSvc;
    
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
        serveditem.clear(); 
        if ( z.current ) { 
            serveditem.title = z.current.title; 
            serveditem.groupname = z.current.groupname; 
            serveditem.currentnumber = z.current.currentno;            
            return "view";
            
        } else {
            return "default";
        }
    }
    
    void take( item ) { 
        serveditem.clear(); 
        if ( !item ) return; 

        def m = [ sectionid: item.sectionid, counterid: item.counterid ];
        def currentnumber = userQueueSvc.takeNextNumber( m ); 
        if ( !currentnumber ) {
            MsgBox.alert('Queue is empty'); 
            return; 
        }

        serveditem.title = item.title;
        serveditem.groupname = item.group?.title;
        serveditem.currentnumber = currentnumber; 
        binding.fireNavigation("view");
    }
    
    def selectNumber() {
        return "view";
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
    
    def edit() { 
        def h = { o-> 
            reload( o ); 
            formControls.reload(); 
        }
        def op = Inv.lookupOpener("queue_counter:show", [entity: entityCounter, handler: h]);
        op.target = "popup";
        return op;
    } 


    def pickhandler = { o-> 
        if ( !o ) return; 
        
        def reqno = queueSvc.fetchNextNumber([ sectionid: o.objid ]); 
        MsgBox.alert("<html>Your Queue Number <br> <font size=\"14\"><b>"+ reqno +"</b></font></html>"); 
    }    
    def queueSectionList = []; 
    def queueSectionHandler = [
        getControlList: { 
            if ( !queueSectionList ) { 
                userQueueSvc.getQueueSections().each{ o-> 
                    queueSectionList << [
                        type: 'qsectionitem', caption: o.title, 
                        actionText: 'Pick', item: o, handler: pickhandler, 
                        showCaption: false, categoryid: o.groupid  
                    ]; 
                } 
            }
            return queueSectionList;
        }
    ] as FormPanelModel;
    
    void refreshQueueSections() {
    }
}