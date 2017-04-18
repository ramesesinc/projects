package com.rameses.gov.police.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.rcp.framework.ClientContext;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;
import java.rmi.server.UID;

public class PoliceClearanceApplicationModel extends CrudFormModel {
    
    @Service('PoliceClearanceReportService') 
    def reportSvc; 
    
    def feeListModel = [
        fetchList: { o->
            return entity.fees;
        }
    ] as BasicListModel;
    
    def showTrackingno() {
        return Modal.show( "show_trackingno", [info: [trackingno: entity.barcode]] );
    }
    
    boolean isCanPrintClearance() {
        return (entity.state == 'CLOSED');
    }
    
    def printClearance() { 
        def params = [ appid: entity.objid ]; 
        def o = reportSvc.getClearance( params ); 
        if ( o == null ) o = reportSvc.create( params ); 
        if ( o?.expired ) throw new Exception('''
            Police Clearance is already expired. 
            Please create another application. 
        '''); 
        
        return Inv.lookupOpener("policeclearance:print", [ entity: entity ]); 
    }    
}