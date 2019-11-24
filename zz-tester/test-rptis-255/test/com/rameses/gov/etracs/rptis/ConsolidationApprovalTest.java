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


public class ConsolidationApprovalTest  {
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
        
        consolidation = consolidationModel.remoteApproval(consolidation);
        
        consolidation = consolidationModel.open(consolidation);
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
    public void shouldSetConsolidationStateToApproved() {
        assertEquals("APPROVED", consolidation.get("state"));
    }
    
    @Test
    public void shouldSetConsolidationFaasStateToCurrent() {
        Map faas = new HashMap();
        faas.put("objid", consolidation.get("newfaasid"));
        faas = faasModel.open(faas);
        assertEquals("CURRENT", faas.get("state"));
    }
    
    @Test
    public void shouldCancelConsolidatedLands() {
        List<Map> list = Data.getList("consolidation_consolidated_land");
        assertNotNull(list);
        assertTrue(!list.isEmpty());
        testCancelledConsolidatedLands(list);
    }
    
    
    /*================================================================
     * REMOTE TEST
     =================================================================*/
    
    @Test
    public void shouldSetRemoteConsolidationStateToApproved() {
        assertEquals("APPROVED", remoteConsolidation.get("state"));
    }
    
        @Test
    public void shouldSetRemoteConsolidationFaasToCurrent() {
        Map faas = new HashMap();
        faas.put("objid", consolidation.get("newfaasid"));
        faas = faasModel.open(faas, true);
        assertEquals("CURRENT", faas.get("state"));
    }
        
    @Test
    public void shouldCancelRemoteConsolidatedLands() {
        List<Map> list = Data.getList("consolidation_consolidated_land", true);
        assertNotNull(list);
        assertTrue(!list.isEmpty());
        testCancelledConsolidatedLands(list);
    }
    
    private void testCancelledConsolidatedLands(List<Map> list) {
        for (Map mland : list) {
            Map faas = new HashMap();
            faas.put("objid", mland.get("landfaasid"));
            faas = faasModel.open(faas);
            Map rpu = (Map) faas.get("rpu");
            Map rp = (Map ) faas.get("rp");
            
            assertEquals("should set consolidated land state to cancelled", "CANCELLED", faas.get("state"));
            assertEquals("should set consolidated land cancelreason to CS", "CS", faas.get("cancelreason"));
            assertNotNull("should set consolidated land canceldate", faas.get("canceldate"));
            assertNotNull("should set consolidated land cancelledbytdnos", faas.get("cancelledbytdnos"));
            assertEquals("should set consolidated land rpu state to cancelled", "CANCELLED", rpu.get("state"));
            assertEquals("should set consolidated land realproperty state to cancelled", "CANCELLED", rp.get("state"));
        }
    }
    
}
