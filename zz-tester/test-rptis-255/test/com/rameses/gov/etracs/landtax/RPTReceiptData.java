/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.landtax;

import com.rameses.gov.etracs.services.CashReceiptService;
import com.rameses.gov.etracs.services.RPTReceiptService;
import test.utils.Data;
import test.utils.ServiceUtil;
import java.util.HashMap;
import java.util.Map;


public class RPTReceiptData {
    public static RPTReceiptService svc;
    public static CashReceiptService receiptSvc;
    
    public static String objid = "R-0001";
    
    static {
        svc = ServiceUtil.create(RPTReceiptService.class);
        receiptSvc = ServiceUtil.create(CashReceiptService.class);
    }
    
    private RPTReceiptData(){}
    
    public static Map createReceipt(Map ledger){
        Map pdate = Data.parseCurrentDate();
        Map taxpayer = (Map) ledger.get("taxpayer");
        
        Map receipt = new HashMap();
        receipt.put("objid", objid);
        receipt.put("state", "POSTED");
        receipt.put("txndate", pdate.get("date"));
        receipt.put("receiptdate", pdate.get("date"));
        receipt.put("txnmode", "ONLINE");
        receipt.put("payer_objid", taxpayer.get("objid"));
        receipt.put("payer_name", taxpayer.get("name"));
        receipt.put("paidby", taxpayer.get("name"));
        receipt.put("paidbyaddress", "ADDRESS");
        receipt.put("collector_objid", "");
        receipt.put("collector_name", "");
        receipt.put("collector_title", "");
        receipt.put("amount", 0);
        receipt.put("totalcash", 0);
        receipt.put("totalnoncash", 0);
        receipt.put("cashchange", 0);
        receipt.put("totalcredit", 0);
        receipt.put("org_objid", null);
        receipt.put("org_name", null);
        receipt.put("formno", "56");
        receipt.put("formtype", "serial");
        
        Map colltype = Data.getCollectionType();
        receipt.put("collectiontype", colltype);
                
        receipt.put("receiptno", "");
        receipt.put("series", "");
        receipt.put("stub", "");
        return receipt;
    }    
    
    
    public static void cleanup(Map entity){
        deleteLedgerItems(entity);
        deleteLedgerFaases(entity);
        deleteLedger(entity);
    }
    
    public static void deleteLedgerItems(Map entity) {
        Map findby = new HashMap();
        findby.put("parentid", entity.get("objid"));
        
        Map m = new HashMap();
        m.put("_schemaname", "rptledger_item");
        m.put("findBy", findby);
        ServiceUtil.persistence().removeEntity(m);
    }
    
    public static void deleteLedgerFaases(Map entity) {
        Map findby = new HashMap();
        findby.put("rptledgerid", entity.get("objid"));
        
        Map m = new HashMap();
        m.put("_schemaname", "rptledger_faas");
        m.put("findBy", findby);
        ServiceUtil.persistence().removeEntity(m);
    }
    
    public static void deleteLedger(Map entity) {
        Map findby = new HashMap();
        findby.put("objid", entity.get("objid"));
        
        Map m = new HashMap();
        m.put("_schemaname", "rptledger");
        m.put("findBy", findby);
        ServiceUtil.persistence().removeEntity(m);
    }
    
}
