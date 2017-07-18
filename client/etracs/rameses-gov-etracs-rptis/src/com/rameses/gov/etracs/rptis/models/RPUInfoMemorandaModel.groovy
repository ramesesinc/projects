package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 

class RPUInfoMemorandaModel extends SubPageModel
{
    @Binding
    def binding;
    
    def getLookupTemplate(){
        return Inv.lookupOpener('memorandatemplate:lookup', [
            
            onselect : { 
                entity.memoranda = it.template;
                binding.refresh('entity.memoranda');
            },
        ])
    }
}    
    