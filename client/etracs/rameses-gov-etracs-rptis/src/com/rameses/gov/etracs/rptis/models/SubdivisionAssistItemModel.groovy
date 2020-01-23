package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class SubdivisionAssistItemModel
{
    @Caller
    def caller;

    @Binding
    def binding;
    
    @Service('SubdivisionService')
    def svc; 
    
    def subdivision; 
    def taskstate;
    def entity;
    def pinTypes = ['new', 'old'];

    @PropertyChangeListener
    def listener = [
        'entity.*parcel' : {
            entity.parcelcount = 0;
            if (entity.startparcel == null) entity.startparcel = 0;
            if (entity.endparcel == null) entity.endparcel = 0;
            if (entity.startparcel > 0 && entity.startparcel <= entity.endparcel) {
                entity.parcelcount = entity.endparcel - entity.startparcel + 1;
            }
        }
    ]

    void init() {
        entity = [:];
        entity.objid = 'SAA' + new java.rmi.server.UID();
        entity.subdivision = subdivision;
        entity.parent = [objid: entity.objid]
        entity.taskstate = taskstate;
    }

    def addAssist() {
        svc.addAssist(entity);
        caller.refresh();
        return '_close';
    }

    def getUsers() {
        return svc.getUsers(taskstate);
    }
    
}
