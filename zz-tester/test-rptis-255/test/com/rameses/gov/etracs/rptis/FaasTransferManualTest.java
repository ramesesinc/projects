package com.rameses.gov.etracs.rptis;

import com.rameses.gov.etracs.rptis.models.Entity;
import test.rptis.models.FaasData;
import test.rptis.models.FaasModel;
import test.utils.Data;
import test.utils.Lookup;
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

public class FaasTransferManualTest {
    protected static FaasModel faasModel;
    protected static Map faas;
    protected static Map transferFaas;
    protected static Map ledger;
    
    
    static {
        faasModel = new FaasModel();
        faas = new HashMap();
        transferFaas = new HashMap();
    }
    
    
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.initAndCapture();
        faas = faasModel.approve(faas);
        ledger = faasModel.approveLedger(faas);
        transferFaas();
        approveTransfer();
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
    public void shouldChangeLedgerStateToApproved() {
        assertEquals("APPROVED", ledger.get("state"));
    }
    
    @Test
    public void shouldChangeTransferStateToCurrent() {
        assertEquals("CURRENT", transferFaas.get("state"));
    }
    
    @Test
    public void shouldSetTransferPrevTdNoEqualFaasTdNo() {
        assertEquals(faas.get("tdno"), transferFaas.get("prevtdno"));
    }
    
    @Test
    public void shouldCreatePreviousFaasList() {
        Map param = new HashMap();
        param.put("_schemaname", "faas_previous");
        param.put("faasid", transferFaas.get("objid"));
        param.put("where", new Object[]{"faasid = :faasid", param});
        List<Map> items = (List<Map>) ServiceUtil.query().getList(param);
        assertNotNull(items);
        assertEquals(1, items.size());
        
        Map item = items.get(0);
        Map rpu = (Map) faas.get("rpu");
        Map owner = (Map) faas.get("owner");
        
        assertEquals("previous faasid should match faas objid", item.get("prevfaasid"), faas.get("objid"));
        assertEquals("previous tdno should match faas tdno", item.get("prevtdno"), faas.get("tdno"));
        assertEquals("previous pin should match faas pin", item.get("prevpin"), faas.get("fullpin"));
        assertEquals("previous owner should match faas owner", item.get("prevowner"), owner.get("name"));
        assertEquals("previous mv should match faas mv", item.get("prevmv"), Data.format(rpu.get("totalmv")));
        assertEquals("previous av should match faas av", item.get("prevav"), Data.format(rpu.get("totalav")));
        assertEquals("previous areaha should match faas areaha", item.get("prevareaha"), Data.format(rpu.get("totalareaha"), "#,##0.000000"));
    }
    
    
    @Test
    public void shouldSetPreviousFaasStateToCancelled() {
        assertEquals("CANCELLED", faas.get("state"));
    }
    
    @Test
    public void shouldSetPreviousFaasStateCancellationInfo() {
        assertEquals("prevfaas cancelledbytdnos should equal faas tdno", faas.get("cancelledbytdnos"), transferFaas.get("tdno"));
        
        Map txntype = (Map)transferFaas.get("txntype");
        assertEquals("prevfaas cancelreason should equal faas txntype", txntype.get("objid"), faas.get("cancelreason"));
        
        String expectedDate = Data.format(transferFaas.get("dtapproved"));
        String actualDate = Data.format(faas.get("canceldate"));
        assertEquals("prevfaas canceldate should equal faas dtapproved", expectedDate, actualDate);
    }
    
    @Test
    public void shouldSetPreviousHistoryStateToCancelled() {
        Map item = getLedgerFaas(faas);
        assertNotNull(item);
        
        int effectivityYear = (Integer) transferFaas.get("effectivityyear");
        int fromYear = (Integer) item.get("fromyear");
        
        if (effectivityYear == fromYear){
            assertEquals("previous history state should be cancelled for fromyear = effectivityear", "CANCELLED", item.get("state"));
        }
    }
    
    @Test
    public void shouldClosePreviousLedgerFaasToYearQtr() {
        Map item = getLedgerFaas(Data.toMapObjid(faas));
        
        int prevEffectivity = (Integer)faas.get("effectivityyear");
        int transferEffectivity = (Integer)transferFaas.get("effectivityyear");
        
        int effectivityYear = (Integer)transferFaas.get("effectivityyear");
        int toYear = effectivityYear;
        int toQtr = 4;
        
        if (prevEffectivity == transferEffectivity){
            assertEquals("previous history toyear should equal transfer effectivity", toYear, item.get("toyear"));
        } else {
            toYear = effectivityYear - 1;
            assertEquals("previous history toyear should equal transfer effectivity - 1", toYear, item.get("toyear"));
        }
        assertEquals("previous history toqtr should equal transfer 4", toQtr, item.get("toqtr"));
    }
    
    @Test
    public void shouldCreateNewLedgerHistoryForTransferFaas() {
        Map item = getLedgerFaas(Data.toMapObjid(transferFaas));
        assertNotNull(item);
        
        int effectivityYear = (Integer)transferFaas.get("effectivityyear");
        int effectivityQtr = (Integer)transferFaas.get("effectivityqtr");
        assertEquals("transfer history fromyear should equal effectivityyear", effectivityYear, item.get("fromyear"));
        assertEquals("transfer history fromqtr should equal effectivityqtr", effectivityQtr, item.get("fromqtr"));
        assertEquals("transfer history toyear should equal 0", 0, item.get("toyear"));
        assertEquals("transfer history toqtr should equal 0", 0, item.get("toqtr"));
    }
    
    
    private static void transferFaas() {
        Map info = createTransferInfo();
        simulatePaidLedger(info);
        transferFaas = faasModel.initOnlineTransaction(info);
        assertNotNull(transferFaas);
        updateTransferInfo(transferFaas);
    }
    
    private static void simulatePaidLedger(Map info){
        Map faas = (Map) info.get("faas");
        Map ledger = Data.getLedgerByFaas(faas);
        assertNotNull(ledger);
        
        Map pdate = Data.parseCurrentDate();
        ledger.put("_schemaname", "rptledger");
        ledger.put("lastyearpaid", pdate.get("year"));
        ledger.put("lastqtrpaid", 4);
        ServiceUtil.persistence().update(ledger);
    }

    private static Map createTransferInfo() {
        Map refFaas = Lookup.faas(faas.get("tdno"));
        Map info = new HashMap();
        info.put("txntype", Data.getTxnType("TR"));
        info.put("datacapture", true);
        info.put("faas", refFaas);
        return info;
    }

    private static void updateTransferInfo(Map transferFaas) {
        Map newOwner = Entity.findEntity(Entity.ENTITY_001);
        transferFaas.put("tdno", "T-" + faas.get("tdno"));
        transferFaas.put("taxpayer", newOwner);
        transferFaas.put("owner", newOwner);
        transferFaas.put("memoranda", "TRANSFER");
        FaasData.buildFaasSignatories(transferFaas);
        transferFaas = faasModel.update(transferFaas);
        try {
            transferFaas = faasModel.submitForApproval(transferFaas);
            fail("Should throw exception 'New owner should not be equal to previous owner.'");
        }catch(Exception ex){
            //should throw same owner not allowed exception 
        }
        
        newOwner = Entity.findEntity(Entity.ENTITY_002);
        transferFaas.put("taxpayer", newOwner);
        transferFaas.put("owner", newOwner);
        transferFaas = faasModel.update(transferFaas);
        transferFaas = faasModel.submitForApproval(transferFaas);
    }

    private static void approveTransfer() {
        transferFaas = faasModel.open(transferFaas);
        transferFaas = faasModel.approve(transferFaas);
        faas.putAll(faasModel.open(faas));
    }

    private Map getLedgerFaas(Map faas) {
        faas.put("_schemaname", "rptledger_faas");
        faas.put("where", new Object[]{"faasid = :objid", Data.toMapObjid(faas)});
        Map item = ServiceUtil.query().findFirst(faas);
        assertNotNull(item);
        return item;
    }
    
}
