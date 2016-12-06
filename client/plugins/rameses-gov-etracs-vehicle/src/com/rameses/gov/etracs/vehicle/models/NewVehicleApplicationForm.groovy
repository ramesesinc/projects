package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.gov.etracs.vehicle.models.*;
import com.rameses.enterprise.models.*;

public class NewVehicleApplicationForm extends AbstractVehicleEntryForm {
    

    def franchiseno;
    
    void create() {
        setUp();
        entity = applicationService.init(entity);
        afterLoad();
        editmode = 'create';
    }
    
    void open() {
        if(!entity) throw new Exception("Call the setUp method in ApplicationForm first. Check start action");
        entity.franchiseno = franchiseno;
        entity = applicationService.init( entity );
        afterLoad();
        editmode = 'read';
    }

    
    def feeListModel = [
        fetchList: { o->
            return entity.fees;
        }
    ] as BasicListModel;
}