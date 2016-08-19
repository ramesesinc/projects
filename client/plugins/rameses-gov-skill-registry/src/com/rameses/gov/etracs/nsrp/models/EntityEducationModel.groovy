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

    @PropertyChangeListener
    def listener = [
        "entity.level": {o ->
            entity.educationlevel = o.level;
            binding.refresh("entity.*");
        }
    ];

    void doOpen(){
        super.open();
        levels = jobSvc.getEducationalLevels();
        levels.each{
            if(entity.educationlevel == it.level){
                entity.level = it;
                println entity.level;
            }
        }
    }

    void afterCreate() {
        entity.entityid = caller?.masterEntity?.objid;
        levels = jobSvc.getEducationalLevels();
    }

}