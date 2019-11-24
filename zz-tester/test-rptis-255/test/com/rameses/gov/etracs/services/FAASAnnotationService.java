package com.rameses.gov.etracs.services;


import java.util.Map;

public interface FAASAnnotationService {
    public Map init(Map entity);
    public Map create(Map entity);
    public Map open(Map entity);
    public Map update(Map entity);
    public Map delete(Map entity);
    public Map submitForApproval(Map entity);
    public Map disapprove(Map entity);
    public Map approve(Map entity);
}

