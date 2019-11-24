package test.rptis.models;

import com.rameses.gov.etracs.rptis.models.Entity;
import com.rameses.gov.etracs.rptis.models.LedgerModel;
import com.rameses.gov.etracs.rptis.models.RedFlagModel;
import com.rameses.gov.etracs.rptis.models.WorkflowModel;
import test.rptis.services.FAASService;
import com.rameses.gov.etracs.services.FAASWorkflowService;
import com.rameses.gov.etracs.services.ProvinceRedFlagService;
import com.rameses.gov.etracs.services.RPTLedgerService;
import com.rameses.gov.etracs.services.WorkflowService;
import test.utils.Data;
import test.utils.Lookup;
import test.utils.ServiceUtil;
import test.utils.Util;
import java.util.HashMap;
import java.util.Map;

public class FaasModel extends WorkflowModel{
    private FAASService svc;
    private FAASService remoteSvc;
    private ProvinceRedFlagService remoteProvSvc;
    
    private LedgerModel ledgerModel;
    private RedFlagModel redflagModel;
    
    public int RY = 2014;
    public String PIN_TYPE = "new";
    
    public FaasModel() {
        this(Data.getRevisionYear(), "new");
    }
    
    public FaasModel(int ry, String pinType) {
        this.svc = ServiceUtil.create(FAASService.class);
        this.remoteSvc = ServiceUtil.create(FAASService.class, true);
        this.remoteProvSvc = ServiceUtil.create(ProvinceRedFlagService.class, true);
        this.RY = ry;
        this.PIN_TYPE = pinType;
        this.ledgerModel = new LedgerModel();
        this.redflagModel = new RedFlagModel();
    }

    @Override
    public void initWorkflowModels() {
        workflowSvc = (WorkflowService) ServiceUtil.create(FAASWorkflowService.class);
        remoteWorkflowSvc = (WorkflowService) ServiceUtil.create(FAASWorkflowService.class, true);
    }
    
    
    
    public Map initCaptureAndCreate(Map info) {
        return svc.initCaptureAndCreate(info);
    }
    
    public Map initAndCapture(){
        Map info = createInitialInfo();
        return initAndCapture(info);
    }
    
    public Map initAndCapture(Map info){
        Map faas = initCaptureAndCreate(info);
        return capture(faas);
    }
    
    public Map initOnlineTransaction(Map info){
        return svc.initOnlineTransaction(info);
    }
    
    public Map submitForApproval(Map faas) {
        return svc.submitForApproval(faas);
    }
    
    public Map capture(Map faas) {
        FaasData land = new FaasData();
        land.build(faas);
        return update(faas);
    }
    
    public Map update(Map entity){
        return svc.updateFaas(entity);
    }
    
    public Map open(Map faas){
        return open(faas, false);
    }
    
    public Map open(Map faas, boolean remote){
        if (remote) {
            waitForRemoteData(faas);
            return remoteSvc.openFaas(Data.toMapObjid(faas));
        }else {
            return svc.openFaas(Data.toMapObjid(faas));
        }
    }

    public Map approve(Map faas) {
        faas = open(faas);
        svc.approveFaas(faas);
        return open(faas);
    }
    
    public Map createAndApprove() {
        return createAndApprove(createInitialInfo());
    }
    
    public Map createAndApprove(Map info) {
        Map faas = initAndCapture(info);
        approve(faas);
        return open(faas);
    }
    
    public Map createInitialInfo() {
        return createInitialInfo("999", "01");
    }
    public Map createInitialInfo(String ssection, String sparcel) {
        Map brgy = Data.getBarangay();
        Map lgu = Data.getLgu(brgy);
        
        String fullpin = brgy.get("pin") + "-" + ssection + '-' + sparcel;
        
        Map info = new HashMap();
        info.put("lgu", lgu);
        info.put("lgutype", lgu.get("orgclass"));
        info.put("barangay", brgy);
        info.put("rputype", "land");
        info.put("ry", RY);
        info.put("txntype", Data.getTxnType("GR"));
        info.put("pintype", PIN_TYPE);
        info.put("datacapture", true);
        info.put("fullpin", fullpin);
        info.put("pin", fullpin);
        info.put("suffix", 0);
        info.put("section", ssection);
        info.put("parcel", sparcel);
        return info;
    }

    public void cleanUp(Map faas) {
        FaasData.cleanUp(faas);
    }
    
    public Map approveLedger(Map faas) {
        RPTLedgerService svc = ServiceUtil.create(RPTLedgerService.class);
        Map ledger = Data.getLedgerByFaas(faas);
        ledger = svc.open(ledger);
        return svc.approve(ledger);
    }

    public Map revertToInterim(Map faas) {
        Util.sleep(2000);
        svc.revertToInterim(faas);
        return open(faas);
    }

    public void delete(Map faas) {
        svc.deleteFaas(faas);
    }

    public Map receiveTransfer(Map faas) {
        ledgerModel.simulatePaidLedger(faas);
        Map info = createTransferInfo(faas);
        Map newFaas = initOnlineTransaction(info);
        currentTask = (Map)newFaas.get("task");
        return newFaas;
    }
    
    public Map taxmap(Map faas) {
        //submit and assign to taxmapper
        signal("submit_taxmapper", faas);
        signal(null, faas);
        return open(faas);
    }

    public Map appraise(Map faas) {
        //submit and assign to appraiser
        signal("submit", faas);
        signal(null, faas);
        updateTransferInfo(faas, faas);
        return open(faas);
    }

    public Map recommend(Map faas) {
        //submit and assign to recommender
        signal("submit", faas);
        signal(null, faas);

        faas = open(faas);

        //submit to province
        signal("submit_to_province", faas);
        waitForRemoteData(faas);
        
        return open(faas);
    }
    
    public Map remoteReceive(Map faas) {
        System.out.println("====================================");
        System.out.println("Performing remote receive... ");
        System.out.println("====================================");

        Util.sleep(1000);

        Map remote = Data.findRemoteEntity("faas", faas, true);
        currentTask = Data.getTask("faas_task", remote, "assign-receiver", true);

        System.out.println("Receiving...");
        signal(null, remote, true);
        return open(faas);
    }
    
    public Map remoteAddRedFlag(Map faas) {
        Map redFlag = new HashMap();
        redFlag.put("objid", "RF" + new java.rmi.server.UID());
        redFlag.put("parentid", faas.get("objid"));
        redFlag.put("refid", faas.get("objid"));
        redFlag.put("refno", faas.get("tdno"));
        redFlag.put("message", "TEST CHANGE BOUNDARY");
        redFlag.put("action", "change_property_info");
        redFlag.put("lguid", faas.get("lguid"));
        redFlag.put("lgu", faas.get("lgu"));
        return remoteProvSvc.postRedFlag(redFlag);
    }
    
    public Map resolveRedFlag(Map redFlag) {
        Map faas = new HashMap();
        faas.put("objid", redFlag.get("refid"));
        faas = open(faas);
        
        Map changeInfo = new HashMap();
        changeInfo.put("objid", "CI" + new java.rmi.server.UID());
        changeInfo.put("refid", redFlag.get("refid"));
        changeInfo.put("faasid", redFlag.get("refid"));
        changeInfo.put("rpid", faas.get("realpropertyid"));
        changeInfo.put("rpuid", faas.get("rpuid"));
        changeInfo.put("redflagid", redFlag.get("objid"));
        changeInfo.put("action", redFlag.get("action"));
        changeInfo.put("reason", "TEST");
        
        Map newInfo = new HashMap();
        newInfo.put("cadastrallotno", "XX");
        newInfo.put("surveyno", "XX");
        newInfo.put("blocko", "XX");
        newInfo.put("street", "XX");
        newInfo.put("purok", "XX");
        newInfo.put("north", "XX");
        newInfo.put("south", "XX");
        newInfo.put("east", "XX");
        newInfo.put("west", "XX");
        changeInfo.put("newinfo", newInfo);
        changeInfo.put("previnfo", new HashMap());
        return redflagModel.resolveRedFlag(redFlag, changeInfo);
    }
    
    private Map createTransferInfo(Map faas) {
        Map refFaas = Lookup.faas(faas.get("tdno"));
        Map info = new HashMap();
        info.put("txntype", Data.getTxnType("TR"));
        info.put("datacapture", false);
        info.put("faas", refFaas);
        return info;
    }

    private void updateTransferInfo(Map newFaas, Map faas) {
        Map newOwner = Entity.findEntity(Entity.ENTITY_001);
        newFaas.put("tdno", "T-" + faas.get("tdno"));
        newFaas.put("taxpayer", newOwner);
        newFaas.put("owner", newOwner);
        newFaas.put("memoranda", "TRANSFER");
        FaasData.buildFaasSignatories(newFaas);
        newOwner = Entity.findEntity(Entity.ENTITY_002);
        newFaas.put("taxpayer", newOwner);
        newFaas.put("owner", newOwner);
        newFaas = update(newFaas);
    }

    private void waitForRemoteData(Map faas) {
        Data.findRemoteEntity("faas", faas, true);
        Util.sleep(1000);
    }

}
