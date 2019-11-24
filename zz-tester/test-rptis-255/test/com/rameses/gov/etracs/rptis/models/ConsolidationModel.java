package com.rameses.gov.etracs.rptis.models;

import test.rptis.models.FaasData;
import test.rptis.models.FaasModel;
import com.rameses.gov.etracs.services.ConsolidationService;
import com.rameses.gov.etracs.services.ConsolidationWorkflowService;
import test.utils.Data;
import test.utils.Lookup;
import test.utils.ServiceUtil;
import test.utils.Util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsolidationModel extends WorkflowModel {
    private ConsolidationService svc;
    private ConsolidationService remoteSvc;
    
    
    private FaasModel faasModel;
    private LedgerModel ledgerModel;
    private FaasData faasData;
    
    public Map landFaas1;
    public Map landFaas2;

    public ConsolidationModel() {
        svc = ServiceUtil.create(ConsolidationService.class);
        remoteSvc = ServiceUtil.create(ConsolidationService.class, true);
        faasModel = new FaasModel();
        faasData = new FaasData();
        ledgerModel = new LedgerModel();
    }

    @Override
    public void initWorkflowModels() {
        workflowSvc = ServiceUtil.create(ConsolidationWorkflowService.class);
        remoteWorkflowSvc = ServiceUtil.create(ConsolidationWorkflowService.class, true);
    }
    
    

    public Map create(Map consolidation) {
        return svc.create(consolidation);
    }
    
    public Map open(Map consolidation) {
        return open(consolidation, false);
    }

    public Map open(Map consolidation, boolean remote) {
        if (remote) {
            return remoteSvc.open(consolidation);
        }
        return svc.open(consolidation);
    }
    
    public void createLandsToConsolidate() {
        Map info1 = faasModel.createInitialInfo("901", "01");
        landFaas1 = faasModel.createAndApprove(info1);
        
        Map info2 = faasModel.createInitialInfo("901", "02");
        landFaas2 = faasModel.createAndApprove(info2);
        
        // wait for remote data to sync
        Data.findRemoteEntity("faas", landFaas2, true);
    }

    
    public Map receive() {
        Map rp = (Map) landFaas1.get("rp");
        Map rpu = (Map) landFaas1.get("rpu");
        Map brgy = (Map) rp.get("barangay");
        
        String section = "901";
        String parcel = "03";
        String pin = rp.get("pin").toString().replace("901-01", "901-03");
        
        Map info = new HashMap();
        info.put("txntype", Data.getTxnType("CS"));
        info.put("ry", rpu.get("ry"));
        info.put("pintype", "new");
        info.put("rputype", "land");
        info.put("suffix", 0);
        info.put("lgu", Data.getLgu(brgy));
        info.put("barangay", brgy);
        info.put("pin", pin);
        info.put("section", section);
        info.put("isection", Data.toInt(section));
        info.put("parcel", parcel);
        info.put("iparcel", Data.toInt(parcel));
        
        
        Map consolidation = svc.init();
        consolidation.put("_info", info);
        consolidation.put("memoranda", "TEST CS");
        consolidation = create(consolidation);
        currentTask = (Map) consolidation.get("task");
        return consolidation;
    }

    public Map taxmap(Map consolidation) {
        //submit and assign to taxmapper
        signal("submit-taxmapper", consolidation);
        signal(null, consolidation);
        
        addConsolidatedLands(consolidation);
        assessConsolidatedFaas(consolidation);
        return open(consolidation);
    }
    
    public Map appraise(Map consolidation) {
        //submit and assign to appraiser
        signal("submit", consolidation);
        signal(null, consolidation);
        return open(consolidation);
    }
    
    public Map recommend(Map consolidation) {
        //submit and assign to recommender
        signal("submit", consolidation);
        signal(null, consolidation);

        consolidation = svc.open(consolidation);
        
        //submit to province
        signal("submit", consolidation);
        simulateSubmitToProvinceTask(consolidation);

        waitForRemoteData(consolidation);
        
        return open(consolidation);
    }
    
    
    public Map remoteApproval(Map consolidation) {
        System.out.println("====================================");
        System.out.println("Performing remote approval... ");
        System.out.println("====================================");

        Util.sleep(1000);

        Map remote = Data.findRemoteEntity("consolidation", consolidation, true);
        currentTask = Data.getTask("consolidation_task", remote, "assign-receiver", true);


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
        executeApprovalTask(consolidation);

        signal("completed", remote, true);
        
        waitUntilConsolidationIsApproved(consolidation);
        
        return open(remote, true);
    }
    
    private void waitUntilConsolidationIsApproved(Map consolidation) {
        System.out.print("Waiting for local consolidation approval... ");
        Map approveConsolidation = Data.findEntity("consolidation", consolidation);
        while(!"APPROVED".equalsIgnoreCase(approveConsolidation.get("state")+"")) {
            Util.sleep(1000);
            approveConsolidation = Data.findEntity("consolidation", consolidation);
        }
        System.out.println("DONE");
    }
    
    private void executeApprovalTask(Map consolidation) {
        System.out.print("Approving consolidation... ");
        remoteSvc.initApprove(consolidation);
        remoteSvc.assignNewTdNos(consolidation);
        remoteSvc.approveConsolidatedLandFaas(consolidation);
        List<Map> affectedRpus = remoteSvc.getAffectedRpusForApproval(consolidation.get("objid"));
        for (Map arpu : affectedRpus) {
            remoteSvc.approveAffectedRpuFaasRecord(consolidation, arpu);
        }
        remoteSvc.approve(consolidation);
        System.out.println("DONE");
    }
    
    private void waitForRemoteData(Map consolidation) {
        Data.findRemoteEntity("consolidation", consolidation, true);
        Util.sleep(1000);
    }

    private void addConsolidatedLands(Map consolidation) {
        ledgerModel.simulatePaidLedger(landFaas1);
        ledgerModel.simulatePaidLedger(landFaas2);
        
        Map faas1 = Lookup.faasById(landFaas1.get("objid"));
        Map cland1 = new HashMap();
        cland1.put("objid", "CL" + new java.rmi.server.UID());
        cland1.put("consolidationid", consolidation.get("objid"));
        cland1.put("faas", faas1);
        cland1.put("rpuid", faas1.get("rpuid"));
        cland1.put("rpid", faas1.get("realpropertyid"));
        cland1.put("landfaasid", faas1.get("objid"));
        svc.validateConsolidatedLand(cland1);
        svc.saveConsolidatedLand(cland1);
        
        Map faas2 = Lookup.faasById(landFaas2.get("objid"));
        Map cland2 = new HashMap();
        cland2.put("objid", "CL" + new java.rmi.server.UID());
        cland2.put("consolidationid", consolidation.get("objid"));
        cland2.put("faas", faas2);
        cland2.put("rpuid", faas2.get("rpuid"));
        cland2.put("rpid", faas2.get("realpropertyid"));
        cland2.put("landfaasid", faas2.get("objid"));
        svc.validateConsolidatedLand(cland2);
        svc.saveConsolidatedLand(cland2);
    }

    private void assessConsolidatedFaas(Map consolidation) {
        Map faas = new HashMap();
        faas.put("objid", consolidation.get("newfaasid"));
        faas = faasModel.open(faas);
        assessLandFaas(faas);
    }
    
    private void assessLandFaas(Map sland) {
        Map consolidation = Entity.findEntity(Entity.ENTITY_001);
        
        Map faas = new HashMap();
        faas.put("objid", sland.get("objid"));
        faas = faasModel.open(faas);
        
        faas.put("taxpayer", consolidation);
        faas.put("owner_name", consolidation.get("name"));
        faas.put("owner_address", "OWNER ADDRESS");
        
        faasData.buildRealProperty(faas);
        
        FaasLand.assess(faas);
        faasModel.update(faas);
    }

    
    private void simulateSubmitToProvinceTask(Map consolidation) {
        Util.sleep(1000);
        System.out.print("Executing submit to province task... ");
        svc.initApprove(consolidation);
        svc.assignNewTdNos(consolidation);
        consolidation.putAll(svc.submitToProvince(consolidation));
        System.out.println("DONE");
        
        //forprovsubmission state (completed)
        signal("completed", consolidation);
    }


}
