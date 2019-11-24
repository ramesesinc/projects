package com.rameses.gov.etracs.rptis.models;

import com.rameses.gov.etracs.services.CancelledFAASService;
import com.rameses.gov.etracs.services.CancelledFAASWorkflowService;
import test.utils.Data;
import test.utils.Lookup;
import test.utils.ServiceUtil;
import test.utils.Util;
import java.util.HashMap;
import java.util.Map;

public class CancelledFaasModel {
    private CancelledFAASService svc;
    private CancelledFAASService remoteSvc;
    private CancelledFAASWorkflowService workflowSvc;
    private CancelledFAASWorkflowService remoteWorkflowSvc;
    public Map currentTask;
    
    public CancelledFaasModel() {
        svc = ServiceUtil.create(CancelledFAASService.class);
        workflowSvc = ServiceUtil.create(CancelledFAASWorkflowService.class);
        
        remoteSvc = ServiceUtil.create(CancelledFAASService.class, true);
        remoteWorkflowSvc = ServiceUtil.create(CancelledFAASWorkflowService.class, true);
        
    }
    
    public Map create(Map entity) {
        return svc.create(entity);
    }
    
    public Map approve(Map entity) {
        return svc.approve(entity);
    }
    
    public Map open(Map entity) {
        return open(entity,false);
    }
    
    public Map open(Map entity, boolean remote) {
        if (remote) {
            return remoteSvc.open(entity);
        }
        return svc.open(entity);
    }
    
    /* waits until the local cancelledfaas record state 
     * is set to APPROVED by the notification
     */
    public Map reload(Map remote) {
        System.out.print("Waiting for local data updates... ");
        Map local = open(remote);
        while (!"APPROVED".equalsIgnoreCase(local.get("state").toString())) {
            Util.sleep(2000);
            local = open(remote);
        }
        System.out.println("DONE");
        return local;
    }
    
    public Map createAndApprove(Map faas) {
        Util.sleep(2000);
        Map cancelledFaas = initCancelledFaas(faas);
        cancelledFaas = create(cancelledFaas);
        approve(cancelledFaas);
        return open(cancelledFaas);
    }
        
    public Map receive(Map faas) {
        Util.sleep(2000);
        Map cancelledFaas = initCancelledFaas(faas);
        cancelledFaas = svc.init(cancelledFaas);
        currentTask = (Map) cancelledFaas.remove("task");
        addFindings(cancelledFaas);
        return open(cancelledFaas);
    }
    
    public Map taxmap(Map cancelledFaas) {
        //submit to taxmapper
        signal("submit", cancelledFaas);
        
        //assign to taxmapper
        signal(null, cancelledFaas);
        
        return svc.open(cancelledFaas);
    }
    
    public Map recommend(Map cancelledFaas) {
        //submit to recommender
        signal("submit", cancelledFaas);
        
        //assign to recommender
        signal(null, cancelledFaas);
        
        //submit to province
        signal("submit_to_province", cancelledFaas);
        
        Util.sleep(2000);
        
        return svc.open(cancelledFaas);
    }
    
    public Map remoteApproval(Map cancelledFaas) {
        System.out.println("====================================");
        System.out.println("Performing remote approval... ");
        System.out.println("====================================");
        
        Util.sleep(1000);
        
        Map remote = Data.findRemoteEntity("cancelledfaas", cancelledFaas, true);
        currentTask = Data.getTask("cancelledfaas_task", remote, "assign-receiver", true);
        
        
        System.out.println("Receiving...");
        signal(null, remote, true);
        signal("submit_taxmapper", remote, true);
        
        System.out.println("Taxmapping...");
        signal(null, remote, true);
        signal("submit", remote, true);
        
        System.out.println("Recommending...");
        signal(null, remote, true);
        signal("submit_approver", remote, true);
        
        System.out.println("Approval...");
        signal(null, remote, true);
        signal("approve", remote, true);
        
        System.out.println("==================================");
        
        return open(remote, true);
    }


    private Map initCancelledFaas(Map faas) {
        Map m = new HashMap();
        m.put("objid", "TCF" + new java.rmi.server.UID());
        m.put("faas", Lookup.faas(faas.get("tdno")));
        m.put("reason", Data.getCancelReason());
        m.put("lasttaxyear", Data.getCurrentYear());
        m.put("online", true);
        m.put("remarks", "CANCELATION REMARKS");
        return m;
    }
    
    public void signal(String action, Map entity) {
        signal(action, entity, false);
    }
    
    public void signal(String action, Map entity, boolean remote) {
        Map info = new HashMap();
        info.put("taskid", currentTask.get("objid"));
        info.put("refid", currentTask.get("refid"));
        info.put("state", currentTask.get("state"));
        info.put("action", action);
        info.put("data", entity);
        
        Map result = null;
        if (remote) {
            result = remoteWorkflowSvc.signal(info);
        }
        else {
            result = workflowSvc.signal(info);
        }
        
        currentTask = (Map) result.remove("task");
    }

    private void addFindings(Map cancelledFaas) {
        Map finding = new HashMap();
        finding.put("_schemaname", "rpt_examination");
        finding.put("objid", "F" + new java.rmi.server.UID());
        finding.put("parent_objid", cancelledFaas.get("objid"));
        finding.put("dtinspected", Data.getServerDate());
        finding.put("findings", "FINDINGS");
        finding.put("recommendations", "RECOMMENDATIONS");
        finding.put("notedby", "NOTEDBY");
        finding.put("notedbytitle", "NOTEDBY TITLE");
        finding.put("inspectors", "-");
        ServiceUtil.persistence().create(finding);
    }

}
