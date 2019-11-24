package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import com.rameses.gov.etracs.rptis.models.AnnotationModel;
import test.utils.Data;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationSubmitTest  {
    protected static FaasModel faasModel;
    protected static AnnotationModel annotationModel;
    
    protected static Map faas;
    protected static Map annotation;
    
    static {
        faasModel = new FaasModel();
        annotationModel = new AnnotationModel();
    }
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.createAndApprove();
        annotation = annotationModel.init(faas);
        annotation = annotationModel.create(annotation);
        annotation = annotationModel.submitForApproval(annotation);
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
    public void shouldCreateAnnotation() {
        assertNotNull(annotation);
    }
            
    @Test
    public void shouldChangeStateToForApproval() {
        assertEquals("FORAPPROVAL", annotation.get("state"));
    }
}
