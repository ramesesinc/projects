package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
            
class LguQueryListModel extends ExplorerViewController
{
    @Service('LGUService')
    def lguSvc
            
    @PropertyChangeListener
    def listener = [
            'query.(lgu|barangay|state)' : {
            search();
        }
    ]
            
    def getStates(){
        return ['INTERIM', 'FORAPPROVAL', 'APPROVED'];
    }
            
    def getLgus(){
        def orgclass = OsirisContext.env.ORGCLASS
        def orgid = OsirisContext.env.ORGID

        if ('PROVINCE'.equalsIgnoreCase(orgclass)) {
            return lguSvc.lookupMunicipalities([:])
        }
        else if ('MUNICIPALITY'.equalsIgnoreCase(orgclass)) {
            return [lguSvc.lookupMunicipalityById(orgid)]
        }
        else if ('CITY'.equalsIgnoreCase(orgclass)) {
            return [lguSvc.lookupCityById(orgid)]
        }
        return []
    }
    
    def getBarangays(){
        if (! query.lgu)
            return lguSvc.getBarangaysByParentId(null);
        return lguSvc.lookupBarangaysByRootId(query.lgu?.objid);
    }         

}  