package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import com.rameses.gov.etracs.rptis.models.RedFlagModel;
import test.utils.Data;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;

public class FaasTransferOnlineReceiveRedFlagTest  {
    protected static FaasModel faasModel;
    protected static RedFlagModel redFlagModel;
    protected static Map faas;
    protected static Map newFaas;
    protected static Map remoteFaas;
    protected static Map ledger;
    protected static Map redFlag;
    protected static Map remoteRedFlag;
    
    
    static {
        faasModel = new FaasModel();
        redFlagModel = new RedFlagModel();
        
        faas = new HashMap();
        newFaas = new HashMap();
        ledger = new HashMap();
    }
    
    @BeforeClass
    public static void setUpClass() {
        Data.cleanUp();
        Data.cleanUp(true);
        
        faas = faasModel.createAndApprove();
        newFaas = faasModel.receiveTransfer(faas);
        newFaas = faasModel.taxmap(newFaas);
        newFaas = faasModel.appraise(newFaas);
        newFaas = faasModel.recommend(newFaas);
        
        remoteFaas = Data.findRemoteEntity("faas", newFaas);
        
        remoteFaas = faasModel.remoteReceive(newFaas);
        remoteRedFlag = faasModel.remoteAddRedFlag(newFaas);
        
        redFlag = Data.findEntity("rpt_redflag", remoteRedFlag, true);
        redFlag = faasModel.resolveRedFlag(redFlag);
        
        remoteRedFlag = redFlagModel.openRemoteResolved(redFlag);
    }
    
    @AfterClass
    public static void tearDownClass() {
//        Data.cleanUp();
//        Data.cleanUp(true);
    }

    @Before
    public void setUp() {
        
        
    }
    
    @After
    public void tearDown() {
        
    }
    
    @Test
    public void shouldCreateFaas() {
        assertNotNull(faas);
    }
    
    @Test
    public void shouldCreateTransferFaas() {
        assertNotNull(newFaas);
    }
    
    @Test
    public void shouldCreateRedFlag() {
        assertNotNull(redFlag);
    }
    
    
    
    
    /*---------------------------------------------------
     * REMOTE TEST 
     ---------------------------------------------------*/
    @Test
    public void shouldCreateRemoteFaas(){
        assertNotNull(remoteFaas);
    }
    
    @Test
    public void shouldSetRemoteStateToResolved(){
        assertEquals("RESOLVED", remoteRedFlag.get("state"));
    }
}
