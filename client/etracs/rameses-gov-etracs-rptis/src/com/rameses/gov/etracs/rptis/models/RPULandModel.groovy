package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 

public class RPULandModel extends RPUModel
{
    void calculateAssessment(){
        super.calculateAssessment();
        entity.rpu.landvalueadjustment = 0.0;
        if (entity.rpu.landadjustments)
            entity.rpu.landvalueadjustment = entity.rpu.landadjustments.adjustment.sum();
    }
}
    