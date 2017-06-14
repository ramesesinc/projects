package com.rameses.gov.police.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;
import java.rmi.server.UID;
import com.rameses.rcp.framework.ClientContext;

public class PoliceClearanceNewApplicationModel extends CrudPageFlowModel   {
    
    @Service("EntityService")
    def entitySvc;
        
    final def base64 = new com.rameses.util.Base64Cipher();
    
    def showTrackingno() {
        return Modal.show( "show_trackingno", [info: [trackingno: entity.barcode]] );
    }   
        
    void create() { 
        def selitem = caller.selectedItem; 
        def result = persistenceService.read([ _schemaname:'cashreceipt_policeclearance', findBy:[ objid: selitem.objid ]]); 
        super.start(); 
        entity.applicant = result.applicant; 
        entity.apptype = [objid: result.txntype, title: result.txntype]; 
        entity.purpose = result.purpose; 
        entity.receipt = result.objid;   
        entity.state = 'PENDING'; 
        entity.fingerprint = [:];  
        entity.ctc = [:];
        entity.payment = [
            objid : result.objid, 
            refno : result.receiptno, 
            refdate : result.receiptdate, 
            amount  : result.amount 
        ]; 
    }
    
    def photo; 
    
    void validateInit() {
        if ( !entity.applicant.objid ) throw new Exception('applicant objid is required'); 
        if ( !entity.applicant.address?.text ) throw new Exception('applicant address is required'); 
        if ( !entity.applicant.gender ) throw new Exception('applicant gender is required'); 
        if ( !entity.applicant.birthdate ) throw new Exception('applicant birthdate is required'); 
        if ( !entity.applicant.civilstatus ) throw new Exception('applicant civilstatus is required'); 
        if ( !entity.applicant.citizenship ) throw new Exception('applicant citizenship is required'); 

        if ( !entity.ctc.refno ) throw new Exception('ctc number is required'); 
        if ( !entity.ctc.issuedat ) throw new Exception('ctc issuance location is required'); 
        if ( !entity.ctc.issuedon ) throw new Exception('ctc issuance date is required'); 
        
        if ( entity.photo == null ) {
            def photodata = entitySvc.getPhoto([ objid: entity.applicant.objid ]); 
            if ( photodata==null ) photodata = [:];
            
            if ( photodata.photo instanceof String ) {
                if ( base64.isEncoded( photodata.photo )) {
                    photodata.photo = base64.decode( photodata.photo ); 
                }
            }
            photo = photodata.photo; 
            entity.photo = [ objid: 'EPH'+ new java.rmi.server.UID()]; 
        }
    }
    
    void validatePhoto() {
        if ( !photo ) throw new Exception("Please specify the applicant's photo"); 
        
        def icon = new javax.swing.ImageIcon( photo ); 
        def scaler = new ImageScaler();         
        def imageobj = scaler.createThumbnail( icon ); 
        def bytes = scaler.getBytes( imageobj ); 
        entity.photo.thumbnail = base64.encode((Object) bytes ); 
        entity.photo.image = base64.encode((Object) photo ); 
    }
    
    final def types = [         
        [key: 'right_thumb', type: FingerPrintModel.RIGHT_THUMB, title: 'RIGHT THUMB'], 
        [key: 'left_thumb', type: FingerPrintModel.LEFT_THUMB, title: 'LEFT THUMB']  
    ]; 
    def fingerprinttype;
    def fingerprintimage;
    def fingerprintdata;
    def fingerPrintHandler = [ 
        getFingerType: {
            return fingerprinttype.type; 
        }, 
        
        onselect: { o-> 
            def fkey = fingerprinttype.key;
            def ftype = fingerprinttype.type;             
            Object fmdobj = o.getFmdData( ftype ); 
            def bytes = o.getData( ftype ); 
            def scaler = new ImageScaler(); 
            def imageobj = scaler.scale( new javax.swing.ImageIcon(bytes), 300, 300); 
            fingerprintimage = scaler.getBytes( imageobj ); 
            fingerprintdata = fmdobj; 
            entity.fingerprint.type = fkey; 
            binding.refresh('fingerprintimage');
        }
    ] as FingerPrintModel; 
    
    void captureFingerPrint() { 
        com.rameses.rcp.fingerprint.FingerPrintViewer.open( fingerPrintHandler );  
    } 
    
    void validateFingerPrint() {
        if ( !fingerprintimage ) throw new Exception("Please specify the applicant's finger print");
        
        entity.fingerprint.data = base64.encode((Object) fingerprintdata );  
        entity.fingerprint.image = base64.encode((Object) fingerprintimage );  
    }

}