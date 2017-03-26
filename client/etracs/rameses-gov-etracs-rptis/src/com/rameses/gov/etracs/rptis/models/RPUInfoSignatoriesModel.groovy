package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 

class RPUInfoSignatoriesModel extends SubPageModel 
{
    
    @PropertyChangeListener
    def listener = [
        'entity.*dtsigned':{caller.calculateAssessment()}
    ]
    
    /*===============================================
     * Signatory Lookup Support
     *===============================================*/
            
    def getLookupAppraiser(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTAPPRAISER',
            onselect : { 
                if (!entity.appraiser) 
                    entity.appraiser = [:]
                entity.appraiser.putAll(it)
            },
            onempty  : {clearSignatory(entity.appraiser)},
        ])
        
    }
    
    def getLookupRecommender(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTRECOMMENDER',
            onselect : { 
                if ( !entity.recommender )
                    entity.recommender = [:]
                entity.recommender.putAll(it) 
            },
            onempty  : { clearSignatory(entity.recommender)},
        ])
        
    }
    
    def getLookupTaxmapper(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTTAXMAPPER',
            onselect : { 
                if (!entity.taxmapper)
                    entity.taxmapper = [:]
                entity.taxmapper.putAll(it) 
            },
            onempty  : { clearSignatory(entity.taxmapper) },
        ])
        
    }
    
    def getLookupApprover(){
        return InvokerUtil.lookupOpener('txnsignatory:lookup',[
            doctype : 'RPTAPPROVER',
            onselect : { 
                if (!entity.approver)
                    entity.approver = [:]
                entity.approver.putAll(it)
            },
            onempty  : { clearSignatory(entity.approver)},
        ])
        
    }
    
    void clearSignatory(signatory){
        if (signatory){
            signatory.personnelid = null;
            signatory.name = null;
            signatory.title = null;
        }
    }    
    
    
   
}    