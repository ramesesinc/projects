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

public class CancelledFaasOnlineTaxmapperTest  {
    protected static FaasModel faasModel;
    protected static CancelledFaasModel cancelledModel;
    
    protected static Map faas;
    protected static Map received;
    protected static Map taxmapped;
    
    static {
        faasModel = new FaasModel();
        cancelledModel = new CancelledFaasModel();
    }
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.createAndApprove();
        received = cancelledModel.receive(faas);
        taxmapped = cancelledModel.taxmap(received);
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
     * CANCELLEDFAAS RELATED TEST 
     ----------------------------------------------------------*/
    
    @Test
    public void shouldCreateCancelledFaas() {
        assertNotNull(taxmapped);
        assertEquals("Taxmapped cancelled faas state should be DRAFT.", "DRAFT", taxmapped.get("state"));
    }
    
    @Test
    public void receiverTaskShouldBeClosed() {
        assertNotNull(received);
        Map task = Data.getTask("cancelledfaas_task", received, "receiver");
        assertNotNull(task);
        assertNotNull(task.get("enddate"));
    }
    
    @Test
    public void taxmapperTaskShouldBeOpened() {
        assertNotNull(taxmapped);
        Map task = Data.getTask("cancelledfaas_task", taxmapped, "taxmapper");
        assertNotNull(task);
        assertNull(task.get("enddate"));
    }
    
}
