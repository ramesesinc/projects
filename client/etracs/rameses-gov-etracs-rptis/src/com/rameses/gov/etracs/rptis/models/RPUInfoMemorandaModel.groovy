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
                entity.memoranda = resolveTemplate(it);
                binding.refresh('entity.memoranda');
            },
        ])
    }
    
    def resolveTemplate(t){
        def engine = new groovy.text.SimpleTemplateEngine();
        def binding = [:]
        
        t.params.each{
            binding[it.param.name] = it.value;
        }
        
        try {
            def template = engine.createTemplate(t.template).make(binding);
            return template.toString();
        }
        catch( e) {
            return t.template;
        }
    }
}    
    