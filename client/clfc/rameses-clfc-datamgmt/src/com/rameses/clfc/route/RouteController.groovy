package com.rameses.clfc.route;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class RouteController extends CRUDController
{
    String serviceName = 'LoanRouteService';
    String entityName = 'route';

    String createFocusComponent = 'entity.description';
    String editFocusComponent = 'entity.description';             
    boolean showConfirmOnSave = false;
    boolean allowApprove = false;

    Map createPermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'route.create']; 
    Map editPermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'route.edit']; 
    Map deletePermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'route.delete']; 
    Map approvePermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'route.approve']; 
    
    def dayPeriodList = [
        [value: 'AM'], 
        [value: 'PM']
    ];
    
    Map createEntity() {
        def sid = new java.rmi.server.UID().toString();
        return [ code: 'R'+sid.hashCode() ]; 
    } 
    
    def viewLogs() {
        return Inv.lookupOpener('txnlog:open', [query: [txnid: entity.code]]); 
    } 
}
