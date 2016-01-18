package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class WaterworksAccountForm {
    
    String title = "Waterworks Account";
    String schemaName = "waterworks_account";
    
    @Service("WaterworksClassificationService")
    def classSvc;
    
    def classificationList;
    
    void init() {
        classificationList = classSvc.getList();
    }
    
    
}