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

public class CancelledFaasOnlineApproverTest  {
    protected static FaasModel faasModel;
    protected static CancelledFaasModel cancelledModel;
    
    protected static Map faas;
    protected static Map received;
    protected static Map taxmapped;
    protected static Map recommended;
    protected static Map approved;
    protected static Map remoteCancelledFaas;
    
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
        remoteCancelledFaas = cancelledModel.remoteApproval(recommended);
        approved = cancelledModel.reload(remoteCancelledFaas);
        faas = faasModel.open(faas);
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
    * REMOTE CANCELLED FAAS TEST
    ----------------------------------------------------------*/
    
    @Test
    public void shouldChangeRemoteCancelledFaasStateToApproved() {
        assertNotNull(remoteCancelledFaas);
        assertEquals("APPROVED", remoteCancelledFaas.get("state"));
    }
    
    @Test
    public void shouldLogRemoteOnlineApproveAction(){
        Map log = Data.getLog("cancelledfaas", remoteCancelledFaas, "onlineApprove", true);
        assertNotNull(log);
    }
    
    @Test
    public void shouldChangeRemoteRpuStateToCancelled() {
        Map remoteRpu = Data.findRemoteEntity("rpu", (Map)faas.get("rpu"));
        assertNotNull(remoteRpu);
        assertEquals("CANCELLED", remoteRpu.get("state"));
    }
    
    @Test
    public void shouldChangeRemoteFaasStateToCancelled() {
        Map remoteFaas = Data.findRemoteEntity("faas", faas);
        assertNotNull(remoteFaas);
        assertEquals("CANCELLED", remoteFaas.get("state"));
    }
    
    
    
    
    /*----------------------------------------------------------
     * LOCAL FAAS TEST 
     ----------------------------------------------------------*/
    @Test
    public void shouldChangeRpuStateToCancelled() {
        assertNotNull(faas);
        Map rpu = (Map) faas.get("rpu");
        assertEquals("CANCELLED", rpu.get("state"));
    }
    
    @Test
    public void shouldChangeFaasStateToCancelled() {
        assertNotNull(faas);
        assertEquals("CANCELLED", faas.get("state"));
    }
    
    @Test
    public void shouldSyncRemoteTasks() {
        Map provtaxmapper = Data.getTask("cancelledfaas_task", remoteCancelledFaas, "provtaxmapper");
        assertNotNull("provtaxmapper task should be synched", provtaxmapper);
        
        Map provrecommender = Data.getTask("cancelledfaas_task", remoteCancelledFaas, "provrecommender");
        assertNotNull("provrecommender task should be synched", provrecommender);
        
        Map approver = Data.getTask("cancelledfaas_task", remoteCancelledFaas, "approver");
        assertNotNull("approver task should be synched", approver);
    }
    
    @Test
    public void shouldUpdateRemoteSignatories() {
        Map signatory = Data.findEntity("cancelledfaas_signatory", remoteCancelledFaas);
        assertNotNull(signatory);
        Map approver = (Map) signatory.get("approver");
        assertNotNull("Approver name must be set", approver.get("name"));
        assertNotNull("Approver dtsigned must be set", approver.get("dtsigned"));
    }
    
    /*----------------------------------------------------------
     * LEDGER RELATED TEST 
     ----------------------------------------------------------*/
    @Test
    public void ledgerShouldNotBeDeleted() {
        Map org = Data.getRootOrg();
        if (org != null && "PROVINCE".equalsIgnoreCase(org.get("orgclass").toString())){
            Map ledger = Data.getLedgerByFaas(faas);
            assertNotNull(ledger);
        }
    }
    
    
}
