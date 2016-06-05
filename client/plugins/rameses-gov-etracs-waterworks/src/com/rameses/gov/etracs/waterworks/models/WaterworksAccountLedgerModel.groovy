package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

public class WaterworksAccountLedgerModel extends CrudListModel {

    def getMasterEntity() {
        return caller.entity;
    }

    void beforeQuery( def qry ) {
        qry.findBy = [parentid: caller.entity.objid];
    }

}