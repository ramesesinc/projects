package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.camera.*;
import com.rameses.seti2.models.*;

class EntityProfileModel extends CrudFormModel {

    def associatedList = [
        fetchList: { o->
            def m = [_schemaname: 'entityindividual'];
            m.findBy = [profileid: entity.objid];
            return queryService.getList(m);
        }
    ] as BasicListModel;
    
}
