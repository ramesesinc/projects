package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
import com.rameses.common.*;
import com.rameses.util.*;

public class MarketRentalLedgerModel  {
    
    @Service("QueryService")
    def querySvc;
    
    @Caller
    def caller;
    
    def showOptions = [
        [ key:0, caption:'Show Only Paid Items' ],
        [ key:1, caption:'Show Only Unpaid Items' ]
    ];
    def yearList;
    def showOption;
    def selectedYear;
    boolean includeVoidPayments = false;
    
    def parent;
    def selectedRental;
    
    void init() {
        parent = caller.entity;
        //get the possible years
        def m = [_schemaname:"market_rental_ledger"];
        m.findBy = [acctid: parent.objid ];
        m.select = "year";
        m.groupBy = "year";
        def yrs = querySvc.getList(m);
        if(yrs) yearList = yrs*.year;
    }
    
    def rentalListHandler = [
        fetchList : { o->
            def m = [_schemaname:"market_rental_ledger"];
            m.findBy = [acctid: parent.objid ];
            def cond = [];
            def parm = [:];
            if( selectedYear ) {
                cond << "year = :year";
                parm.year = selectedYear;
            }
            if( showOption != null ) {
                if( showOption.key == 0 ) {
                    cond << " amount-amtpaid = 0 ";
                }
                else {
                    cond << " amount-amtpaid > 0 ";
                }
            }
            if(cond ) {
                m.where = [cond.join(" AND "), parm];
            }
            m.orderBy = "year,month";
            return querySvc.getList(m);
        }
    ] as BasicListModel;
    
    def paymentListHandler = [
        fetchList: { o->
            if(!selectedRental ) return [];
            def m = [_schemaname:"market_payment_rental"];
            m.findBy = [ledgerid: selectedRental.objid ];
            if( includeVoidPayments == false ) {
                m.where = [" parent.voided = 0 "];
            }
            m.orderBy = "fromdate";
            return querySvc.getList(m);
        }
    ] as BasicListModel;
    
}