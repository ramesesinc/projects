package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
        
class UserQueueTicketRequestModel {

    @Service("UserQueueService")
    def userQueueSvc;

    @Service("QueueService")
    def queueSvc;
    
    @Binding
    def binding;
    
    void init() {
        
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