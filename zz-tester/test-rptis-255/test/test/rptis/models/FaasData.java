/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.rptis.models;

import com.rameses.gov.etracs.landtax.RPTLedgerData;
import com.rameses.gov.etracs.rptis.models.Entity;
import test.rptis.services.FAASService;
import test.utils.Data;
import test.utils.ServiceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FaasData {

    public int ry = 2014;
    public int effectivityYear = ry + 1;
    public int effectivityQtr = 1;
    
    public static String INTERIM = "INTERIM";
    public static String PENDING = "PENDING";
    public static String CURRENT = "CURRENT";
    public static String CANCELLED = "CANCELLED";
    
    public void build(Map faas){
        buildRealProperty(faas);
        buildRpu(faas);
        
        Map pdate = Data.parseCurrentDate();
        Map lgu = Data.getLgu(faas.get("lguid"));
        Map rp  = (Map)faas.get("rp");
        Map rpu = (Map)faas.get("rpu");
        Map owner = Entity.findEntity(Entity.ENTITY_001);
        Map administrator = new HashMap();
        administrator.put("name", "ADMINISTRATOR");
        administrator.put("address", "ADMINISTRATOR ADDRESS");
        
        int hashCode = this.hashCode();
        
        faas.put("tdno", hashCode);
        faas.put("utdno", hashCode );
        faas.put("effectivityyear", effectivityYear);
        faas.put("effectivityqtr", effectivityQtr);
        faas.put("rpuid", rpu.get("objid"));
        faas.put("realpropertyid", rp.get("objid"));
        faas.put("fullpin", "PIN-" + hashCode);
        faas.put("taxpayer", owner);
        faas.put("owner", owner);
        faas.put("administrator", administrator);
        faas.put("memoranda", "DATA CAPTURE");
        faas.put("backtaxyrs", 0);
        faas.put("prevtdno", "PREV-" + hashCode);
        faas.put("lguid", lgu.get("objid"));
        faas.put("lgutype", lgu.get("orgclass"));
        faas.put("name", owner.get("name"));
        
        buildFaasSignatories(faas);
        buildAssessments(faas, rpu);
        buildPreviousInfo(faas, rpu);
        
    }

    public Map buildRealProperty() {
        Map faas = new HashMap();
        faas.put("rp", new HashMap());
        Map rp = buildRealProperty(faas);
        rp.put("objid", "RP" + new java.rmi.server.UID());
        rp.put("state", "INTERIM");
        rp.put("autonumber", false);
        rp.put("pintype", "new");
        return rp;
    }
    
    public Map buildRealProperty(Map faas) {
        Map rp = (Map) faas.get("rp");
        rp.put("claimno", null);
        rp.put("purok", null);
        rp.put("surveyno", "PSD-001");
        rp.put("cadastrallotno", "01");
        rp.put("blockno", "01");
        rp.put("street", null);
        rp.put("north", "N-01");
        rp.put("east", "E-01");
        rp.put("south", "S-01");
        rp.put("west", "W-01");
        rp.put("previd", null);
        rp.put("stewardshipno", null);
        return rp;
    }

    private static void buildRpu(Map faas) {
        Map rpu = (Map) faas.get("rpu");
        rpu.put("classification", Data.getClassification());
        rpu.put("exemptiontype", null);
        rpu.put("taxable", true);
        rpu.put("totalareaha", 0.0100);
        rpu.put("totalareasqm", 100);
        rpu.put("totalbmv", 100000);
        rpu.put("totalmv", 100000);
        rpu.put("totalav", 10000);
        rpu.put("hasswornamount", false);
        rpu.put("swornamount", 0.0);
        rpu.put("useswornamount", false);
        rpu.put("previd", null);
        rpu.put("reclassed", false);
    }


    public static void buildFaasSignatories(Map faas) {
        Map pdate = Data.parseCurrentDate();
        Map user = new HashMap();
        user.put("name", "NAME");
        user.put("title", "TITLE");
        user.put("dtsigned", pdate.get("date"));
        
        faas.put("taxmapper", user);
        faas.put("appraiser", user);
        faas.put("recommender", user);
        faas.put("approver", user);
    }
    
    public static void buildAssessments(Map faas, Map rpu) {
        List<Map> items = new ArrayList<Map>();
        
        Map assessment = new HashMap();
        assessment.put("objid", rpu.get("objid"));
        assessment.put("rpuid", rpu.get("objid"));
        assessment.put("rputype", rpu.get("rputype"));
        assessment.put("classification", rpu.get("classification"));
        assessment.put("actualuse", rpu.get("classification"));
        assessment.put("areasqm", rpu.get("totalareasqm"));
        assessment.put("areaha", rpu.get("totalareaha"));
        assessment.put("marketvalue", rpu.get("totalmv"));
        assessment.put("assesslevel", 10.0);
        assessment.put("assessedvalue", rpu.get("totalav"));
        assessment.put("taxable", true);
        items.add(assessment);
        
        rpu.put("assessments", items);
    }    

    public static void buildPreviousInfo(Map faas, Map rpu) {
        List<Map> items = new ArrayList<Map>();
        
        Map prev = new HashMap();
        prev.put("objid", faas.get("objid"));
        prev.put("faasid", faas.get("objid") );
        prev.put("prevfaasid", null);
        prev.put("prevrpuid", null);
        prev.put("prevtdno", "NEW");
        prev.put("prevpin", faas.get("fullpin"));
        prev.put("prevowner", "SAME");
        prev.put("prevadministrator", null );
        prev.put("prevav", rpu.get("totalav"));
        prev.put("prevmv", rpu.get("totalmv"));
        prev.put("prevareasqm", rpu.get("totalareasqm"));
        prev.put("prevareaha", rpu.get("totalareaha"));
        prev.put("preveffectivity", faas.get("effectivityyear"));
        
        items.add(prev);
        faas.put("previousfaases", items);
    }
    
    public static void cleanUp(Map faas) {
        cleanUpLedger(faas);
        
        FAASService svc = ServiceUtil.create(FAASService.class);
        if ("CURRENT".equalsIgnoreCase(faas.get("state").toString())){
            //reset state to interim to be deleted
            Map param = new HashMap();
            param.put("_schemaname", "faas");
            param.put("objid", faas.get("objid"));
            param.put("state", INTERIM);
            ServiceUtil.persistence().update(param);
            param.put("_schemaname", "faas_list");
            ServiceUtil.persistence().update(param);
        }
        svc.deleteFaas(faas);
    }
    
    private static void cleanUpLedger(Map faas) {
        Map ledger = getLedger(faas);
        if (ledger != null){
            RPTLedgerData.cleanup(ledger);
        }
    }

    

    private static Map getLedger(Map faas) {
        Map params = new HashMap();
        params.put("_schemaname", "rptledger");
        params.put("faasid", faas.get("objid"));
        params.put("where", new Object[]{"faasid = :faasid", params});
        return ServiceUtil.query().findFirst(params);
    }
    
    
}
