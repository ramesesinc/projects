package com.rameses.gov.etracs.nsrp.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.camera.*;
import com.rameses.seti2.models.*;

class EntityElibilityModel extends CrudFormModel
{
    @Service("JobSearchService")
    def jobSvc;

    @Binding
    def binding;

    def levels;
    def courses;
    def level;

    @PropertyChangeListener
    def listener = [
        "level": {o ->
            entity.educationlevel = o.level;
            binding.refresh("entity.*");
        }
    ];

    void doOpen(){
        super.open();
        courses = jobSvc.getCourses();
        levels = jobSvc.getEducationalLevels();
        levels.each{
            if(entity.educationlevel == it.level){
                level = it;
            }
        }
    }

    void afterCreate() {
        entity.entityid = caller?.masterEntity?.objid;
        levels = jobSvc.getEducationalLevels();
        courses = jobSvc.getCourses();
    }

}