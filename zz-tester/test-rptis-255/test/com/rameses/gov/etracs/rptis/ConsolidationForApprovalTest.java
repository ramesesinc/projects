package com.rameses.gov.etracs.rptis;

import com.rameses.gov.etracs.rptis.models.ConsolidationModel;
import test.rptis.models.FaasModel;
import test.utils.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class ConsolidationForApprovalTest  {
    protected static FaasModel faasModel;
    protected static ConsolidationModel consolidationModel;
    
    protected static Map land1;
    protected static Map land2;
    protected static Map consolidatedLand;
    protected static Map consolidation;
    protected static Map remoteConsolidation;
    
    static {
        faasModel = new FaasModel();
        consolidationModel = new ConsolidationModel();
    }
    
    @BeforeClass
    public static void setUpClass() {
        Data.cleanUp();
        Data.cleanUp(true);
        
        consolidationModel.createLandsToConsolidate();
        consolidation = consolidationModel.receive();
        consolidation = consolidationModel.taxmap(consolidation);
        consolidation = consolidationModel.appraise(consolidation);
        consolidation = consolidationModel.recommend(consolidation);
        
        remoteConsolidation = consolidationModel.open(consolidation, true);
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
    public void shouldCreateConsolidation() {
        assertNotNull(consolidation);
    }
    
    @Test
    public void shouldSetConsolidationStateToForApproval() {
        assertEquals("FORAPPROVAL", consolidation.get("state"));
    }
    
    
    /*================================================================
     * REMOTE TEST
     =================================================================*/
    @Test
    public void shouldRemoteCreateConsolidation() {
        assertNotNull(remoteConsolidation);
    }
    
    @Test
    public void shouldCreateRemoteConsolidatedLands() {
        List<Map> list = Data.getList("consolidation_consolidated_land", true);
        assertNotNull(list);
        assertTrue(!list.isEmpty());
    }
    
    public void shouldCreateRemoteConsolidatedFaas() {
        Map faas = new HashMap();
        faas.put("objid", remoteConsolidation.get("newfaasid"));
        faas = faasModel.open(faas, true);
        
        assertNotNull("should create remote consolidated faas", faas);
        assertEquals("remote consolidated faas state should be pending", "PENDING", faas.get("state"));
    }
    
}
