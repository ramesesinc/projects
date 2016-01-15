package com.rameses.clfc.borrower;

class BorrowerContext
{
    Map dataChangeHandlers = [:];
    Map beforeSaveHandlers = [:];
    
    private def parentCodeBean;
    private def codeBean;
    private def service;
    private def loanapp;
    
    BorrowerContext(parentCodeBean, codeBean, service, loanapp) {
        this.parentCodeBean = parentCodeBean;
        this.codeBean = codeBean;
        this.service = service;
        this.loanapp = loanapp;
    }

    public void addDataChangeHandler(name, handler) {
        if (handler != null) dataChangeHandlers.put(name, handler);
    }
    
    public void addBeforeSaveHandler(name, handler) {
        if (handler != null) beforeSaveHandlers.put(name, handler);
    }
    
    public def getCaller() { return codeBean; }
    public def getService() { return service; } 
    public def getLoanapp() { return loanapp; } 
    public def getMode() { return parentCodeBean.mode; } 
    public def getBorrower() { return loanapp.borrower; } 
    
    public def getLoanappid() { 
        return loanapp.objid; 
    } 
    
    public def openBorrower( params ) {
        return service.openBorrower(params); 
    }
    
    public void refresh() {
        caller.tabHandler.refresh(); 
    } 
}
