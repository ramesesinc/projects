package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;
import com.rameses.seti2.models.*;

public class MarketClusterModel extends CrudFormModel {

    void afterCreate() {
        entity.market = [
            objid: caller.masterEntity.objid,
            name: caller.masterEntity.name,
            barangay: caller.masterEntity.barangay
        ];    
    }
    
}