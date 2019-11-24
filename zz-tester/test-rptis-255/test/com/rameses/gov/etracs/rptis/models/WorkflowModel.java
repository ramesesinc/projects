/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rptis.models;

import com.rameses.gov.etracs.services.RPTRequirementService;
import com.rameses.gov.etracs.services.RPTRequirementTypeService;
import com.rameses.gov.etracs.services.WorkflowService;
import test.utils.ServiceUtil;
import java.util.HashMap;
import java.util.Map;


public abstract class WorkflowModel {
    public Map currentTask;
    public WorkflowService workflowSvc;
    public WorkflowService remoteWorkflowSvc;
    public RPTRequirementService reqSvc;
    public RPTRequirementTypeService reqTypeSvc;
    
    public WorkflowModel() {
        reqSvc = ServiceUtil.create(RPTRequirementService.class);
        reqTypeSvc = ServiceUtil.create(RPTRequirementTypeService.class);
        initWorkflowModels();
    }
    
    public abstract void initWorkflowModels();
    
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
        } else {
            result = workflowSvc.signal(info);
        }

        currentTask = (Map) result.remove("task");
    }

}
