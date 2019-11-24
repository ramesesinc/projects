package com.rameses.gov.etracs.rptis;

import com.rameses.gov.etracs.rptis.models.Entity;
import test.rptis.models.FaasData;
import test.rptis.models.FaasModel;
import test.utils.Data;
import test.utils.Lookup;
import test.utils.ServiceUtil;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FaasTransactionWithTaxDifferenceTest  {
    protected static FaasModel faasModel;
    protected static Map faas;
    protected static Map newFaas;
    protected static Map ledger;
    protected static int YEAR_DIFFERENCE = 2;
    protected static BigDecimal AV_DIFFERENCE;
    
    static {
        faasModel = new FaasModel();
        faas = new HashMap();
        newFaas = new HashMap();
        ledger = new HashMap();
    }
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.initAndCapture();
        faas = faasModel.approve(faas);
        ledger = faasModel.approveLedger(faas);
        reassessFaas();
        approve();
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
    public void shouldChangeReassessStateToCurrent() {
        assertEquals("CURRENT", newFaas.get("state"));
    }
    
    @Test
    public void shouldSetReassessPrevTdNoEqualFaasTdNo() {
        assertEquals(faas.get("tdno"), newFaas.get("prevtdno"));
    }
    
    @Test
    public void shouldCreatePreviousFaasList() {
        Map param = new HashMap();
        param.put("_schemaname", "faas_previous");
        param.put("faasid", newFaas.get("objid"));
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
        assertEquals("prevfaas cancelledbytdnos should equal faas tdno", newFaas.get("tdno"), faas.get("cancelledbytdnos"));
        
        Map txntype = (Map)newFaas.get("txntype");
        assertEquals("prevfaas cancelreason should equal faas txntype", txntype.get("displaycode"), faas.get("cancelreason"));
        
        String expectedDate = Data.format(newFaas.get("dtapproved"));
        String actualDate = Data.format(faas.get("canceldate"));
        assertEquals("prevfaas canceldate should equal faas dtapproved", expectedDate, actualDate);
    }
    
    @Test
    public void shouldSetPreviousHistoryStateToCancelled() {
        Map item = getLedgerFaas(Data.toMapObjid(faas));
        assertNotNull(item);
        
        int effectivityYear = (Integer) newFaas.get("effectivityyear");
        int fromYear = (Integer) item.get("fromyear");
        
        if (effectivityYear == fromYear){
            assertEquals("previous history state should be cancelled for fromyear = effectivityear", "CANCELLED", item.get("state"));
        }
    }
    
    @Test
    public void historyCountShouldBeTwo() {
        Map ledger = Data.getLedgerByFaas(newFaas);
        Map params = new HashMap();
        params.put("_schemaname", "rptledger_faas");
        params.put("rptledgerid", ledger.get("objid"));
        params.put("where", new Object[]{"rptledgerid = :rptledgerid", params});
        
        List items = ServiceUtil.query().getList(params);
        assertEquals("Ledger history count", 2, items.size());
    }
    
    @Test
    public void shouldPostTaxDifferenceEntries() {
        Map ledger = Data.getLedgerByFaas(newFaas);
        Map params = new HashMap();
        params.put("_schemaname", "rptledger_item");
        params.put("rptledgerid", ledger.get("objid"));
        params.put("where", new Object[]{"parentid = :rptledgerid", params});
        
        List<Map> items = ServiceUtil.query().getList(params);
        assertNotNull("Tax difference entries should not be null", items);
        
        int expectedCount = (YEAR_DIFFERENCE + 1) * 2;
        assertEquals("Tax difference entries count", expectedCount, items.size());
        
        for (Map item : items){
            assertEquals("item should set tax difference to true", true, Data.toBool(item.get("taxdifference")));
        }
    }
    
    @Test
    public void shouldClosePreviousLedgerFaasToYearQtr() {
        Map item = getLedgerFaas(Data.toMapObjid(faas));
        
        int prevEffectivity = (Integer)faas.get("effectivityyear");
        int transferEffectivity = (Integer)newFaas.get("effectivityyear");
        
        int effectivityYear = (Integer)newFaas.get("effectivityyear");
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
    public void shouldCreateNewLedgerHistoryForReassessFaas() {
        Map item = getLedgerFaas(Data.toMapObjid(newFaas));
        assertNotNull(item);
        
        int effectivityYear = (Integer)newFaas.get("effectivityyear");
        int effectivityQtr = (Integer)newFaas.get("effectivityqtr");
        assertEquals("transfer history fromyear should equal effectivityyear", effectivityYear, item.get("fromyear"));
        assertEquals("transfer history fromqtr should equal effectivityqtr", effectivityQtr, item.get("fromqtr"));
        assertEquals("transfer history toyear should equal 0", 0, item.get("toyear"));
        assertEquals("transfer history toqtr should equal 0", 0, item.get("toqtr"));
    }
    
    @Test
    public void shouldCreateDifferenceAv() {
        List<Map> differences = getAvDifferences();
        assertNotNull(differences);
        assertEquals("Difference count", YEAR_DIFFERENCE + 1, differences.size());
        
        int year = Data.getCurrentYear() - YEAR_DIFFERENCE;
        for (int i = 0; i < differences.size(); i++){
            Map item = differences.get(i);
            assertEquals("Item " + i + " year", year, item.get("year"));
            assertEquals("Item " + i + " av", AV_DIFFERENCE, item.get("av"));
            ++year;
        }
    }
    
    
    private static void reassessFaas() {
        Map info = createReassessInfo();
        simulatePaidLedger(info);
        newFaas = faasModel.initOnlineTransaction(info);
        assertNotNull(newFaas);
        updateReassessFaas(newFaas);
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
        
        Map params = new HashMap();
        params.put("_schemaname", "rptledger_item");
        Map finder = new HashMap();
        finder.put("parentid", ledger.get("objid"));
        params.put("findBy", finder);
        ServiceUtil.persistence().removeEntity(params);
    }

    private static Map createReassessInfo() {
        Map refFaas = Lookup.faas(faas.get("tdno"));
        Map info = new HashMap();
        info.put("txntype", Data.getTxnType("RE"));
        info.put("datacapture", true);
        info.put("faas", refFaas);
        return info;
    }

    private static void updateReassessFaas(Map reassessFaas) {
        Map newOwner = Entity.findEntity(Entity.ENTITY_001);
        reassessFaas.put("taxpayer", newOwner);
        reassessFaas.put("owner", newOwner);
        reassessFaas.put("tdno", "RE-" + faas.get("tdno"));
        reassessFaas.put("memoranda", "REASSESS");
        FaasData.buildFaasSignatories(reassessFaas);
        
        //set effectivity to cy - 2
        int neweffectivityyear = Data.getCurrentYear() - YEAR_DIFFERENCE; 
        reassessFaas.put("effectivityyear", neweffectivityyear);
        
        //increase totalav 
        Map rpu = (Map) reassessFaas.get("rpu");
        double currentAv = Data.toDecimal(rpu.get("totalav")).doubleValue();
        double newAv = currentAv * 1.5;
        AV_DIFFERENCE = Data.toDecimal(newAv - currentAv);
        rpu.put("totalav", Data.toDecimal(newAv));
        reassessFaas = faasModel.update(reassessFaas);
    }

    private Map getLedgerFaas(Map faas) {
        faas.put("_schemaname", "rptledger_faas");
        faas.put("where", new Object[]{"faasid = :objid", faas});
        Map item = ServiceUtil.query().findFirst(faas);
        assertNotNull(item);
        return item;
    }
    
    private List<Map> getAvDifferences() {
        Map ledger = Data.getLedgerByFaas(newFaas);
        Map m = new HashMap();
        m.put("_schemaname", "rptledger_avdifference");
        m.put("orderBy", "year");
        m.put("where", new Object[]{"parent_objid = :objid", ledger});
        return ServiceUtil.query().getList(m);
    }
    
    private static void approve() {
        newFaas = faasModel.open(newFaas);
        newFaas = faasModel.approve(newFaas);
        newFaas = faasModel.open(newFaas);
        faas = faasModel.open(faas);
    }
    
}
