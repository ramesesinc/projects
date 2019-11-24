package test.rptis;

import test.rptis.models.FaasModel;
import test.utils.Data;
import test.utils.ServiceUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class FaasDataCaptureLandTest {
    protected static FaasModel faasModel;
    protected static Map faas;
    
    static {
        faasModel = new FaasModel();
    }
    
    @BeforeClass
    public static void setUpClass() {
        Map info = faasModel.createInitialInfo();
        faas = faasModel.initCaptureAndCreate(info);
        faas = faasModel.capture(faas);
    }
    
    @AfterClass
    public static void tearDownClass() {
        Data.cleanUp();
        Data.cleanUp(true);
    }
    
    @Test
    public void shouldCreateRealProperty() {
        Map rp = (Map) faas.get("rp");
        Map param = new HashMap();
        param.put("_schemaname", "realproperty");
        param.put("where", new Object[]{"objid = :objid", Data.toMapObjid(rp)});
        Map master = ServiceUtil.query().findFirst(param);
        assertNotNull(master);
    }
    
    @Test
    public void shouldCreateRpuMaster() {
        Map rpu = (Map) faas.get("rpu");
        Map param = new HashMap();
        param.put("_schemaname", "rpumaster");
        param.put("where", new Object[]{"objid = :rpumasterid", rpu});
        Map master = ServiceUtil.query().findFirst(param);
        assertNotNull(master);
    }

    @Test
    public void shouldCreateRpu() {
        Map rpu = (Map) faas.get("rpu");
        Map param = new HashMap();
        param.put("_schemaname", "rpu");
        param.put("where", new Object[]{"objid = :objid", Data.toMapObjid(rpu)});
        Map master = ServiceUtil.query().findFirst(param);
        assertNotNull(master);
    }
    
    @Test
    public void shouldCreateRpuAssessments() {
        Map rpu = (Map) faas.get("rpu");
        Map param = new HashMap();
        param.put("_schemaname", "rpu_assessment");
        param.put("where", new Object[]{"rpuid = :objid", Data.toMapObjid(rpu)});
        List<Map> items = ServiceUtil.query().getList(param);
        assertNotNull(items);
        assertEquals("Assessment count is 1", 1, items.size());
    }
    
    @Test
    public void shouldCreatePreviousFaases() {
        Map param = new HashMap();
        param.put("_schemaname", "faas_previous");
        param.put("where", new Object[]{"faasid = :objid", Data.toMapObjid(faas)});
        List<Map> items = ServiceUtil.query().getList(param);
        assertNotNull(items);
        assertEquals("prevous faases count is 1", 1, items.size());
    }
    
    @Test
    public void shouldCreateSignatory() {
        Map param = new HashMap();
        param.put("_schemaname", "faas_signatory");
        param.put("where", new Object[]{"objid = :objid", Data.toMapObjid(faas)});
        Map data = ServiceUtil.query().findFirst(param);
        assertNotNull(data);
        assertNotNull("taxmapper should not be null", data.get("taxmapper"));
        assertNotNull("appraiser should not be null", data.get("appraiser"));
        assertNotNull("recommender should not be null", data.get("recommender"));
        assertNotNull("approver should not be null", data.get("approver"));
    }
    
    @Test
    public void shouldOpenFaas(){
        try {
            Map actual = faasModel.open(faas);
            assertNotNull(actual);
        } catch(Exception ex ){
            fail("Should not throw exception");
        }
    }
    
    @Test
    public void shouldLogCreate(){
        Map param = new HashMap();
        param.put("_schemaname", "txnlog");
        param.put("where", new Object[]{"refid = :objid and action = 'createfaas'", Data.toMapObjid(faas)});
        Map data = ServiceUtil.query().findFirst(param);
        assertNotNull(data);
    }
    
    @Test
    public void shouldLogUpdate(){
        Map param = new HashMap();
        param.put("_schemaname", "txnlog");
        param.put("where", new Object[]{"refid = :objid and action = 'updatefaas'", Data.toMapObjid(faas)});
        Map data = ServiceUtil.query().findFirst(param);
        assertNotNull(data);
    }
}
