package com.rameses.admin.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class OrgClassModel extends CrudFormModel {

    def getLookupParentClass() {
        def params = ['query.excludename' : entity.name]; 
        params.onselect = {o-> 
            entity.parentclass = o.name; 
            binding.refresh('entity.parentclass');
        }
        params.onempty = {
            entity.parentclass = null; 
            binding.refresh('entity.parentclass');
        } 
        return Inv.lookupOpener('orgclass:lookup', params); 
    } 
}
