package com.rameses.gov.etracs.services;


import java.util.Map;

public interface ConsolidationWorkflowService extends WorkflowService{
    @Override
    public Map signal(Map entity);
}

