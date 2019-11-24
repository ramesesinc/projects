package com.rameses.gov.etracs.services;


import java.util.Map;

public interface FAASRestrictionService {
    public Map create(Map entity);
    public Map open(Map entity);
    public Map approve(Map entity);
    public Map removeRestriction(Map entity);
    public Map cancel(Map entity, Map receipt);

    public Map reactivate(Map restriction);
}

