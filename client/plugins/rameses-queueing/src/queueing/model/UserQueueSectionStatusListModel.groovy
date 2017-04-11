package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
        
class UserQueueSectionStatusListModel {

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
            return userQueueSvc.getSectionStatusList( m ); 
        } 
    ] as BasicListModel; 
} 