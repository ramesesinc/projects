package com.rameses.gov.etracs.services;


import java.util.List;
import java.util.Map;

public interface FAASLookupService {
    public List<Map> lookupFaas(Map entity);
}

