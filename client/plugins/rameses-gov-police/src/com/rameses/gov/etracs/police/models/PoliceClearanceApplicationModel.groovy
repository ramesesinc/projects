package com.rameses.gov.police.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;
import java.rmi.server.UID;
import com.rameses.rcp.framework.ClientContext;

public class PoliceClearanceApplicationModel extends CrudFormModel {
    
    def showTrackingno() {
        return Modal.show( "show_trackingno", [info: [trackingno: entity.barcode]] );
    }
    
    def feeListModel = [
        fetchList: { o->
            return entity.fees;
        }
    ] as BasicListModel;
    
    /*
    void loadPhysical() {
        def p = physicalSvc.open([objid:entity.person.objid]);            
        entity.person.putAll( p );
    }

    void savePhysical() {
        physicalSvc.save( entity.person );
    }
    */
            
}