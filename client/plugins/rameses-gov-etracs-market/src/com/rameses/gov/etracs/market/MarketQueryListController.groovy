package com.rameses.gov.etracs.market;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class MarketQuery extends DefaultListController {

    @Service("MarketService")
    def mktSvc;

    def marketList;
    def market;

    @PropertyChangeListener
    def listener = [
        "market": { o->
            query.marketid = o?.objid;
            reload();
        }
    ]

    void init() {
        marketList = mktSvc.getList([:]);
    }

}