/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.utils;

import com.rameses.gov.etracs.rptis.models.Entity;
import com.rameses.gov.etracs.services.DateService;
import com.rameses.gov.etracs.services.RPTUtilityService;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

    private static DateService dtSvc;
    private static RPTUtilityService utilSvc;
    private static RPTUtilityService remoteUtilSvc;

    static {
        dtSvc = ServiceUtil.create(DateService.class);
        utilSvc = ServiceUtil.create(RPTUtilityService.class);
        remoteUtilSvc = ServiceUtil.create(RPTUtilityService.class, true);
    }

    private Data() {
    }

    public static Map getMunicipality(Map brgy) {
        Map m = new HashMap();
        m.put("_schemaname", "sys_org");
        m.put("where", new Object[]{"objid = :parentid", brgy});
        return ServiceUtil.query().findFirst(m);
    }

    public static Map getLgu() {
        return getLgu(new HashMap());
    }

    public static Map getLgu(Object lguid) {
        Map m = new HashMap();
        m.put("_schemaname", "sys_org");
        m.put("lguid", lguid);
        m.put("where", new Object[]{"objid = :lguid", m});
        Map lgu = ServiceUtil.query().findFirst(m);
        lgu.put("lgutype", lgu.get("orgclass"));
        return lgu;
    }

    public static Map getLgu(Map brgy) {
        Map m = new HashMap();
        m.put("_schemaname", "sys_org");
        m.put("where", new Object[]{"root=1"});
        Map lgu = ServiceUtil.query().findFirst(m);
        if (lgu == null) {
            throw new RuntimeException("root lgu is not set.");
        }
        if (brgy != null && !brgy.isEmpty() && "PROVINCE".equalsIgnoreCase(lgu.get("orgclass").toString())) {
            lgu = getMunicipality(brgy);
        }
        lgu.put("lgutype", lgu.get("orgclass"));
        return lgu;
    }

    public static Map getCollectionType() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Map getTxnType(String txntype) {
        Map param = new HashMap();
        param.put("objid", txntype);

        Map m = new HashMap();
        m.put("_schemaname", "faas_txntype");
        m.put("where", new Object[]{"objid = :objid", param});
        return ServiceUtil.query().findFirst(m);
    }

    public static Map getAdminUser() {
        Map m = new HashMap();
        m.put("_schemaname", "user");
        m.put("where", new Object[]{"username='ADMIN'"});
        return ServiceUtil.query().findFirst(m);
    }

    public static Map getBarangay() {
        Map m = new HashMap();
        m.put("_schemaname", "barangay");
        m.put("where", new Object[]{"1=1"});
        return ServiceUtil.query().findFirst(m);
    }

    public static Map getEntity() {
        return Entity.findEntity(Entity.ENTITY_001);
    }

    public static Integer getCurrentYear() {
        return (Integer) ServiceUtil.getDateService().parseCurrentDate().get("year");
    }

    public static Object getServerDate() {
        return ServiceUtil.getDateService().getServerDate();
    }

    public static Map parseCurrentDate() {
        return (Map) ServiceUtil.getDateService().parseCurrentDate();
    }

    public static Map parseDate(Object dt) {
        return ServiceUtil.getDateService().parseDate(dt, null);
    }

    public static Map getClassification() {
        Map m = new HashMap();
        m.put("_schemaname", "propertyclassification");
        m.put("name", "RESIDENTIAL");
        m.put("where", new Object[]{"name = :name", m});
        return ServiceUtil.query().findFirst(m);
    }

    public static Map toMapObjid(Map entity) {
        Map m = new HashMap();
        m.put("objid", entity.get("objid"));
        return m;

    }

    public static Map getLedgerByFaas(Map faas) {
        Map param = new HashMap();
        param.put("_schemaname", "rptledger");
        param.put("where", new Object[]{"faasid = :objid", Data.toMapObjid(faas)});
        return ServiceUtil.query().findFirst(param);
    }

    public static BigDecimal toDecimal(Object val) {
        return toDecimal(val, 2);
    }

    public static BigDecimal toDecimal(Object val, int scale) {
        return new BigDecimal(val.toString()).setScale(2);
    }

    public static boolean toBool(Object val) {
        if (val instanceof Boolean) {
            return (Boolean) val;
        }
        String s = val.toString().toLowerCase();
        return s.matches("1|t|true|y|yes");
    }
    
    public static int toInt(Object val) {
        return toInt(val, 0);
    }
    
    public static int toInt(Object val, int defVal) {
        if (val instanceof Integer){
            return (Integer)val;
        }
        try {
            return Integer.parseInt(val.toString());
        }catch (Exception ex) {
            return defVal;
        }
    }


    public static String format(Object val) {
        if (val instanceof Date) {
            return format(val, "yyyy-MM-dd");
        } else if (val instanceof Number) {
            return format(val, "#,##0.00");
        } else {
            return val.toString();
        }
    }

    public static String format(Object val, String pattern) {
        if (val instanceof Date) {
            return dtSvc.format(pattern, val);
        } else if (val instanceof Number) {
            DecimalFormat df = new DecimalFormat(pattern);
            return df.format(val);
        } else {
            return val.toString();
        }
    }

    public static void cleanUp() {
        cleanUp(false);
    }

    public static void cleanUp(boolean remote) {
        delete("rpt_redflag", remote);
        delete("rpt_requirement", remote);
        delete("faas_restriction", remote);
        delete("faasannotation", remote);
        delete("rpt_changeinfo", remote);
        delete("examiner_finding", remote);
        delete("rptbill_ledger", remote);
        delete("rptbill", remote);
        delete("rptcompromise_credit", remote);
        delete("rptcompromise_installment", remote);
        delete("rptcompromise_item", remote);
        delete("rptcompromise", remote);
        delete("rptpayment_share", remote);
        delete("rptpayment_item", remote);
        delete("rptpayment", remote);
        delete("rptledger_avdifference", remote);
        delete("rptledgerfaas", remote);
        delete("rptledger_item", remote);
        delete("rptledger_credit", remote);
        delete("rptledger_subledger", remote);
        delete("rptledger", remote);
        delete("sys_sequence", remote);
        delete("txnref", remote);
        delete("workflowstate", remote);
        delete("assessmentnoticeitem", remote);
        delete("assessmentnotice", remote);
        delete("faas_signatory", remote);
        delete("faasbacktax", remote);
        delete("faas_task", remote);
        delete("cancelannotation", remote);
        delete("faas_previous", remote);
        delete("rpttaxclearance", remote);
        delete("rptcertificationitem", remote);
        delete("rptcertification", remote);
        delete("consolidatedland", remote);
        delete("consolidationaffectedrpu", remote);
        delete("consolidation_task", remote);
        delete("consolidation", remote);
        delete("subdivisionaffectedrpu", remote);
        delete("subdividedland", remote);
        delete("subdivision_task", remote);
        delete("subdivision_motherland", remote);
        delete("subdivision", remote);
        delete("previousfaas", remote);
        delete("faas_previous", remote);
        delete("faas_sketch", remote);
        delete("cancelledfaas_signatory", remote);
        delete("cancelledfaas_task", remote);
        delete("cancelledfaas", remote);
        delete("bldgrpu_land", remote);
        delete("faas", remote);
        delete("faas_list", remote);
        delete("machdetail", remote);
        delete("machuse", remote);
        delete("machrpu", remote);
        delete("bldgflooradditionalparam", remote);
        delete("bldgflooradditional", remote);
        delete("bldgfloor", remote);
        delete("bldguse", remote);
        delete("bldgrpu_structuraltype", remote);
        delete("bldgstructure", remote);
        delete("bldgrpu", remote);
        delete("planttreedetail", remote);
        delete("planttreerpu", remote);
        delete("landadjustmentparameter", remote);
        delete("landadjustment", remote);
        delete("landdetail", remote);
        delete("landrpu", remote);
        delete("rpu_assessment", remote);
        delete("rpu", remote);
        delete("rpumaster", remote);
        delete("realproperty", remote);
        delete("txnlog", remote);

//        delete("cashreceiptitem", remote);
//        delete("cashreceiptpayment_creditmemo", remote);
//        delete("cashreceiptpayment_noncash", remote);
//        delete("cashreceipt_largecattleownership", remote);
//        delete("cashreceipt_largecattletransfer", remote);
//        delete("cashreceipt_slaughter", remote);
//        delete("cashreceipt_marriage", remote);
//        delete("cashreceipt_void", remote);
//        delete("cashreceipt_rpt", remote);
//        delete("cashreceipt", remote);
//        delete("entitymultiple", remote);
//        delete("entity_address", remote);
//        delete("entity_relation", remote);
//        delete("entitycontact", remote);
//        delete("entityid", remote);
//        delete("entityindividual", remote);
//        delete("entityjuridical", remote);
//        delete("entitymember", remote);
//        delete("entity", remote);
    }

    public static void delete(String tablename, boolean remote) {
        try {
            Map param = new HashMap();
            param.put("tablename", tablename);
            if (remote) {
                remoteUtilSvc.deleteTableData(param);
            } else {
                utilSvc.deleteTableData(param);
            }
        } catch (Exception ex) {
            System.out.println("delete [ERROR] " + ex.getMessage());
        }
    }

    public static Map getTxnRef(Object refid) {
        return getTxnRef(refid, false);
    }

    public static Map getTxnRef(Object refid, boolean remote) {
        Map m = new HashMap();
        m.put("_schemaname", "txnref");
        m.put("refid", refid);
        m.put("where", new Object[]{"refid = :refid", m});
        return ServiceUtil.query(remote).findFirst(m);
    }

    public static int getRevisionYear() {
        return getRevisionYear(false);
    }

    public static int getRevisionYear(boolean remote) {
        Map m = new HashMap();
        m.put("_schemaname", "rysetting_land");
        m.put("orderBy", "ry");
        m.put("where", new Object[]{"1=1"});
        Map setting = ServiceUtil.query(remote).findFirst(m);
        if (setting == null) {
            throw new RuntimeException("Land Revision Setting is not defined.");
        }
        return (Integer) setting.get("ry");
    }

    public static Map getLog(String docType, Map entity, String action) {
        return getLog(docType, entity.get("objid"), action, false);
    }

    public static Map getLog(String docType, Map entity, String action, boolean remote) {
        return getLog(docType, entity.get("objid"), action, remote);
    }

    public static Map getLog(String docType, Object refid, String action) {
        return getLog(docType, refid, action, false);
    }

    public static Map getLog(String docType, Object refid, String action, boolean remote) {
        Map param = new HashMap();
        param.put("_schemaname", "txnlog");
        param.put("refid", refid);
        param.put("ref", docType);
        param.put("action", action);
        param.put("where", new Object[]{"ref = :ref and refid = :refid and action = :action", param});
        return ServiceUtil.query(remote).findFirst(param);
    }

    public static Map getRootOrg() {
        Map param = new HashMap();
        param.put("_schemaname", "sys_org");
        param.put("root", 1);
        param.put("where", new Object[]{"root = :root", param});
        return ServiceUtil.query().findFirst(param);
    }

    public static Map getCancelReason() {
        Map param = new HashMap();
        param.put("_schemaname", "cancelfaasreason");
        param.put("where", new Object[]{"1=1", param});
        return ServiceUtil.query().findFirst(param);
    }

    public static Map getTask(String schemaName, Map entity, String state) {
        return getTask(schemaName, entity, state, false);
    }

    public static Map getTask(String schemaName, Map entity, String state, boolean remote) {
        Map param = new HashMap();
        param.put("_schemaname", schemaName);
        param.put("refid", entity.get("objid"));
        param.put("state", state);
        param.put("where", new Object[]{"refid = :refid and state = :state", param});
        param.put("orderBy", "startdate desc");
        return ServiceUtil.query(remote).findFirst(param);
    }

    public static List<Map> getTasks(String schemaName, Map entity) {
        return getTasks(schemaName, entity, false);
    }

    public static List<Map> getTasks(String schemaName, Map entity, boolean remote) {
        Map param = new HashMap();
        param.put("_schemaname", schemaName);
        param.put("refid", entity.get("objid"));
        param.put("where", new Object[]{"refid = :refid", param});
        return ServiceUtil.query(remote).getList(param);
    }

    public static List<Map> getOpenTasks(String schemaName, Map entity) {
        return getOpenTasks(schemaName, entity, false);
    }

    public static List<Map> getOpenTasks(String schemaName, Map entity, boolean remote) {
        Map param = new HashMap();
        param.put("_schemaname", schemaName);
        param.put("refid", entity.get("objid"));
        param.put("where", new Object[]{"refid = :refid and enddate is null", param});
        return ServiceUtil.query(remote).getList(param);
    }

    public static List<Map> getExaminations(Map parent) {
        return getExaminations(parent, false);
    }

    public static List<Map> getExaminations(Map parent, boolean remote) {
        Map param = new HashMap();
        param.put("_schemaname", "rpt_examination");
        param.put("parentid", parent.get("objid"));
        param.put("where", new Object[]{"parent_objid = :parentid", param});
        return ServiceUtil.query(true).getList(param);
    }

    public static Map findEntity(String schemaName, Map entity) {
        return findEntity(schemaName, entity, false);
    }
    
    public static Map findEntity(String schemaName, Map entity, boolean wait) {
        Map param = new HashMap();
        param.put("_schemaname", schemaName);
        param.put("where", new Object[]{"objid = :objid", toMapObjid(entity)});
        Map remoteData = ServiceUtil.query().findFirst(param);

        if (wait) {
            System.out.print("Waiting for remote " + schemaName + " data ... ");
            while (remoteData == null) {
                Util.sleep(1000);
                remoteData = ServiceUtil.query().findFirst(param);
            }
            System.out.println("DONE");
        }
        return remoteData;
    }

    public static Map findRemoteEntity(String schemaName, Map entity) {
        return findRemoteEntity(schemaName, entity, false);
    }

    public static Map findRemoteEntity(String schemaName, Map entity, boolean wait) {
        waitSyncCompleted(entity);

        Map param = new HashMap();
        param.put("_schemaname", schemaName);
        param.put("where", new Object[]{"objid = :objid", toMapObjid(entity)});
        Map remoteData = ServiceUtil.query(true).findFirst(param);

        if (wait) {
            System.out.print("Waiting for remote " + schemaName + " data ... ");
            while (remoteData == null) {
                Util.sleep(1000);
                remoteData = ServiceUtil.query(true).findFirst(param);
            }
            System.out.println("DONE");
        }
        return remoteData;
    }

    public static Map waitRemoteEntityDeleted(String schemaName, Map entity) {
        waitSyncCompleted(entity);

        Map param = new HashMap();
        param.put("_schemaname", schemaName);
        param.put("where", new Object[]{"objid = :objid", toMapObjid(entity)});
        Map remoteData = ServiceUtil.query(true).findFirst(param);

        System.out.print("Waiting for remote " + schemaName + " data ... ");
        while (remoteData != null) {
            Util.sleep(1000);
            remoteData = ServiceUtil.query(true).findFirst(param);
        }
        System.out.println("DONE");
        return remoteData;
    }

    public static Map getRestrictionType(String objid) {
        Map param = new HashMap();
        param.put("_schemaname", "faas_restriction_type");
        param.put("objid", objid);
        param.put("where", new Object[]{"objid = :objid", param});
        return ServiceUtil.query().findFirst(param);
    }

    public static List<Map> getList(String schemaName) {
        return getList(schemaName, false);
    }
    
    public static List<Map> getList(String schemaName, boolean remote) {
        Map param = new HashMap();
        param.put("_schemaname", schemaName);
        param.put("where", new Object[]{"1=1"});
        return ServiceUtil.query(true).getList(param);
    }

    public static void waitSyncCompleted(Map entity) {
        waitSyncCompleted(entity, 2000);
    }

    public static void waitSyncCompleted(Map entity, int millis) {
        Map param = new HashMap();
        param.put("_schemaname", "sync_data");
        String where = "parentid = :objid and forprocess.objid is null and pending.objid is null";
        param.put("where", new Object[]{where, toMapObjid(entity)});

        System.out.print("Sending remote data... ");
        Map syncdata = ServiceUtil.query(true).findFirst(param);
        while (syncdata != null) {
            Util.sleep(millis);
            syncdata = ServiceUtil.query(true).findFirst(param);
        }
        System.out.println("COMPLETED");

        //data pushed, give remote extra time to process the data 
        Util.sleep(1000);
    }

    public static Map findLedgerByFaas(Map faas) {
        return findLedgerByFaas(faas, false);
    }

    public static Map findLedgerByFaas(Map faas, boolean remote) {
        Map param = new HashMap();
        param.put("_schemaname", "rptledger");
        param.put("where", new Object[]{"faasid = :objid", toMapObjid(faas)});
        return ServiceUtil.query(remote).findFirst(param);
    }
}
