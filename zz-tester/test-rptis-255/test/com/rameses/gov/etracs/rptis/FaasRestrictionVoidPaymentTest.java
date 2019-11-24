package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import com.rameses.gov.etracs.rptis.models.FaasRestrictionModel;
import test.utils.Data;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FaasRestrictionVoidPaymentTest  {
    protected static FaasModel faasModel;
    protected static FaasRestrictionModel restrictionModel;
    
    protected static Map faas;
    protected static Map restriction;
    protected static Map remoteRestriction;
    
    
    static {
        faasModel = new FaasModel();
        restrictionModel = new FaasRestrictionModel();
    }
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.createAndApprove();
        faasModel.open(faas, true);
        restriction = restrictionModel.init(faas);
        restriction = restrictionModel.create(restriction);
        restriction = restrictionModel.approve(restriction);
        remoteRestriction = restrictionModel.open(restriction, true);
        
        //paid
        restriction = restrictionModel.cancel(restriction);
        remoteRestriction = restrictionModel.openCancelled(restriction, true);
        
        //void payment
        restriction = restrictionModel.voidPayment(restriction);
        remoteRestriction = restrictionModel.openReactivated(restriction, true);
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
     * LOCAL TEST 
     ----------------------------------------------------------*/
    
    @Test
    public void shouldCreateRestriction() {
        assertNotNull(restriction);
    }
            
    @Test
    public void shouldChangeStateToActive() {
        assertEquals("ACTIVE", restriction.get("state"));
    }
    
    @Test
    public void shouldSetReceiptToNull() {
        Map receipt = (Map) restriction.get("receipt");
        assertNull(receipt);
    }

    @Test
    public void shouldLogReactivate() {
        Map log = Data.getLog("faas_restriction", restriction, "reactivate");
        assertNotNull(log);
    }
    
    
    /*----------------------------------------------------------
     * REMOTE TEST 
     ----------------------------------------------------------*/
    
    @Test
    public void shouldCreateRemoteRestriction() {
        assertNotNull(remoteRestriction);
    }
            
    @Test
    public void shouldSetRemoteStateToActive() {
        assertEquals("ACTIVE", remoteRestriction.get("state"));
    }
    
    @Test
    public void shouldSetRemoteReceiptInfo() {
        Map receipt = (Map) remoteRestriction.get("receipt");
        if (receipt != null) {
            assertNull("receipt objid should be null", receipt.get("objid"));
            assertNull("receipt no should be null", receipt.get("receiptno"));
            assertNull("receipt date should be null", receipt.get("receiptdate"));
            assertNull("receipt amount should be null", receipt.get("amount"));
            assertNull("receipt lastyearpaid should be null", receipt.get("lastyearpaid"));
            assertNull("receipt lastqtrpaid should be null", receipt.get("lastqtrpaid"));
        } 
    }

    
}
