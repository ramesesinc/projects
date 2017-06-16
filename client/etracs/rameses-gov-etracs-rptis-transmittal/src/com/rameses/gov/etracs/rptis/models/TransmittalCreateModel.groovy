package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class TransmittalCreateModel
{
    @Binding
    def binding;
    
    def openers;
    def opener;
    
    String title = 'Transmittal (Initial)'
    
    void init(){
        openers = Inv.lookupOpeners('rpttransmittal', [:])
    }
    
    def oncomplete = {
        opener = null;
        binding?.refresh();
    }
    
    def next(){
        if (!opener)
            throw new Exception('Document Type is required.');
            
        opener.handle.createTransmittal();
        def filetype = opener.handle.getFileType();
        return Inv.lookupOpener('rpttransmittal:' + filetype + ':open', [entity:opener.handle.entity, oncomplete:oncomplete]);
    }
}