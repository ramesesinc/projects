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
    
    boolean editAllowed = false;
    boolean createAllowed = false;
    boolean viewReportAllowed = false; 
    boolean allowOpenApplicant = true; 
    
    final def base64 = new com.rameses.util.Base64Cipher();
    
    def showTrackingno() { 
        return Modal.show( "show_trackingno", [info: [trackingno: entity.barcode]] );
    }
    
    def photo; 
    def fingerprintimage; 
    
    void afterOpen() {
        photo = decodeImage( entity.photo?.image ); 
        fingerprintimage = decodeImage( entity.fingerprint?.image ); 
    }

    private def decodeImage( o ) { 
        try { 
            if ( o instanceof String ) {
                if ( base64.isEncoded( o )) {                 
                    return base64.decode( o ); 
                } 
            }
            return o; 
        } catch(Throwable t) {
            t.printStackTrace(); 
            return null; 
        }
    }
    

    boolean isCanIssueClearance() {
        return (entity.state == 'ACTIVE' && !entity.clearanceid ); 
    } 
    void issueClearance() {
        def params = [ appid: entity.objid ]; 
        boolean success = false; 
        Modal.show('policeclearance_info:create', [ 
            handler: { o-> 
                success = true; 
                params.putAll( o ); 
            } 
        ]); 
        if ( !success ) return; 
        
        def result = reportSvc.createClearance( params ); 
        entity.clearanceid = result.objid; 
        entity.clearance = result; 
        binding.refresh('formActions');
    } 
    
    boolean isCanPrintPoliceClearance() {
        return ( entity.clearance?.objid ? true: false ); 
    }     
    def printPoliceClearance() { 
        def params = [ objid: entity.clearanceid ];
        def opener = Inv.lookupOpener("police_clearance:open", [entity: params ]); 
        opener.target = 'self'; 
        return opener; 
    } 
    
    
    boolean isCanIssueMayorClearance() {
        return (entity.state == 'ACTIVE' && !entity.clearanceid ); 
    } 
    
    boolean isCanPrintMayorClearance() {
        return (entity.state == 'ACTIVE' && entity.clearanceid );
    }    
    def printMayorClearance() { 
        def params = [ objid: entity.clearanceid ];
        def opener = Inv.lookupOpener("mayor_clearance:open", [entity: params ]); 
        opener.target = 'self'; 
        return opener;         
    } 

    boolean isCanIssuePoliceClearance() {
        if ( !isCanPrintMayorClearance()) return false;         
        return ( entity.pcid ? false : true ); 
    } 
    void issuePoliceClearance() { 
        def params = [ appid: entity.objid ]; 
        def pc = reportSvc.createPoliceClearance( params ); 
        entity.pcid = pc.objid; 
        binding.refresh('formActions');
    }
    
}