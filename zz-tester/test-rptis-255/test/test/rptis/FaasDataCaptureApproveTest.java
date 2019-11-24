package test.rptis;

import test.rptis.models.FaasModel;
import test.utils.Data;
import test.utils.ServiceUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FaasDataCaptureApproveTest {

    protected static FaasModel faasModel;
    protected static Map faas;
    protected static Map remoteFaas;

    static {
        faasModel = new FaasModel();
    }

    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.initAndCapture();
        faas = faasModel.approve(faas);
        remoteFaas = Data.findRemoteEntity("faas", faas, true);
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
    public void shouldChangeRealPropertyStateToCurrent() {
        assertNotNull(faas);
        Map rp = (Map) faas.get("rp");
        assertEquals("CURRENT", rp.get("state"));
    }

    @Test
    public void shouldChangeRpuStateToCurrent() {
        assertNotNull(faas);
        Map rpu = (Map) faas.get("rpu");
        assertEquals("CURRENT", rpu.get("state"));
    }

    @Test
    public void shouldChangeFaasStateToCurrent() {
        assertNotNull(faas);
        assertEquals("CURRENT", faas.get("state"));
    }

    @Test
    public void shouldLogApprove() {
        Map param = new HashMap();
        param.put("_schemaname", "txnlog");
        param.put("where", new Object[]{"refid = :objid and action = 'approvefaas'", Data.toMapObjid(faas)});
        Map data = ServiceUtil.query().findFirst(param);
        assertNotNull(data);
    }

    @Test
    public void shouldCreateLedger() {
        Map param = new HashMap();
        param.put("_schemaname", "rptledger");
        param.put("where", new Object[]{"faasid = :objid", Data.toMapObjid(faas)});
        Map ledger = ServiceUtil.query().findFirst(param);
        assertNotNull(ledger);
        assertEquals("ledger state should be pending", "PENDING", ledger.get("state"));
        assertEquals("ledger faasid should not be equal to faas.objid", ledger.get("faasid"), faas.get("objid"));
    }

    @Test
    public void shouldCreateLedgerFaas() {
        Map param = new HashMap();
        param.put("_schemaname", "rptledger_faas");
        param.put("where", new Object[]{"rptledgerid = :objid", Data.toMapObjid(faas)});
        List<Map> items = ServiceUtil.query().getList(param);
        assertNotNull(items);
        assertEquals("faas history count should be 1", 1, items.size());

        Map item = items.get(0);
        assertEquals("faas history from year should equal effectivity year", item.get("fromyear"), faas.get("effectivityyear"));
        assertEquals("faas history from qtr should equal effectivity qtr", item.get("fromqtr"), faas.get("effectivityqtr"));
        assertEquals("faas history to year should be 0", item.get("toyear"), 0);
        assertEquals("faas history to qtr should be 0", item.get("toqtr"), 0);
    }

    @Test
    public void shouldSetYearQtrMonthDay() {
        int year = (Integer) faas.get("year");
        int qtr = (Integer) faas.get("qtr");
        int mon = (Integer) faas.get("month");
        int day = (Integer) faas.get("day");

        Map approver = (Map) faas.get("approver");
        assertNotNull("Approver should be set", approver);
        assertNotNull("Approver dtsigned should be set", approver.get("dtsigned"));

        Map pdate = Data.parseDate(approver.get("dtsigned"));
        assertEquals("year should equal approved year", pdate.get("year"), year);
        assertEquals("qtr should equal approved qtr", pdate.get("qtr"), qtr);
        assertEquals("month should equal approved month", pdate.get("month"), mon);
        assertEquals("day should equal approved day", pdate.get("day"), day);
    }

    /*-----------------------------------------------
     * REMOTE TEST
     -----------------------------------------------*/
    @Test
    public void shouldCreateRemoteFaas() {
        assertNotNull(remoteFaas);
    }

    @Test
    public void remoteStateShouldBeCurrent() {
        assertEquals("CURRENT", remoteFaas.get("state"));
    }
}
