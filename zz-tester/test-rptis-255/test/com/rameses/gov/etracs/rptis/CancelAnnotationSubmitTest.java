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

public class CancelAnnotationSubmitTest  {
    protected static FaasModel faasModel;
    protected static AnnotationModel annotationModel;
    protected static CancelAnnotationModel cancelModel;
    
    protected static Map faas;
    protected static Map annotation;
    protected static Map cancelAnnotation;
    
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
    public void shouldChangeStateToForApproval() {
        assertEquals("FORAPPROVAL", cancelAnnotation.get("state"));
    }
}
