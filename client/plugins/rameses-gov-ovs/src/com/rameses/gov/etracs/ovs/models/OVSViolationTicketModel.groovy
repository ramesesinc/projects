package com.rameses.gov.etracs.ovs.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
import com.rameses.seti2.models.*;
        
public class OVSViolationTicketModel extends CrudFormModel{
        
    @Service('ViolationTicketService')
    def violationSvc;

    public void afterColumnUpdate(String itemName, Object item, String colName){ 
        if ( !entity.violator?.objid ) throw new Exception('Please specify a violator first');
        if ( !item.violation?.objid ) throw new Exception('Please specify a violation');

        def m = [violationid: item.violation.objid, violatorid: entity.violator.objid ];
        def z = violationSvc.getViolationInfo( m );
        if(!z)
            throw new Exception("Error no value returned in assessing violation");
        item.putAll( z ); 
    }
    
}