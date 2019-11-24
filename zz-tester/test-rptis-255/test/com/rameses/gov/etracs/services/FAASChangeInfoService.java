package com.rameses.gov.etracs.services;


import java.util.Map;

public interface FAASChangeInfoService {
    public Map open(Map entity);
    public Map openByRedFlag(Map entity);
    public Map updateInfo(Map entity);
}

