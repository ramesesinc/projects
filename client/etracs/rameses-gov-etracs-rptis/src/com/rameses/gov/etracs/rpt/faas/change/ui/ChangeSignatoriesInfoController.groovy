package com.rameses.gov.etracs.rpt.faas.change.ui;
        

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.faas.change.ui.*;


public class ChangeSignatoriesInfoController extends ChangeFaasInfoController
{
    @Script("FaasSignatoryUtil")
    def util;

    
    String title = 'Modify Signatories';
    
    def prevsignatory;
 
    public def getModifiedEntity(){
        prevsignatory = [:]
        prevsignatory.appraiser   = util.clone(entity.appraiser)
        prevsignatory.taxmapper   = util.clone(entity.taxmapper)
        prevsignatory.recommender = util.clone(entity.recommender)
        prevsignatory.approver    = util.clone(entity.approver)
        
        return [
            appraiser       : entity.appraiser,
            taxmapper       : entity.taxmapper,
            recommender     : entity.recommender,
            approver        : entity.approver,
        ]
    }
    
    def doCancel(){
        entity.putAll(prevsignatory);
        return '_close';
    }
    
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
       