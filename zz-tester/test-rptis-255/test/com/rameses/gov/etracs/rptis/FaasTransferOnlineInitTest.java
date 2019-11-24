package com.rameses.gov.etracs.rptis;

import com.rameses.gov.etracs.rptis.models.Entity;
import test.rptis.models.FaasData;
import test.utils.Data;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

public class FaasTransferOnlineInitTest extends OnlineTransactionStandardTest {
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.initAndCapture();
        faas = faasModel.approve(faas);
        ledger = faasModel.approveLedger(faas);
        transferFaas();
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
    
    
    private static void transferFaas() {
        Map info = createTransactionInfo("TR");
        simulatePaidLedger(info);
        newFaas = faasModel.initOnlineTransaction(info);
        assertNotNull(newFaas);
        updateTransferInfo(newFaas);
    }
    

    private static void updateTransferInfo(Map onlineFaas) {
        Map newOwner = Entity.findEntity(Entity.ENTITY_001);
        onlineFaas.put("tdno", "T-" + faas.get("tdno"));
        onlineFaas.put("taxpayer", newOwner);
        onlineFaas.put("owner", newOwner);
        onlineFaas.put("memoranda", "TRANSFER");
        FaasData.buildFaasSignatories(onlineFaas);
        onlineFaas = faasModel.update(onlineFaas);
        try {
            onlineFaas = faasModel.submitForApproval(onlineFaas);
            fail("Should throw exception 'New owner should not be equal to previous owner.'");
        }catch(Exception ex){
            //should throw same owner not allowed exception 
        }
        
        newOwner = Entity.findEntity(Entity.ENTITY_002);
        onlineFaas.put("taxpayer", newOwner);
        onlineFaas.put("owner", newOwner);
        onlineFaas = faasModel.update(onlineFaas);
    }
}
