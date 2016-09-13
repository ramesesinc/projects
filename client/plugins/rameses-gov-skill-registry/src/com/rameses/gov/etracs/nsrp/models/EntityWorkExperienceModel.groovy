package com.rameses.gov.etracs.nsrp.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.camera.*;
import com.rameses.seti2.models.*;

class EntityWorkExperienceModel extends CrudFormModel
{
    @Service("JobSearchService")
    def jobSvc;

    def jobtitles;

    void doOpen(){
        super.open();
        jobtitles = jobSvc.getJobTitles();
    }

    void afterCreate() {
        entity.entityid = caller?.masterEntity?.objid;
        jobtitles = jobSvc.getJobTitles();
    }
}