package com.rameses.gov.etracs.services;


import java.util.List;
import java.util.Map;

public interface RPTRequirementService {
    public List<Map> getList(Map param);
    public Map create(Map entity);
    public Map open(Map entity);
    public Map update(Map entity);
    public Map delete(Map entity);
}

