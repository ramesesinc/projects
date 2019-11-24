package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import test.utils.Data;
import test.utils.Lookup;
import test.utils.ServiceUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineTransactionStandardTest {
    protected static FaasModel faasModel;
    protected static Map faas;
    protected static Map newFaas;
    protected static Map ledger;
    
    static{
        faasModel = new FaasModel();
        faas = new HashMap();
        newFaas = new HashMap();
        ledger  = new HashMap();
    }
    
    @Test
    public void shouldInsertReceiveTask() {
        Map task = getReceiverTask();
        assertNotNull(task);
        assertEquals("receiver", task.get("state"));
    }
    
    @Test
    public void shouldPostTransferFaasTxnRef() {
        Map ref = Data.getTxnRef(newFaas.get("objid"));
        assertNotNull(ref);
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
        Map rpu = (Map) newFaas.get("rpu");
        Map owner = (Map) newFaas.get("owner");
        
        assertEquals("previous faasid should match faas objid", item.get("prevfaasid"), faas.get("objid"));
        assertEquals("previous tdno should match faas tdno", item.get("prevtdno"), faas.get("tdno"));
        assertEquals("previous pin should match faas pin", item.get("prevpin"), faas.get("fullpin"));
        assertEquals("previous owner should match faas owner", item.get("prevowner"), owner.get("name"));
        assertEquals("previous mv should match faas mv", item.get("prevmv"), Data.format(rpu.get("totalmv")));
        assertEquals("previous av should match faas av", item.get("prevav"), Data.format(rpu.get("totalav")));
        assertEquals("previous areaha should match faas areaha", item.get("prevareaha"), Data.format(rpu.get("totalareaha"), "#,##0.000000"));
    }
    
    
    protected static Map createTransactionInfo(String txnType) {
        return createTransactionInfo(txnType, false);
    }
    
    protected static Map createTransactionInfo(String txnType, boolean manualMode) {
        Map refFaas = Lookup.faas(faas.get("tdno"));
        Map info = new HashMap();
        info.put("txntype", Data.getTxnType(txnType));
        info.put("datacapture", manualMode);
        info.put("faas", refFaas);
        return info;
    }
    
    
    protected static void simulatePaidLedger(Map info){
        Map faas = (Map) info.get("faas");
        Map ledger = Data.getLedgerByFaas(faas);
        assertNotNull(ledger);
        
        Map pdate = Data.parseCurrentDate();
        ledger.put("_schemaname", "rptledger");
        ledger.put("lastyearpaid", pdate.get("year"));
        ledger.put("lastqtrpaid", 4);
        ServiceUtil.persistence().update(ledger);
    }

    
    protected Map getReceiverTask() {
        Map param = new HashMap();
        param.put("_schemaname", "faas_task");
        param.put("refid", newFaas.get("objid"));
        param.put("state", "receiver");
        param.put("where", new Object[]{"refid = :refid and state = :state and enddate is null", param});
        return ServiceUtil.query().findFirst(param);
    }
    
}
