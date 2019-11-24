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


public class SubdivisionForApprovalTest  {
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
        motherLand = faasModel.createAndApprove();
        motherLand = faasModel.open(motherLand, true);
        
        subdivision = subdivisionModel.receive(motherLand);
        subdivision = subdivisionModel.taxmap(subdivision, motherLand);
        subdivision = subdivisionModel.appraise(subdivision);
        subdivision = subdivisionModel.recommend(subdivision);
        
        remoteSudivision = subdivisionModel.open(subdivision, true);
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
    
    @Test
    public void shouldCreateMotherLand() {
        assertNotNull(motherLand);
    }
    
    @Test
    public void shouldCreateSubdivision() {
        assertNotNull(subdivision);
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
    public void shouldCreateRemoteSubdividedLandFaases() {
        List<Map> list = Data.getList("subdivision_subdividedland", true);
        assertNotNull(list);
        assertEquals("subdivided faases count should be 2", 2, list.size());
        
        for(Map sland : list) {
            Map remoteFaas = new HashMap();
            remoteFaas.put("objid", sland.get("newfaasid"));
            remoteFaas = faasModel.open(remoteFaas, true);
            assertNotNull(remoteFaas);
            assertEquals("state should be pending", "PENDING", remoteFaas.get("state"));
        }
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
        
}
