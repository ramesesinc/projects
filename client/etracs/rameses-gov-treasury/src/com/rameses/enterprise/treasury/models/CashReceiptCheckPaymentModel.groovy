package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*; 
import com.rameses.util.*;
import java.rmi.server.UID

class CashReceiptCheckPaymentModel extends PageFlowController { 

    def _persistenceService;
    def _queryService;
    def _cashReceiptSvc;

    @Binding 
    def binding; 

    def entity;
    def saveHandler;
    
    //for cash payments
    def balance = 0;
    def cashtendered = 0;
    
    def fundList;
    def fund;
    def check;
    def checks = [];
    def payments = [];
    def totalcash = 0;
    def cashchange = 0;
    def openFundList;
    
    def noncashListHandler = [
        fetchList: { return payments; }, 
    ] as BasicListModel;
    
    public def getPersistenceService() {
        if ( _persistenceService == null ) {
            _persistenceService = InvokerProxy.getInstance().create("PersistenceService", null);
        }
        return _persistenceService; 
    }     
    
    public def getQueryService() {
        if ( _queryService == null ) {
            _queryService = InvokerProxy.getInstance().create("QueryService", null);
        }
        return _queryService; 
    }     
    
    public def getCashReceiptSvc() {
        if ( _cashReceiptSvc == null ) {
            _cashReceiptSvc = InvokerProxy.getInstance().create("CashReceiptService", null);
        }
        return _cashReceiptSvc; 
    }     
    
    void init() {
        check = [split:0];
        check.receivedfrom = entity.paidby;
        balance = entity.amount;
        fundList.each {it.used=0};
        if( fundList.size() == 1 ) {
            fund = fundList[0].fund;
            openFundList = [];
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
        def m = [_schemaname:'checkpayment'];
        m.where = [ " amount - amtused > 0 AND collector.objid =:collectorid", [collectorid: entity?.collector?.objid] ];
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
        check.objid = selectedCheck.objid;
        check.bank = selectedCheck.bank;
        check.refno = selectedCheck.refno;
        check.refdate = selectedCheck.refdate;
        check.receivedfrom = selectedCheck.receivedfrom;
        check.amount = selectedCheck.amount - selectedCheck.amtused;
        if(check.split == 1 ) {
            def _total = fundList.sum{ it.amount - it.used };
            if(_total < check.amount ) {
                check.amount = _total;
            }
        }
        return signal("submit");
    }
    
    def addNewCheck() {
        new_check = true;
        return signal( "submit" );
    }
    
    void saveAndAddCheck() {
        check._schemaname = 'checkpayment';
        check.state = 'PENDING';
        check.amtused = 0;
        check.external = 0;
        check.collector = entity.collector;
        if( entity.subcollector ) {
            check.subcollector = entity.subcollector;
        };
        def _total = fundList.sum{ it.amount - it.used };
        if( check.amount > _total && check.split != 1 ) {
            throw new Exception("Amount of check must be less than or equal to amount to pay for non split checks");
        }
        
        if( check.split == 1 ) {
            check = persistenceService.create( check );
        }
        else {
            check.objid = "CHKPMT"+ new UID();
            checks << check;
        }
        if( check.split ==  1 && _total < check.amount ) {
            check.amount = _total;
        }
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
        
        def numformat = new java.text.DecimalFormat('0.00'); 
        balance = new BigDecimal(numformat.format( balance )); 
        check.amount = new BigDecimal(numformat.format( check.amount )); 
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

    def addAnotherCheck() {
        new_check = true;
        check = [split:0];
        check.receivedfrom = entity.paidby;
        fund = null;
        if(openFundList.size()==1) fund = openFundList[0];
        return super.signal( "add-check" );
    }
    
    def addCashBalance() {
        return super.signal( "add-cash" );
    }
    
    def getChange() {
        if(cashtendered == 0 ) return 0;
        return cashtendered - balance;
    }
    
    def verifyCashPayment() {
        if( balance > cashtendered ) throw new Exception("There is still unpaid balance");
        totalcash = cashtendered;
        cashchange = change;
        balance = 0;
        fundList.clear();
        super.signal("submit");
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
        if(balance > 0  )
            throw new Exception("There is still balance unpaid" );
        def m = [:];
        m.paymentitems = payments;
        m.totalcash = totalcash;
        m.cashchange = cashchange;
        m.checks = checks.unique();
        
        saveHandler(m);  
        return "_close";
    }
    
    
    
} 