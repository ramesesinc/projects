package com.rameses.gov.etracs.services;


import java.util.List;
import java.util.Map;

public interface SubdivisionService {
    public Map create(Map entity);
    public Map update(Map entity);
    public Map open(Map entity);
    public Map delete(Map entity);
    public Map addMotherLand(Map entity);
    public Map deleteMotherLand(Map entity);
    public Map createSubdividedLand(Map entity, Map subdivision);
    public Map deleteSubdividedLand(Map entity);
    public void createAffectedRpus(Map entity);
    public Map saveAffectedRpu(Map entity);
    public List<Map> getAffectedRpus(Map entity);
    public Map submitToProvince(Map entity);
    public Map approve(Map entity);

    public void initApproveSubdivision(Map subdivision);

    public void assignNewTdNos(Map subdivision);
    public List<Map> getSubdividedLandsForApproval(Object objid);

    public void approveSubdividedLandFaasRecord(Map subdivision, Map land);

    public List<Map> getAffectedRpusForApproval(Object objid);

    public void approveAffectedRpuFaasRecord(Map subdivision, Map arpu);

    public List<Map> getCancelledImprovements(Object objid);

    public void approveCancelledImprovement(Map subdivision, Map ci);
    
}

