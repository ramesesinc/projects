package com.rameses.gov.etracs.rptis.models;

import test.rptis.models.FaasData;
import test.rptis.models.FaasModel;
import com.rameses.gov.etracs.services.RPTRequirementService;
import com.rameses.gov.etracs.services.RPTRequirementTypeService;
import com.rameses.gov.etracs.services.SubdivisionService;
import com.rameses.gov.etracs.services.SubdivisionWorkflowService;
import test.utils.Data;
import test.utils.ServiceUtil;
import test.utils.Util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubdivisionModel {

    private SubdivisionService svc;
    private SubdivisionService remoteSvc;
    private SubdivisionWorkflowService workflowSvc;
    private SubdivisionWorkflowService remoteWorkflowSvc;
    private RPTRequirementService reqSvc;
    private RPTRequirementTypeService reqTypeSvc;
    private FaasModel faasModel;
    private LedgerModel ledgerModel;
    private FaasData faasData;
    private Map currentTask;

    public SubdivisionModel() {
        svc = ServiceUtil.create(SubdivisionService.class);
        remoteSvc = ServiceUtil.create(SubdivisionService.class, true);
        workflowSvc = ServiceUtil.create(SubdivisionWorkflowService.class);
        remoteWorkflowSvc = ServiceUtil.create(SubdivisionWorkflowService.class, true);

        reqSvc = ServiceUtil.create(RPTRequirementService.class);
        reqTypeSvc = ServiceUtil.create(RPTRequirementTypeService.class);

        faasModel = new FaasModel();
        faasData = new FaasData();
        ledgerModel = new LedgerModel();
    }

    public Map open(Map subdivision) {
        return open(subdivision, false);
    }

    public Map open(Map subdivision, boolean remote) {
        if (remote) {
            return remoteSvc.open(subdivision);
        }
        return svc.open(subdivision);
    }

    public Map receive(Map motherLand) {
        Util.sleep(1000);
        ledgerModel.simulatePaidLedger(motherLand);

        Map subdivision = new HashMap();
        subdivision.put("objid", "TSD" + new java.rmi.server.UID());
        subdivision.put("filetype", "subdivision");
        subdivision.put("memoranda", "TEST MEMORANDA");
        subdivision = svc.create(subdivision);
        currentTask = (Map) subdivision.get("task");
        return svc.open(subdivision);
    }

    public Map taxmap(Map subdivision, Map motherLand) {
        //submit and assign to taxmapper
        signal("submit-taxmapper", subdivision);
        signal(null, subdivision);

        addMotherLand(subdivision, motherLand);
        createSubdividedLands(subdivision, motherLand);
        addExamination(subdivision);
        addRequirement(subdivision);
        return svc.open(subdivision);
    }

    public Map appraise(Map subdivision) {
        //submit and assign to appraiser
        signal("submit", subdivision);
        signal(null, subdivision);

        return svc.open(subdivision);
    }

    public Map recommend(Map subdivision) {
        //submit and assign to recommender
        signal("submit", subdivision);
        signal(null, subdivision);

        subdivision = svc.open(subdivision);

        //submit to province
        signal("submit", subdivision);
        simulateSubmitToProvinceTask(subdivision);

        waitForRemoteData(subdivision);
        return open(subdivision);
    }

    public Map remoteApproval(Map subdivision) {
        System.out.println("====================================");
        System.out.println("Performing remote approval... ");
        System.out.println("====================================");

        Util.sleep(1000);

        Map remote = Data.findRemoteEntity("subdivision", subdivision, true);
        currentTask = Data.getTask("subdivision_task", remote, "assign-receiver", true);


        System.out.println("Receiving...");
        signal(null, remote, true);
        signal("submit_taxmapper", remote, true);

        System.out.println("Taxmapping...");
        signal(null, remote, true);
        signal("submit", remote, true);

        System.out.println("Appraisal...");
        signal(null, remote, true);
        signal("submit", remote, true);

        System.out.println("Recommending...");
        signal(null, remote, true);
        signal("submit_approver", remote, true);

        System.out.println("Approval...");
        signal(null, remote, true);
        signal("approve", remote, true);

        System.out.println("==================================");

        //simulate approval task
        executeApprovalTask(subdivision);

        signal("completed", remote, true);
        waitUntilSudivisionIsApproved(subdivision);
        return open(remote, true);
    }

    private void executeApprovalTask(Map subdivision) {
        System.out.print("Approving subdivision... ");
        remoteSvc.initApproveSubdivision(subdivision);
        remoteSvc.assignNewTdNos(subdivision);
        
        List<Map> lands = remoteSvc.getSubdividedLandsForApproval(subdivision.get("objid"));
        for (Map land : lands) {
            remoteSvc.approveSubdividedLandFaasRecord(subdivision, land);
        }
        
        List<Map> rpus = remoteSvc.getAffectedRpusForApproval(subdivision.get("objid"));
        for (Map arpu : rpus) {
            remoteSvc.approveAffectedRpuFaasRecord(subdivision, arpu);
        }
        
        List<Map> cancellations = remoteSvc.getCancelledImprovements(subdivision.get("objid"));
        for (Map ci : cancellations) {
            remoteSvc.approveCancelledImprovement(subdivision, ci);
        }
        subdivision.putAll(remoteSvc.approve(subdivision));
        System.out.println("DONE");
    }

    private void addMotherLand(Map subdivision, Map motherLand) {
        Map land = new HashMap();
        land.put("objid", "SML" + new java.rmi.server.UID());
        land.put("subdivisionid", subdivision.get("objid"));
        land.put("landfaasid", motherLand.get("objid"));
        land.put("rpuid", motherLand.get("rpuid"));
        land.put("rpid", motherLand.get("realpropertyid"));
        land.put("lguid", motherLand.get("lguid"));
        svc.addMotherLand(land);
    }

    private void createSubdividedLands(Map subdivision, Map motherLand) {
        Map rp1 = createRealProperty(motherLand, "999", "02");
        Map sland1 = new HashMap();
        sland1.put("objid", "SL" + new java.rmi.server.UID());
        sland1.put("subdivisionid", subdivision.get("objid"));
        sland1.put("newpin", rp1.get("pin"));
        sland1.put("rp", rp1);
        sland1 = svc.createSubdividedLand(sland1, subdivision);
        assessLandFaas(sland1);

        Map rp2 = createRealProperty(motherLand, "999", "03");
        Map sland2 = new HashMap();
        sland2.put("objid", "SL" + new java.rmi.server.UID());
        sland2.put("subdivisionid", subdivision.get("objid"));
        sland2.put("newpin", rp2.get("pin"));
        sland2.put("rp", rp2);
        sland2 = svc.createSubdividedLand(sland2, subdivision);
        assessLandFaas(sland2);
    }

    private Map createRealProperty(Map motherLand, String section, String parcel) {
        Map motherRp = (Map) motherLand.get("rp");

        Map rp = new HashMap(motherRp);
        rp.put("objid", "RP" + new java.rmi.server.UID());
        rp.put("state", "INTERIM");
        rp.put("pin", motherRp.get("pin").toString().replace("999-01", section + "-" + parcel));
        rp.put("isection", section);
        rp.put("iparcel", parcel);
        rp.put("section", section);
        rp.put("parcel", parcel);
        Map brgy = (Map) rp.get("barangay");
        brgy.put("lgu", Data.getLgu(motherRp.get("lguid")));
        return rp;
    }

    private void waitForRemoteData(Map subdivision) {
        Data.findRemoteEntity("subdivision", subdivision, true);
        Util.sleep(1000);
    }

    public void signal(String action, Map subdivision) {
        signal(action, subdivision, false);
    }

    public void signal(String action, Map subdivision, boolean remote) {
        Map info = new HashMap();
        info.put("taskid", currentTask.get("objid"));
        info.put("refid", currentTask.get("refid"));
        info.put("state", currentTask.get("state"));
        info.put("action", action);
        info.put("data", subdivision);

        Map result = null;
        if (remote) {
            result = remoteWorkflowSvc.signal(info);
        } else {
            result = workflowSvc.signal(info);
        }

        currentTask = (Map) result.remove("task");
    }

    private void simulateSubmitToProvinceTask(Map subdivision) {
        Util.sleep(1000);
        
        System.out.print("Executing submit to province task... ");
        svc.initApproveSubdivision(subdivision);
        svc.assignNewTdNos(subdivision);
        subdivision.putAll(svc.submitToProvince(subdivision));
        System.out.println("DONE");
        
        //forprovsubmission state (completed)
        signal("completed", subdivision);
        
    }

    private void assessLandFaas(Map sland) {
        Map faas = new HashMap();
        faas.put("objid", sland.get("newfaasid"));
        faas = faasModel.open(faas);
        FaasLand.assess(faas);
        faasModel.update(faas);

    }

    private void addExamination(Map subdivision) {
        Map exam = new HashMap();
        exam.put("_schemaname", "rpt_examination");
        exam.put("objid", "F" + new java.rmi.server.UID());
        exam.put("parent_objid", subdivision.get("objid"));
        exam.put("dtinspected", Data.getServerDate());
        exam.put("findings", "FINDINGS");
        exam.put("recommendations", "RECOMMENDATIONS");
        exam.put("notedby", "NOTEDBY");
        exam.put("notedbytitle", "NOTEDBY TITLE");
        exam.put("inspectors", "-");
        ServiceUtil.persistence().create(exam);
    }

    private void addRequirement(Map subdivision) {
        Map reqType = reqTypeSvc.getList(new HashMap()).get(0);
        Map req = new HashMap();
        req.put("objid", "RQ" + new java.rmi.server.UID());
        req.put("requirementtypeid", reqType.get("objid"));
        req.put("handler", reqType.get("handler"));
        req.put("refid", subdivision.get("objid"));
        req.put("value_objid", null);
        req.put("value_txnno", null);
        req.put("value_txndate", null);
        req.put("value_txnamount", null);
        req.put("value_remarks", null);
        req.put("complied", 1);
        reqSvc.create(req);
    }

    private void waitUntilSudivisionIsApproved(Map subdivision) {
        System.out.print("Waiting for local subdivision approval... ");
        Map approveSubdivision = Data.findEntity("subdivision", subdivision);
        while(!"APPROVED".equalsIgnoreCase(approveSubdivision.get("state")+"")) {
            Util.sleep(1000);
            approveSubdivision = Data.findEntity("subdivision", subdivision);
        }
        System.out.println("DONE");
    }
}
