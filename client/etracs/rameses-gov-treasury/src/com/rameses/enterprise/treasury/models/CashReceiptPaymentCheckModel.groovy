package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*; 
import com.rameses.util.*;

class CashReceiptPaymentCheckModel extends PageFlowController { 

    
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
    
    def balance = 0;
    def fundList;
    def fund;
    def check;
    def checks = [];
    def payments = [];
    def totalcash = 0;
    def openFundList;
    
    def noncashListHandler = [
        fetchList: { return payments; }, 
    ] as BasicListModel;
    
    void init() {
        check = [split:0];
        check.receivedfrom = entity.paidby;
        balance = entity.amount;
        fundList.each {it.used=0};
        if( fundList.size() == 1 ) {
            fund = fundList[0].fund;
        }
        else {
            openFundList = fundList*.fund;
        }
    } 
    
    boolean check_exists = false;
    boolean new_check = false;
    def selectionList;
    def selectedCheck;
    void searchCheckIfExists() {
        new_check = true;
        def m = [_schemaname:'paymentcheck'];
        m.findBy = [refno: check.refno ];
        m.where = [ " amount - amtused > 0 " ];
        selectionList = queryService.getList( m );
        if(selectionList ) {
            check_exists = true;
            checkSelectionModel.reload();
        }
    }
    
    def checkSelectionModel = [
        fetchList: { o->
            return selectionList;
        }
    ] as BasicListModel;
    
    
    def useExistingCheck() {
        if(!selectedCheck ) throw new Exception("Please select an unused check from list");
        new_check = false;
        check.putAll(selectedCheck);
        return signal("submit");
    }
    
    def addNewCheck() {
        new_check = true;
        return signal( "submit" );
    }
    
    void saveAndAddCheck() {
        check._schemaname = 'paymentcheck';
        check.state = 'PENDING';
        check.amtused = 0;
        check = persistenceService.create( check );
        addCheck();
    }
    
    void addCheckEntry( def vfund, def vamt  ) {
        //check the amount must not be greater than the allocated fund.
        def entry = fundList.find{ it.fund == vfund };
        if( entry.used + vamt > entry.amount )
            throw new Exception("Amount is greater than the amount of " + entry.fund.title );
        
        def m = [:];
        m.refid = check.objid;
        m.reftype =  "CHECK"; 
        m.check =  check;
        m.amount = vamt;
        m.particulars = check.refno + " (" + check.bank.name + ") dated " + check.refdate; 
        m.refno = check.refno;
        m.refdate = check.refdate;
        m.fund = vfund;
        payments << m;
        
        //update fundList immediately
        entry.used += vamt;
        openFundList = fundList.findAll{ (it.amount - it.used) > 0 }*.fund;
    }
    
    void addCheck() {
        if ( !check.bank ) throw new Exception('Please specify a bank'); 
        if( check.amount==null || check.amount <= 0 ) 
            throw new Exception("Amount must be greater than 0.0 ");
        if(balance - check.amount < 0 )
            throw new Exception("Check amount must be greater than the balance unpaid");
        cashReceiptSvc.validateCheckDate( check.refdate );
        
        if( fund == null ) {
            //split the check
            def _list = fundList.findAll{ (it.amount - it.used)>0 };
            def _total = _list.sum{ it.amount - it.used };
            _list.each { fa->
               def _amt = NumberUtil.round( ((fa.amount - fa.used)/ _total)*check.amount );
               addCheckEntry( fa.fund, _amt );
            }
        } 
        else {
            addCheckEntry( fund, check.amount );
        }
        
        //update balance
        balance = balance - check.amount;
    }

    void initCheck() {
        check = [:];
        fund = null;
        if(openFundList.size()==1) fund = openFundList[0];
    }
    
    
    
    def addAnotherCheck() {
        return super.signal( "add-check" );
    }
    
    def addCashBalance() {
        return super.signal( "add-cash" );
    }
    
    void addCash() {
        boolean pass= false;
        def p = [:];
        p.title = "Additional Cash";
        p.value = balance;
        p.handler = { o->
            if(o!=balance)
                throw new Exception("Cash must equal the balance due");
            totalcash = o;
            balance = 0;
            fundList.clear();
            pass = true;
        }
        Modal.show( "decimal:prompt", p );
        if(!pass) throw new BreakException();
    }
    
    //This will add adjusting entries for the fund just in case there are balances due;
    void correctNoncashPayment() {
        fundList.findAll{ it.amount - it.used !=0 }.each { o->
            def _amt = o.amount - o.used;
            int i = (_amt <0)? 0: 1;
            
            def m = [:];
            m.reftype =  (_amt <0) ? 'FTOUT' : 'FTIN'; 
            m.amount = _amt;
            m.particulars = 'FUND TRANSFER '; 
            m.fund = o.fund;
            payments << m;
        }
    }
    
    def saveCheck() {
        if(balance != 0  )
            throw new Exception("Amount paid must be exact " );
        def m = [:];
        m.paymentitems = payments;
        m.totalcash = totalcash;
        saveHandler(m);  
        return "_close";
    }
    
    
    
} 