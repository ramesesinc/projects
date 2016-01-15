package com.rameses.clfc.migrationsupport.borrowerresolver

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class BorrowerResolvedController 
{
    @Binding
    def binding;
    
    @ChangeLog
    def changeLog;
    
    @Service('MigrationBorrowerResolverService')
    def service;
    
    @Service('MigrationLoanResolverService')
    def loanSvc;
    
    def borrower, entity, loan, producttype, route;
    def selectedState, selectedLoan, mode = 'read';
    def prevproducttype, prevroute;
    
    void open() {
        borrower = service.open(entity);
    }
    
    def close() {
        return '_close';
    }
    
    def statesHandler = [
        getItems: {
            return loanSvc.getStates();
        }, 
        onselect: { o->
            loansHandler?.reloadAll();
        }
    ] as ListPaneModel;
    
    
    def loansHandler = [
        getRows: { return 15; },
        getColumns: { 
            def o = [
                borrowerid  : borrower?.objid,
                state       : selectedState?.state
            ];
            return loanSvc.getColumns(o);
        }, 
        fetchList: { o->
            o.borrowerid = borrower?.objid;
            o.state = selectedState?.state;
            return loanSvc.getList(o);
        }
    ] as PageListModel;    
    
    void moveFirstPage() {
        loansHandler.moveFirstPage();
        binding?.refresh('pagecount');
    }

    void moveBackPage() {
        loansHandler.moveBackPage();
        binding?.refresh('pagecount');
    }

    void moveNextPage() {
        loansHandler.moveNextPage();
        binding?.refresh('pagecount');
    }
    
    void refreshLoanList() {
        loansHandler?.reload();
    }
    
    void setSelectedLoan( selectedLoan ) {
        this.selectedLoan = selectedLoan;
        if (mode != 'read') return;
            
        loan = null;
        if (selectedLoan) {
            loan = loanSvc.open([objid: selectedLoan.objid]);
            loan.borrower = borrower;
        } else {
            clearFields();
        }
        binding?.refresh();
    }
    
    void clearFields() {
        loan = null;
        route = null;
        producttype = null
    }
        
    def productTypeLookup = Inv.lookupOpener('product_type:lookup', [
         onselect: { o->
             loan.producttype = o;
             binding?.refresh("loan.producttype.*|producttype");
         }
    ]);
    def routeLookup = Inv.lookupOpener('route:lookup', [
         onselect: { o->
             loan.route = o;
             binding?.refresh('loan.route.*|route');
         }   
    ]);    
    
    def getPaymentMethodList() {
        return loanSvc.getPaymentMethods();
    }
    
    def getAccountStatusList() {
        return loanSvc.getAccountStates();
    }
    
    def getLoanTypeList() {
        return loanSvc.getLoanTypes();
    }
    
    void editLoan() {
        mode = 'edit';
        if (loan.producttype) {
            prevproducttype = [:];
            prevproducttype.putAll(loan.producttype);
        }
        if (loan.route) {
            prevroute = [:];
            prevroute.putAll(loan.route);
        }
    }
    
    void cancelLoan() {
        if (!MsgBox.confirm("You are about to cancel changes made. Continue?")) return;
        
        mode =  'read';
        if (changeLog.hasChanges()) {
            changeLog.undoAll();
            changeLog.clear();
        }
        loan.producttype = [:];
        if (prevproducttype) {
            loan.producttype.putAll(prevproducttype);
        }
        loan.route = [:];
        if (prevroute) {
            loan.route.putAll(prevroute);
        }
        binding?.refresh('producttype|loan.*|route');
    }
    
    void saveLoan() {
        if (!MsgBox.confirm("You are about to save this document. Continue?")) return;
        
        loan = loanSvc.update(loan);
        mode = 'read'
        binding.refresh();
        loansHandler?.reload();
        MsgBox.alert("Successfully updated document.");
    }
    
    void resolveLoan() {
        if (!MsgBox.confirm("You are about to resolve this loan. Continue?")) return;
        
        loan = loanSvc.resolve(loan);
        binding?.refresh();
        loansHandler?.reload();
        MsgBox.alert("Successfully resolve loan.");
    }
    
    def getAllowResolve() {
        def flag = false;
        
        def isresolver = (ClientContext.currentContext.headers.ROLES.containsKey('MIGRATION_SUPPORT.RESOLVER')? true : false);
        if (mode=='read' && loan!=null && loan.isedited == true && loan.isresolved == false && isresolver == true) {
            flag = true;
        }
        
        return flag;
    }
}

