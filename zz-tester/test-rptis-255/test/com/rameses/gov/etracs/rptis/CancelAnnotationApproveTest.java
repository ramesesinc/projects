package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import com.rameses.gov.etracs.rptis.models.AnnotationModel;
import com.rameses.gov.etracs.rptis.models.CancelAnnotationModel;
import test.utils.Data;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CancelAnnotationApproveTest  {
    protected static FaasModel faasModel;
    protected static AnnotationModel annotationModel;
    protected static CancelAnnotationModel cancelModel;
    
    protected static Map faas;
    protected static Map annotation;
    protected static Map cancelAnnotation;
    protected static Map remoteAnnotation;
    protected static Map remoteCancel;
    
    static {
        faasModel = new FaasModel();
        annotationModel = new AnnotationModel();
        cancelModel = new CancelAnnotationModel();
    }
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.createAndApprove();
        annotation = annotationModel.createAndApprove(faas);
        cancelAnnotation = cancelModel.init(annotation);
        cancelAnnotation = cancelModel.create(cancelAnnotation);
        cancelAnnotation = cancelModel.submitForApproval(cancelAnnotation);
        cancelAnnotation = cancelModel.approve(cancelAnnotation);
        
        annotation = annotationModel.open(annotation);
        remoteCancel = Data.findRemoteEntity("cancelannotation", cancelAnnotation, true);
        remoteAnnotation = Data.findRemoteEntity("faasannotation", annotation);
    }
    
    @AfterClass
    public static void tearDownClass() {
        Data.cleanUp();
        Data.cleanUp(true);
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }
    
    
    /*----------------------------------------------------------
     * ANNOTATION TEST 
     ----------------------------------------------------------*/
    
    @Test
    public void shouldCreateCancelAnnotation() {
        assertNotNull(cancelAnnotation);
    }
            
    @Test
    public void shouldChangeCancelAnnotationStateToApproved() {
        assertEquals("APPROVED", cancelAnnotation.get("state"));
    }
    
    @Test
    public void shouldChangeAnnotationStateToCancelled() {
        assertEquals("CANCELLED", annotation.get("state"));
    }
    
    /*----------------------------------------------------------
     * REMOTE TEST  
     ----------------------------------------------------------*/
    @Test
    public void shouldCreateRemoteCancelAnnotation() {
        assertNotNull(remoteCancel);
    }
    
    @Test
    public void shouldChangeRemoteCancelAnnotationStateToApproved() {
        assertEquals("APPROVED", remoteCancel.get("state"));
    }
    
    @Test
    public void shouldChangeRemoteAnnotationStateToCancelled() {
        assertEquals("CANCELLED", remoteAnnotation.get("state"));
    }
}
