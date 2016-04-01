package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class AccountSectionGeneralModel {
    
    @Service('WaterworksAccountService') 
    def acctSvc; 
    
    @Caller 
    def caller; 
    
    @Binding 
    def binding;
    
    def title = 'General Information';
    
    def getEntity() {
        return caller?.entity; 
    }
    
    def changeOwner(){ 
        def currentowner = acctSvc.initChangeOwner([ ownerid: entity.owner?.objid ]); 
        def params = [entity: [currentowner: currentowner ]];
        params.entity.acctname = entity.acctname; 
        params.entity.acctno = entity.acctno;
        params.handler = { o-> 
            def resp = acctSvc.postChangeOwner([ 
                            accountid: entity.objid, 
                            acctname: o.acctname, 
                            prevownerid: currentowner?.objid, 
                            ownerid: o.newowner.objid
                        ]);
                    
            entity.owner = resp?.owner;
            entity.address = resp?.address; 
            if ( resp?.acctname ) entity.acctname = resp.acctname; 
            
            binding.refresh();
        }
        return Inv.lookupOpener("waterworks_account:changeowner", params );
    }
    
    def changeMeter(){
        return Inv.lookupOpener("waterworks_account:changemeter", [:]); 
    }
    
}