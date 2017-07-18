package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.RPTUtil;
            
class FAASExplorerViewModel extends ExplorerViewController
{
    @Service('FAASListService')
    def svc 
            
    @Service('LGUService')
    def lguSvc
            
    @Service('Var')
    def var 
            
    @Service('GeneralRevisionService')
    def grSvc;
    
    def states 
    def classifications;
    def txntypes;
            
    boolean allowSearch = false;
    boolean allowOpen = true;
    boolean allowCreate = false;
            
    String formTarget = 'window'
    String serviceName = 'FAASListService'
    String formName = 'faas:list';
    String entityName = 'faaslist';
            
            
    int getRows() { return 20}
                        
    List getStates(){
        if (!states) states = svc.getStates();
        return states
    }
            
    List getClassifications() {
        if (!classifications)
        classifications = svc.getClassifications();
        return classifications;
    }
            
    List getTxntypes(){
        return svc.getTxnTypes();
    }
    
    public Object openItem( Map item ) {  
        def opener = doOpen();
        opener.target = 'window';
        return opener;
    }     
            
    def doOpen(){
        if (!selectedEntity) return;
                
        if (selectedEntity.taskstate == null && selectedEntity.state.matches('CURRENT|CANCELLED'))
        return InvokerUtil.lookupOpener('faas:open:closedwf', [entity:selectedEntity])
                
        if ( RPTUtil.toBoolean(selectedEntity?.datacapture, false) == true )
        return InvokerUtil.lookupOpener('faas:capture:open', [entity:selectedEntity])
                
        return InvokerUtil.lookupOpener('faas:open', [entity:selectedEntity])
    }
            
    def onOpenItem(Object item, String columnName) { 
        return doOpen();
    }
            
    def getBarangays(){
        if (! query.lgu)
        return lguSvc.getBarangaysByParentId(null);
        return lguSvc.lookupBarangaysByRootId(query.lgu?.objid);
    }            
            
            
    def rputypes;
            
    def getRputypes(){
        if (!rputypes)
            rputypes = svc.getRpuTypes();
        return rputypes;
    }

    void clear(){
        query.clear()
        search();
        binding.refresh('.*');
        binding.focus('query.tdno');

    }
        
        
    def getEntity(){
        return selectedEntity;
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
        
    def getRevisionyears(){
        if (!selectedEntity) 
        return []
        return grSvc.getRyList(selectedEntity.lguid, selectedEntity.ry, selectedEntity.rputype );
    }
            
    def getShowRevision(){
        if (!selectedEntity) return false;
        if (selectedEntity.state != 'CURRENT') return false;
        def rys = getRevisionyears();
        if( !rys ) return false;
        def maxry = rys.max()
        if (selectedEntity.ry >= maxry) return false;
        return true;
    }            
            
}  
    