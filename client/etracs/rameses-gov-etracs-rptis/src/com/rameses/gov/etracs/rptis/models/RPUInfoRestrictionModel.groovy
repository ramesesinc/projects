package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPUInfoRestrictionModel extends SubPageModel
{
    @Service('DateService')
    def dtSvc;
    
    @Service('QueryService')
    def querySvc
    
    def selectedItem;
    
    def states = ['ACTIVE', 'UNRESTRICTED']
    
    void init(){
        if (!entity.restrictions)
            entity.restrictions = []
    }
    
    void afterModeChanged(){
        listHandler.reload();
    }

    
    def listHandler = [
        fetchList   : { entity.restrictions },
        
    ] as BasicListModel 
    
    
}    
    