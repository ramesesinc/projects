package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 

class RPUInfoSignatoriesModel extends SubPageModel 
{
    @Script("FaasSignatoryUtil")
    def util;

    
    @PropertyChangeListener
    def listener = [
        'entity.*dtsigned':{
            if (caller) {
                caller.calculateAssessment()
            }
        }
    ]
    
    /*===============================================
     * Signatory Lookup Support
     *===============================================*/
    
    
    def getLookupAppraiser(){
        return util.getLookupAppraiser();
    }
    
    def getLookupRecommender(){
        return util.getLookupRecommender();
    }
    
    def getLookupTaxmapper(){
        return util.getLookupTaxmapper();
    }
    
    def getLookupApprover(){
        return util.getLookupApprover();
    }
   
}    