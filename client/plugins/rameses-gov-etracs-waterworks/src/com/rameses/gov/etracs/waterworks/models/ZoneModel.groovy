package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.util.*;

public class ZoneModel extends CrudFormModel {
    
    public void afterCreate() {
        entity.sectorid = caller.masterEntity?.objid;
    }

}