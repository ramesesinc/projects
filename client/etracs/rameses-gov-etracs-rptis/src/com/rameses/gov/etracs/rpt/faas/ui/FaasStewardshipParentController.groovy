package com.rameses.gov.etracs.rpt.faas.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class FaasStewardshipParentController extends FaasController 
{
    def motherfaas;
    
    
    def getEntity(){
        return motherfaas; 
    }
    
    void openMotherFaas(){
        motherfaas = service.openFaas([objid:super.getEntity().parentfaasid])
        motherfaas.taskstate = super.getEntity().taskstate;
        motherfaas.assignee = super.getEntity().assignee;
        loadRpuOpener();
        mode = MODE_READ;
    }
}
