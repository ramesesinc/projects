package com.rameses.gov.etracs.rptis.models;

import com.rameses.gov.etracs.services.MunicipalityRedFlagService;
import test.utils.Data;
import test.utils.ServiceUtil;
import test.utils.Util;
import java.util.Map;

public class RedFlagModel {
    private MunicipalityRedFlagService muniSvc;
    
    public RedFlagModel() {
        muniSvc = ServiceUtil.create(MunicipalityRedFlagService.class);
    }
    
    public Map resolveRedFlag(Map redFlag, Map changeInfo) {
        return muniSvc.resolveRedFlag(redFlag, changeInfo);
    }

    public Map openRemoteResolved(Map redFlag) {
        Map remote = Data.findRemoteEntity("rpt_redflag", redFlag);
        while (!"RESOLVED".equalsIgnoreCase(remote.get("state").toString())) {
            Util.sleep(1000);
            remote = Data.findRemoteEntity("rpt_redflag", redFlag);
        }
        return remote;
    }
}
