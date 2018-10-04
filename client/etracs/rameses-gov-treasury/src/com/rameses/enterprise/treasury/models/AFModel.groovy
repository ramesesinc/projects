package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;
        
class AFModel extends CrudFormModel {

    def useTypes = ["collection", "disbursement"];
    def formTypes = [ "serial", "cashticket", "eor" ];

    void afterCreate() {
        entity.system = 0;
        entity.serieslength = 0;
        entity.denomination = 0;
    }

}        