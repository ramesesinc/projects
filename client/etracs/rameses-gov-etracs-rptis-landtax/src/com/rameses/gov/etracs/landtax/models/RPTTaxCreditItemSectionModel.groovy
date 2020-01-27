package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class RPTTaxCreditItemSectionModel extends CrudListModel {

    def getMasterEntity() {
        return caller?.entity;
    }
    
    def getCustomFilter() {
        def params = [
            ledgerid: masterEntity?.rptledger.objid,
            creditid: masterEntity?.objid,
        ]
        return ["refid = :ledgerid AND receiptid = :creditid", params];
    }    
}