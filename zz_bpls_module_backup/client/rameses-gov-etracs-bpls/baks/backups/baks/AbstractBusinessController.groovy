package com.rameses.gov.etracs.bpls.business;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*

class AbstractBusinessController extends PageFlowController {

    @Binding
    def binding;

    def entity = [lobs:[]];
    def officeTypes = LOV.BUSINESS_OFFICE_TYPES;
    def orgTypes = LOV.ORG_TYPES;
    def selectedLob;

    def getLookupOwners() {
        return InvokerUtil.lookupOpener( "entity:lookup", [
            onselect: { o->
                entity.owner = o;
            }
        ]);
    }
            
    def getLookupLob() {
        return InvokerUtil.lookupOpener( "lob:lookup", [
            onselect: { o->
                if(entity.lobs.find{ it.lobid == o.objid }!=null) 
                    throw new Exception("Item already added");
                def m = [:];
                m.objid = "BPLOB"+new UID();
                m.lobid = o.objid;
                m.name = o.name;
                m.classification = o.classification;
                m.assessmenttype = "NEW";
                entity.lobs << m; 
                lobModel.reload();
                binding.focus("lob");
            }
        ]);
    }
        
    def removeLob()  {
        if( !selectedLob ) return;
        MsgBox.alert( entity );
        if( MsgBox.confirm("You are about to remove this item. Continue?")) {
            entity.lobs.remove(selectedLob);
            if( !entity._lobs_deleted ) entity._lobs_deleted = [];
            entity._lobs_deleted << selectedLob;
            lobModel.reload();
            binding.focus("lob");
        }
    }
   
    def lobModel = [
        fetchList: { o->
            return entity.lobs;
        },
    ] as BasicListModel;


}