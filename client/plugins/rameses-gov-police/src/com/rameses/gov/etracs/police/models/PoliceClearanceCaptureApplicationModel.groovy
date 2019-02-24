package com.rameses.gov.police.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;
import java.rmi.server.UID;
import com.rameses.rcp.framework.ClientContext;

public class PoliceClearanceCaptureApplicationModel extends CrudPageFlowModel   {
    
    @Service("EntityService")
    def entitySvc;
        
    final def base64 = new com.rameses.util.Base64Cipher();
    
    def apptypes = [];
    
    @PropertyChangeListener 
    def changeHandler = [
        'entity.applicant': { o-> 
            applicantChanged(); 
        }
    ];  
                
    def photo; 
    def fpdata;

    def fingerprinttype;
    def fingerprintimage;
    def fingerprintdata;

    void create() { 
        apptypes = queryService.getList([ _schemaname:'policeclearance_type', where:['1=1', [:]] ]); 
        
        super.start(); 
        entity.state = 'PENDING'; 
        entity.fingerprint = [:];  
        entity.payment = [:]; 
        entity.ctc = [:];
    }
    
    
    void applicantChanged() {
        def photodata = null; 
        def fpdata = null; 
        
        if ( entity.applicant?.objid ) {
            photodata = entitySvc.getPhoto([ objid: entity.applicant.objid ]); 
            
            fpdata = queryService.findFirst([
                _schemaname: 'entity_fingerprint', 
                findBy: [ entityid: entity.applicant.objid ],
                select: 'objid,fingertype,image', 
                orderBy: 'dtfiled desc' 
            ]); 
        }
        
        if ( photodata == null ) photodata = [:];
        if ( fpdata == null ) fpdata = [:];
        
        if ( photodata.photo instanceof String ) {
            if ( base64.isEncoded( photodata.photo )) {
                photodata.photo = base64.decode( photodata.photo ); 
            }
        }
        
        if ( fpdata.image instanceof String ) {
            if ( base64.isEncoded( fpdata.image )) {
                fpdata.photo = base64.decode( fpdata.image ); 
            }
        }
        
        photo = photodata.photo; 
        entity.photo = [ objid: 'EPH'+ new java.rmi.server.UID()]; 
        binding.notifyDepends('photo'); 
        
        entity.fingerprint = [objid: fpdata?.objid, type: fpdata?.fingertype];
        fingerprinttype = fingerprinttypes.find{ it.key == fpdata.fingertype }
        fingerprintimage = fpdata.photo; 
        binding.notifyDepends('fingerprintimage'); 
    }
        
    void validateInit() {
        if ( !entity.applicant ) throw new Exception('applicant is required'); 
        if ( !entity.applicant.objid ) throw new Exception('applicant objid is required'); 
        if ( !entity.applicant.address?.text ) throw new Exception('applicant address is required'); 
        if ( !entity.applicant.gender ) throw new Exception('applicant gender is required'); 
        if ( !entity.applicant.birthdate ) throw new Exception('applicant birthdate is required'); 
        if ( !entity.applicant.civilstatus ) throw new Exception('applicant civilstatus is required'); 
        if ( !entity.applicant.citizenship ) throw new Exception('applicant citizenship is required'); 
        if ( !entity.purpose ) throw new Exception('purpose is required'); 

        if ( !entity.ctc.refno ) throw new Exception('ctc number is required'); 
        if ( !entity.ctc.issuedat ) throw new Exception('ctc issuance location is required'); 
        if ( !entity.ctc.issuedon ) throw new Exception('ctc issuance date is required'); 
        
        if ( !entity.payment.refno ) throw new Exception('payment receipt number is required'); 
        if ( !entity.payment.refdate ) throw new Exception('payment receipt date is required'); 
        if ( !entity.payment.amount ) throw new Exception('payment amount is required'); 
        
        validatePhoto(); 
        validateFingerPrint(); 
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
    
    final def fingerprinttypes = [         
        [key: 'right_thumb', type: FingerPrintModel.RIGHT_THUMB, title: 'RIGHT THUMB'], 
        [key: 'left_thumb', type: FingerPrintModel.LEFT_THUMB, title: 'LEFT THUMB']  
    ]; 
    def fingerPrintHandler = [ 
        getFingerType: {
            return fingerprinttype.type; 
        }, 
        
        onselect: { o-> 
            fpdata = [:]; 
            def fkey = fingerprinttype.key;
            def ftype = fingerprinttype.type;             
            Object fmdobj = o.getFmdData( ftype ); 
            def bytes = o.getData( ftype ); 
            def scaler = new ImageScaler(); 
            def imageobj = scaler.scale( new javax.swing.ImageIcon(bytes), 300, 300); 
            fingerprintimage = scaler.getBytes( imageobj ); 
            fingerprintdata = fmdobj; 
            entity.fingerprint = [type: fkey]; 
            binding.refresh('fingerprintimage');
        }
    ] as FingerPrintModel; 
    
    void captureFingerPrint() { 
        com.rameses.rcp.fingerprint.FingerPrintViewer.open( fingerPrintHandler );  
    } 
    
    void validateFingerPrint() {
        if ( !fingerprintimage ) throw new Exception("Please specify the applicant's finger print");
        
        if ( entity.fingerprint.objid ) {
            // fingerprint data is coming from entity 
        } else {
            entity.fingerprint.image = base64.encode((Object) fingerprintimage );
            entity.fingerprint.data = base64.encode((Object) fingerprintdata );  
        }
    }
    
    public void saveCreate() {
        super.saveCreate(); 
        MsgBox.alert('Application successfully saved');
        
        def p = [entity: [objid: entity.objid]]; 
        def op = Inv.lookupOpener('policeclearance_application:open', p); 
        op.target = 'self';
        binding.fireNavigation( op );
    }
    
    public void createAnother() {
        photo = null; 
        fpdata = null;
        fingerprinttype = null;
        fingerprintimage = null;
        fingerprintdata = null;        
        entity.clear(); 
        init(); 
    }
}