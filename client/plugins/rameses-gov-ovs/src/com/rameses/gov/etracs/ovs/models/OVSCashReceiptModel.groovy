package com.rameses.gov.etracs.ovs.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;

import com.rameses.treasury.common.models.*;

public class OVSCashReceipt extends BasicBillingCashReceiptModel {
    
    //we use person bec. entity is already taken
    def person;
    
    def ticketno;
    def searchoption;

    void findTxn() {
        query.clear();
        query.searchoption = searchoption;
        if(searchoption=='entity') {
            query.entity = person;
        } 
        else {
            query.ticketno = ticketno;
        }
        loadInfo([action: "initial"]);
    }
    
}