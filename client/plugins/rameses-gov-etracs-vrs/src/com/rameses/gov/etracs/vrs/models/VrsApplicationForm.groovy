package com.rameses.gov.etracs.vrs.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;

public class VrsApplicationForm extends CrudFormModel {
    
    String title = "Vehicle Registration";
    String schemaName = "vrs_application";
    
    def create() {
        def z = super.create();
        entity.owner = [:];
        entity.vehicle = [:];
        entity.items = [];
        return z;
    }
    
    void assignFranchise() {
        println entity;
    }
    
    def itemModel = [
        fetchList: { o->
            return entity.items;
        },
        onAddItem: { o->
            entity.items << o;
            entity.amount = entity.items.sum{ it.amount };
        }
    ] as EditorListModel;
    
}