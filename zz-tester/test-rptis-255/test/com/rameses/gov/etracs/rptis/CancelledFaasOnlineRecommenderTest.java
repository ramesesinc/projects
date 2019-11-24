package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import com.rameses.gov.etracs.rptis.models.CancelledFaasModel;
import test.utils.Data;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CancelledFaasOnlineRecommenderTest  {
    protected static FaasModel faasModel;
    protected static CancelledFaasModel cancelledModel;
    
    protected static Map faas;
    protected static Map received;
    protected static Map taxmapped;
    protected static Map recommended;
    
    static {
        faasModel = new FaasModel();
        cancelledModel = new CancelledFaasModel();
    }
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.createAndApprove();
        received = cancelledModel.receive(faas);
        taxmapped = cancelledModel.taxmap(received);
        recommended = cancelledModel.recommend(taxmapped);
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
     * 
     * RECOMMENDER RELATED TEST 
     * 
     ----------------------------------------------------------*/
    
    @Test
    public void shouldCreateCancelledFaas() {
        assertNotNull(recommended);
        assertEquals("Recommended cancelled faas state should be DRAFT.", "DRAFT", recommended.get("state"));
    }
    
    @Test
    public void taxmapperTaskShouldBeClosed() {
        assertNotNull(received);
        Map task = Data.getTask("cancelledfaas_task", received, "taxmapper");
        assertNotNull(task);
        assertNotNull(task.get("enddate"));
    }
    
    @Test
    public void recommenderTaskShouldBeClosed() {
        assertNotNull(recommended);
        Map task = Data.getTask("cancelledfaas_task", recommended, "recommender");
        assertNotNull(task);
        assertNotNull(task.get("enddate"));
    }
    
    @Test
    public void approverTaskShouldBeOpened() {
        assertNotNull(recommended);
        Map task = Data.getTask("cancelledfaas_task", recommended, "approver");
        assertNotNull(task);
        assertNull(task.get("enddate"));
    }
    
    
    /*----------------------------------------------------------
     * 
     * REOMOTE RELATED TEST 
     * 
     ----------------------------------------------------------*/
    @Test
    public void shouldCreateRemoteData() {
        Map remote = Data.findRemoteEntity("cancelledfaas", recommended, true);
        assertNotNull(remote);
        assertEquals("Remote cancelled faas should be DRAFT", "DRAFT", remote.get("state"));
    }
    
    @Test
    public void remoteAssignReceiverTaskShouldBeOpened() {
        Map task = Data.getTask("cancelledfaas_task", recommended, "assign-receiver", true);
        assertNotNull(task);
        assertNull(task.get("enddate"));
    }
    
    @Test
    public void remoteOpenTasksShouldBeOne() {
        List<Map> tasks = Data.getOpenTasks("cancelledfaas_task", recommended, true);
        assertEquals(1, tasks.size());
    }
    
    @Test
    public void remoteShouldPostExaminationFindings() {
        List<Map> list = Data.getExaminations(recommended, true);
        assertEquals(1, list.size());
    }
    
}
