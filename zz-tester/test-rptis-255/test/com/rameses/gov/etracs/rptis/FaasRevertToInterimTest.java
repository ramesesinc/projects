package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import test.utils.Data;
import test.utils.LocalConfig;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FaasRevertToInterimTest {

    protected static FaasModel faasModel;
    protected static Map faas;

    static {
        faasModel = new FaasModel();
    }

    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.initAndCapture();
        faas = faasModel.approve(faas);
        faas = faasModel.revertToInterim(faas);
        Data.waitRemoteEntityDeleted("faas", faas);
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
    public void shouldChangeRealPropertyStateToInterim() {
        assertNotNull(faas);
        Map rp = (Map) faas.get("rp");
        assertEquals("INTERIM", rp.get("state"));
    }

    @Test
    public void shouldChangeRpuStateToInterim() {
        assertNotNull(faas);
        Map rpu = (Map) faas.get("rpu");
        assertEquals("INTERIM", rpu.get("state"));
    }

    @Test
    public void shouldChangeFaasStateToInterim() {
        assertNotNull(faas);
        assertEquals("INTERIM", faas.get("state"));
    }

    @Test
    public void shouldRemoveLedger() {
        Map ledger = Data.getLedgerByFaas(faas);
        assertNull(ledger);
    }

    @Test
    public void shouldDeleteRemoteFaas() {
        Map remoteFaas = Data.findRemoteEntity("faas", faas);
        assertNull(remoteFaas);
    }
}
