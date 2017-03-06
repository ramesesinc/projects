package com.rameses.gov.police.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;
import java.rmi.server.UID;
import com.rameses.rcp.framework.ClientContext;

public class PoliceClearanceModel extends CrudFormModel {
        
    def getLookupPersonalInfo() {
        hasLookup = true;
        return InvokerUtil.lookupOpener( 'individualentity:lookup', [
            onselect : { o->
                entity.person = entitySvc.open( o );
                binding.refresh();
            },
            onempty: {
                hasLookup = false;
                entity.person = null;
                binding.refresh('entity.*');
            }
        ]);
    }

    void loadPhysical() {
        def p = physicalSvc.open([objid:entity.person.objid]);            
        entity.person.putAll( p );
    }

    void savePhysical() {
        physicalSvc.save( entity.person );
    }

            
}