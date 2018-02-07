package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.interfaces.SubPage

class RPUInfoRequirementsModel extends FAASRequirementsModel implements SubPage
{
    def mode = 'read';
    
    void modeChanged(String mode){
        this.mode = mode;
        binding?.refresh();
    }
    
}    
    