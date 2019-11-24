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

public class FaasRestrictionPaidTest  {
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
        restriction = restrictionModel.init(faas);
        restriction = restrictionModel.create(restriction);
        restriction = restrictionModel.approve(restriction);
        remoteRestriction = restrictionModel.open(restriction, true);
        
        restriction = restrictionModel.cancel(restriction);
        restriction = restrictionModel.open(restriction);
        
        remoteRestriction = restrictionModel.openCancelled(restriction, true);
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
    public void shouldChangeStateToUnrestricted() {
        assertEquals("UNRESTRICTED", restriction.get("state"));
    }
    
    @Test
    public void shouldSetReceiptInfo() {
        Map actualReceipt = restrictionModel.getReceipt();
        Map receipt = (Map) restriction.get("receipt");
        
        assertEquals("receipt no should match", actualReceipt.get("receiptno"), receipt.get("receiptno"));
        assertEquals("receipt date should match", actualReceipt.get("receiptdate"), Data.format(receipt.get("receiptdate")));
        assertEquals("receipt amount should match", Data.format(actualReceipt.get("amount")), Data.format(receipt.get("amount")));
        assertEquals("receipt lastyearpaid should match", actualReceipt.get("lastyearpaid"), receipt.get("lastyearpaid"));
        assertEquals("receipt lastqtrpaid should match", actualReceipt.get("lastqtrpaid"), receipt.get("lastqtrpaid"));
    }

    @Test
    public void shouldLogCancel() {
        Map log = Data.getLog("faas_restriction", restriction, "cancel");
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
    public void shouldSetRemoteStateToUnrestricted() {
        assertEquals("UNRESTRICTED", remoteRestriction.get("state"));
    }
    
    @Test
    public void shouldSetRemoteReceiptInfo() {
        Map actualReceipt = restrictionModel.getReceipt();
        Map receipt = (Map) remoteRestriction.get("receipt");
        
        assertEquals("receipt no should match", actualReceipt.get("receiptno"), receipt.get("receiptno"));
        assertEquals("receipt date should match", actualReceipt.get("receiptdate"), Data.format(receipt.get("receiptdate")));
        assertEquals("receipt amount should match", Data.format(actualReceipt.get("amount")), Data.format(receipt.get("amount")));
        assertEquals("receipt lastyearpaid should match", actualReceipt.get("lastyearpaid"), receipt.get("lastyearpaid"));
        assertEquals("receipt lastqtrpaid should match", actualReceipt.get("lastqtrpaid"), receipt.get("lastqtrpaid"));
    }

    
}
