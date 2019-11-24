package com.rameses.gov.etracs.services;


import java.util.Map;

public interface ProvinceRedFlagService {
    public Map postRedFlag(Map entity);
    public void resolveRedFlag(Map entity);
    
}

