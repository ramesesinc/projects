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

public class FaasRestrictionTest {

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
        restriction = restrictionModel.open(restriction);

        remoteRestriction = restrictionModel.open(restriction, true);
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
    public void shouldLogApprove() {
        Map log = Data.getLog("faas_restriction", restriction, "approve");
        assertNotNull(log);
    }

    @Test
    public void shouldLogLedgerAddRestriction() {
        Map ledger = (Map) restriction.get("ledger");
        if (ledger != null && ledger.get("objid") != null) {
            Map log = Data.getLog("rptledger", ledger, "add_restriction");
            assertNotNull(log);
        }
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
}
