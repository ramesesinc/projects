package com.rameses.gov.etracs.rpt.entity.rpu.land.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rpt.util.RPTUtil

public class RpuLandController extends com.rameses.gov.etracs.rpt.rpu.ui.RpuController 
{
    
    @Service('LandRPUService')
    def svc;
    
}