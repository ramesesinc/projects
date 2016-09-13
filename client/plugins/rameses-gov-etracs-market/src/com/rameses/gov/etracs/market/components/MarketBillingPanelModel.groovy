package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;
import com.rameses.seti2.models.*;

public class MarketBillingPanelModel {

    @Service("MarketBillingService")
    def billingSvc;
    
    void reload() {
        listModel.reload();
    }
    
    def listModel = [
        fetchList: { o->
            def val = getValue();
            def m = [:];
            m.acctid = val.objid;
            return billingService.getList(m);
        }
    ] as BasicListModel;
   
}