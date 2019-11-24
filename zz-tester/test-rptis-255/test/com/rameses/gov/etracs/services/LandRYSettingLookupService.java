package com.rameses.gov.etracs.services;


import java.util.List;
import java.util.Map;

public interface LandRYSettingLookupService {
    public List<Map> lookupAssessLevels(Map entity);
    public List<Map> lookupSubclasses(Map entity);
}

