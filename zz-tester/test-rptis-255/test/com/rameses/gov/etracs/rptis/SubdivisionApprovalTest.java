package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import com.rameses.gov.etracs.rptis.models.SubdivisionModel;
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


public class SubdivisionApprovalTest  {
    protected static FaasModel faasModel;
    protected static SubdivisionModel subdivisionModel;
    
    protected static Map motherLand;
    protected static Map subdivision;
    protected static Map remoteSudivision;
    
    static {
        faasModel = new FaasModel();
        subdivisionModel = new SubdivisionModel();
    }
    
    @BeforeClass
    public static void setUpClass() {
        Data.cleanUp();
        Data.cleanUp(true);
        motherLand = faasModel.createAndApprove();
        motherLand = faasModel.open(motherLand, true);
        
        subdivision = subdivisionModel.receive(motherLand);
        subdivision = subdivisionModel.taxmap(subdivision, motherLand);
        subdivision = subdivisionModel.appraise(subdivision);
        subdivision = subdivisionModel.recommend(subdivision);

        subdivision = subdivisionModel.remoteApproval(subdivision);
        
        subdivision = subdivisionModel.open(subdivision);
        remoteSudivision = subdivisionModel.open(subdivision, true);
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
    public void shouldSetSubdivisionStateToApproved() {
        assertEquals("APPROVED", subdivision.get("state"));
    }
    
    @Test
    public void shouldCancelMotherLands() {
        List<Map> list = Data.getList("subdivision_motherland");
        assertNotNull(list);
        assertTrue(!list.isEmpty());
        testCancelledMotherLands(list);
    }
    
    @Test
    public void shouldSetSubdividedFaasStateToCurrent() {
        List<Map> list = Data.getList("subdivision_subdividedland");
        assertNotNull(list);
        assertEquals("subdivided faases count should be 2", 2, list.size());
        testSubdividedFaasesStateSetToCurrent(list, false);
    }
    
    
    /*---------------------------------------------------
     * REMOTE TEST
     ----------------------------------------------------*/
    
    @Test
    public void shouldCreateRemoteSubdivision() {
        assertNotNull(remoteSudivision);
    }
    
    @Test
    public void shouldCreateRemoteMotherLands() {
        List<Map> list = Data.getList("subdivision_motherland", true);
        assertNotNull(list);
        assertTrue(!list.isEmpty());
    }
    
    @Test
    public void shouldCreateRemoteSubdividedLands() {
        List<Map> list = Data.getList("subdivision_subdividedland", true);
        assertNotNull(list);
        assertTrue(!list.isEmpty());
    }
    
    @Test
    public void shouldCancelRemoteMotherLands() {
        List<Map> list = Data.getList("subdivision_motherland", true);
        assertNotNull(list);
        assertTrue(!list.isEmpty());
        testCancelledMotherLands(list);
    }
    
    @Test
    public void shouldSetRemoteSubdividedFaasesStateToCurrent() {
        List<Map> list = Data.getList("subdivision_subdividedland");
        assertNotNull(list);
        assertEquals("subdivided faases count should be 2", 2, list.size());
        testSubdividedFaasesStateSetToCurrent(list, false);
    }
    
    
    @Test
    public void shouldCreateRemoteRequirements() {
        List<Map> list = Data.getList("rpt_requirement", true);
        assertNotNull(list);
        assertTrue(!list.isEmpty());
    }
    
    @Test
    public void shouldCreateRemoteExaminationFindings() {
        List<Map> list = Data.getList("rpt_examination", true);
        assertNotNull(list);
        assertTrue(!list.isEmpty());
    }
    
    @Test
    public void shouldSetRemoteStateToApproved() {
        assertEquals("APPROVED", remoteSudivision.get("state"));
    }
    
    

    private void testCancelledMotherLands(List<Map> list) {
        for (Map mland : list) {
            Map faas = new HashMap();
            faas.put("objid", mland.get("landfaasid"));
            faas = faasModel.open(faas);
            Map rpu = (Map) faas.get("rpu");
            Map rp = (Map ) faas.get("rp");
            
            assertEquals("should set mother land state to cancelled", "CANCELLED", faas.get("state"));
            assertEquals("should set mother land cancelreason to SD", "SD", faas.get("cancelreason"));
            assertNotNull("should set mother land canceldate", faas.get("canceldate"));
            assertNotNull("should set mother land cancelledbytdnos", faas.get("cancelledbytdnos"));
            assertEquals("should set mother land rpu state to cancelled", "CANCELLED", rpu.get("state"));
            assertEquals("should set mother land realproperty state to cancelled", "CANCELLED", rp.get("state"));
        }
    }

    private void testSubdividedFaasesStateSetToCurrent(List<Map> slands, boolean remote) {
        for (Map sland: slands) {
            Map faas = new HashMap();
            faas.put("objid", sland.get("newfaasid"));
            faas = faasModel.open(faas, remote);
            Map rpu = (Map) faas.get("rpu");
            Map rp = (Map) faas.get("rp");
            
            assertEquals("faas state should be set to current", "CURRENT", faas.get("state"));
            assertEquals("rpu state should be set to current", "CURRENT", rpu.get("state"));
            assertEquals("realproperty state should be set to current", "CURRENT", rp.get("state"));
        }
    }
}
