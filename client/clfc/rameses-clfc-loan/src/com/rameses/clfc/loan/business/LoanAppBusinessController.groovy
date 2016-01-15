package com.rameses.clfc.loan.business;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

class LoanAppBusinessController 
{
    //feed by the caller
    def loanapp, caller, selectedMenu;
    
    @Binding
    def binding;
    
    @Service('LoanAppBusinessService') 
    def service; 
    
    def htmlBuilder = new BusinessHtmlBuilder(); 
    def businesses = [];
        
    void init() {
        selectedMenu.saveHandler = { save(); }
        selectedMenu.dataChangeHandler = { dataChange(); }
        def data = service.open([objid: loanapp.objid]);
        loanapp.clear();
        loanapp.putAll(data);
        businesses = loanapp.businesses;
    }
    
    void save() {
        if(loanapp.state == 'FOR_INSPECTION') {
            def business = businesses.find{ it.ci?.evaluation == null }
            if( business ) throw new Exception("CI Report for business $business.tradename is required.");
        }
        
        def data = [objid: loanapp.objid, businesses: businesses]
        loanapp.state = service.update(data).state;
    }

    void dataChange() {
        loanapp.businesses = businesses;
    }
    
    def selectedBusiness;
    def businessHandler = [
        fetchList: {o->
            if( !businesses ) businesses = []
            businesses.each{ it._filetype = "business" }
            return businesses;
        },
        onRemoveItem: {o->
            return removeBusinessImpl(o);
        },
        getOpenerParams: {o->
            return [mode: caller.mode, caller:this]
        }
    ] as EditorListModel;
    
    def addBusiness() {
        def handler = {business->
            business.parentid = loanapp.objid;
            businesses.add(business);
            businessHandler.reload();
        }
        return InvokerUtil.lookupOpener("business:create", [handler:handler]);
    }   
        
    void removeBusiness() {
        removeBusinessImpl(selectedBusiness); 
    }
    
    boolean removeBusinessImpl(o) {
        if (caller.mode == 'read') return false;
        if (MsgBox.confirm("You are about to remove this business. Continue?")) {
            businesses.remove(o);
            return true;
        } else { 
            return false; 
        } 
    }      

    def addCiReport() {
        if( !selectedBusiness ) {
            MsgBox.alert("No business selected.");
            return null;
        }
        def params = [
            handler: { selectedBusiness.ci = it },
            mode: caller.mode,
            entity: [ filedby: OsirisContext.env.USER ],
            caller: this
        ]
        if(selectedBusiness.ci?.evaluation) params.entity = selectedBusiness.ci;
        return InvokerUtil.lookupOpener("cireport:edit", params);
    }
    
    def getHtmlview() { 
        return htmlBuilder.buildBusiness(selectedBusiness);
    } 
}
