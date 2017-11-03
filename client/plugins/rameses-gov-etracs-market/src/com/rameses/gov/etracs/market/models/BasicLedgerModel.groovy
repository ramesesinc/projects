package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
import com.rameses.common.*;
import com.rameses.util.*;

/*******************************************************************************
* This class is used for Rental, Other Fees and Utilities
********************************************************************************/
public class BasicLedgerModel  {
    
    @Service("QueryService")
    def querySvc;
    
    @Caller
    def caller;
    
    @Controller
    def workunit;
    
    String ledgerSchemaName;
    String paymentSchemaName;
    
    def showOptions = [
        [ key:0, caption:'Show Only Paid Items' ],
        [ key:1, caption:'Show Only Unpaid Items' ]
    ];
    
    def showOption;
    def selectedYear;
    boolean includeVoidPayments = false;
    
    def parent;
    def selectedRental;
    String parentid = "acctid"
    
    void init() {
        parent = caller.entity;
        ledgerSchemaName = workunit.info.workunit_properties.ledgerSchemaName;
        paymentSchemaName = workunit.info.workunit_properties.paymentSchemaName;
        if(!ledgerSchemaName) throw new Exception("ledgerSchemaName is required in workunit");
        if(!paymentSchemaName) throw new Exception("paymentSchemaName is required in workunit");
        if( workunit.info.workunit_properties.parentid ) parentid = workunit.info.workunit_properties.parentid;
    }
    
    def getYearList() {
        //get the possible years
        def m = [_schemaname:ledgerSchemaName];
        m.findBy = [ (parentid): parent.objid ];
        m.select = "year";
        m.groupBy = "year";
        def yrl = [];
        def yrs = querySvc.getList(m);
        if(yrs) yrl = yrs*.year;
        return yrl;
    }
    
    def rentalListHandler = [
        fetchList : { o->
            def m = [_schemaname:ledgerSchemaName];
            m.findBy = [ (parentid): parent.objid ];
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
            def m = [_schemaname:paymentSchemaName];
            m.findBy = [ledgerid: selectedRental.objid ];
            if( includeVoidPayments == false ) {
                m.where = [" parent.voided = 0 "];
            }
            m.orderBy = "parent.refdate,parent.refno";
            return querySvc.getList(m);
        }
    ] as BasicListModel;
    
    def addEntry() {
        def h = {
            rentalListHandler.reload();
        }
        return Inv.lookupOpener( ledgerSchemaName + ":create", [parent: parent, saveHandler: h] );
    }
     
}