package com.rameses.gov.etracs.nsrp.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.camera.*;
import com.rameses.seti2.models.*;

class EntityLanguageModel extends CrudFormModel
{

    @Service("JobSearchService")
    def jobSvc;

    @Binding
    def binding;

    def languages;

    void doOpen(){
        super.open();
        languages = jobSvc.getLanguages();
    }

    void afterCreate() {
        entity.entityid = caller?.masterEntity?.objid;
        languages = jobSvc.getLanguages();
    }
}