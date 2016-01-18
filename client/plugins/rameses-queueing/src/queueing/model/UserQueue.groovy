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

    @Binding
    def binding;
    
    def entityCounter;
    def counter;
    def counterid;
    def currentNumber;
    
    def title;
    def groupname;
    
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
        z.sections.each{ o-> 
            def item = [caption: o.title, name:o.objid, counter:z.objid, title:o.title, groupname: o.groupid];
            controlList <<  [name: o.objid, type: 'subform', handler:'queue:button', showCaption:false, properties:[item:item], categoryid: o.groupid];
        }
        entityCounter = z;  
        if(z.current) {
            currentNumber = z.current.currentno;
            title = z.current.title;
            groupname = z.current.groupname;
            return "view";
        }
        else {
            return "default";
        }
    }
    
    void take( item ) {
        title = item.title;
        groupname = item.groupname;
        def z = [
            sectionid: item.name,
            counterid: item.counter
        ];
        MsgBox.alert(z);
        currentNumber = userQueueSvc.takeNextNumber(z);
        binding.fireNavigation("view");
    }
    
    def selectNumber() {
        return "view";
    }
    
    void buzz() {
        userQueueSvc.buzzNumber([counterid:counterid]);
    }
    
    def skip() {
        userQueueSvc.skipNumber([counterid:counterid]);
        currentNumber = null; 
        return "default";
    }
    
    def finish() {
        userQueueSvc.consumeNumber([counterid:counterid]);
        currentNumber = null; 
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
}