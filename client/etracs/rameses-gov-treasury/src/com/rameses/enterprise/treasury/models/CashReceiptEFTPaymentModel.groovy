package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*; 
import com.rameses.util.*;
import java.rmi.server.*;

class CashReceiptEFTPaymentModel  { 

    @Binding 
    def binding; 

    @Service("PersistenceService")
    def persistenceService;
    
    @Service("QueryService")
    def queryService;
    
    @Service("CashReceiptService")
    def cashReceiptSvc;

    def entity;
    def saveHandler;
    def eft;
    
    def balance = 0;
    def fundList;
    def fund;
    
    @PropertyChangeListener
    def listener = [
        "eft.bankaccount" : { o->
            eft.fund = o.fund;
            eft.fundid = o.fund.objid;
        }
    ]
    
    def fundListHandler = [
        fetchList: { o->
            return fundList;
        }
    ] as BasicListModel;
    
    void init() {
        eft = [objid:'EFT' + new UID()];
        eft.receivedfrom = entity.paidby;
        eft.amount = entity.amount;
        balance = entity.amount;
    } 
    
    def doOk() {
        def payments = [];
        fundList.each {
            def m = [:];
            m.item = eft;
            m.refid = eft.objid;
            m.reftype =  "EFT"; 
            m.amount = it.amount;
            m.particulars = eft.refno + " (" + eft.bankaccount.code + " - " + eft.bankaccount.title + ") dated " + eft.refdate; 
            m.refno = eft.refno;
            m.refdate = eft.refdate;
            m.fund = it.fund;
            payments << m;
        }
        eft.paymentitems = payments;
        saveHandler( eft );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
} 