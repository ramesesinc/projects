package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import com.rameses.gov.etracs.rptis.models.CancelledFaasModel;
import test.utils.Data;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CancelledFaasManualTest  {
    protected static FaasModel faasModel;
    protected static CancelledFaasModel cancelledModel;
    
    protected static Map faas;
    protected static Map cancelledFaas;
    
    static {
        faasModel = new FaasModel();
        cancelledModel = new CancelledFaasModel();
    }
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.createAndApprove();
        cancelledFaas = cancelledModel.createAndApprove(faas);
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
     * CANCELLEDFAAS RELATED TEST 
     ----------------------------------------------------------*/
    
    @Test
    public void shouldChangeCancelledFaasStateToApproved() {
        assertNotNull(cancelledFaas);
        assertEquals("APPROVED", cancelledFaas.get("state"));
    }
    
    @Test
    public void shouldLogApproveAction(){
        Map log = Data.getLog("cancelledfaas", cancelledFaas, "approve");
        assertNotNull(log);
    }
    
    
    
    /*----------------------------------------------------------
     * FAAS RELATED TEST 
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
    
    
    /*----------------------------------------------------------
     * REMOTE TEST 
     ----------------------------------------------------------*/
    @Test
    public void shouldCreateRemoteCancelledFaasRecord() {
        Map remote = Data.findRemoteEntity("cancelledfaas", cancelledFaas, true);
        assertNotNull(remote);
    }
    
    @Test
    public void shouldChangeRemoteRpuStateToCancelled() {
        Map rpu = new HashMap();
        rpu.put("objid", faas.get("rpuid"));
        Map remote = Data.findRemoteEntity("rpu", rpu);
        assertNotNull(remote);
        assertEquals("CANCELLED", remote.get("state"));
    }
    
    @Test
    public void shouldChangeRemoteFaasStateToCancelled() {
        Map remote = Data.findRemoteEntity("faas", faas);
        assertNotNull(remote);
        assertEquals("CANCELLED", remote.get("state"));
    }
        
}
