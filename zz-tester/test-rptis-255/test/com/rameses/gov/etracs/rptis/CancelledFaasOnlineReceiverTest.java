package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import com.rameses.gov.etracs.rptis.models.CancelledFaasModel;
import test.utils.Data;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CancelledFaasOnlineReceiverTest  {
    protected static FaasModel faasModel;
    protected static CancelledFaasModel cancelledModel;
    
    protected static Map faas;
    protected static Map received;
    
    static {
        faasModel = new FaasModel();
        cancelledModel = new CancelledFaasModel();
    }
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.createAndApprove();
        received = cancelledModel.receive(faas);
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
    
      
    @Test
    public void shouldLogInitAction(){
        Map log = Data.getLog("cancelledfaas", received, "init");
        assertNotNull(log);
    }
    
    @Test
    public void shouldCreateCancelledFaas() {
        assertNotNull(received);
        assertEquals("DRAFT", received.get("state"));
    }
    
    @Test
    public void receiverTaskShouldBeOpened() {
        assertNotNull(received);
        Map task = Data.getTask("cancelledfaas_task", received, "receiver");
        assertNotNull(task);
        assertNull(task.get("enddate"));
    }
    

}
