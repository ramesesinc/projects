package com.rameses.gov.etracs.services;


import java.util.List;
import java.util.Map;

public interface ConsolidationService {
    public Map init();
    public Map create(Map entity);
    public Map update(Map entity);
    public Map open(Map entity);
    public Map delete(Map entity);
    public Map submitToProvince(Map entity);
    public Map initApprove(Map entity);
    public Map approve(Map entity);
    
    public Map saveConsolidatedLand(Map entity);
    public Map assignNewTdNos(Map entity);
    public List<Map> getAffectedRpusForApproval(Object objid);
    public Map approveConsolidatedLandFaas(Map entity);
    public Map approveAffectedRpuFaasRecord(Map entity);

    public void validateConsolidatedLand(Map land);

    public void approveAffectedRpuFaasRecord(Map consolidation, Map arpu);
}

