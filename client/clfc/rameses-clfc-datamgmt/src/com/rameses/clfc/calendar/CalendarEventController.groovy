package com.rameses.clfc.calendar;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class CalendarEventController extends CRUDController
{
    @Caller
    def caller;
    
    String serviceName = 'CalendarEventService';
    String entityName  = 'calendar_event';

    String createFocusComponent = 'entity.date';
    String editFocusComponent = 'entity.name';            
    boolean showConfirmOnSave = false;
    boolean allowApprove = false;
    boolean allowDelete = false;

    Map createPermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'calendar_event.create']; 
    Map editPermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'calendar_event.edit']; 
    
    def viewLogs() {
        return Inv.lookupOpener('txnlog:open', [query: [txnid: entity.objid]]); 
    } 
}
