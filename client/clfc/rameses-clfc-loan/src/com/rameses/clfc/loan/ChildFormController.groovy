package com.rameses.clfc.loan;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;
import com.rameses.clfc.loan.controller.*;

class ChildFormController extends PopupMasterController
{
    def selectedEmployment;
    def selectedOtherIncome;
    def employments = [];
    def otherincomes = [];
    
    public def createEntity() {
        return [ objid:'BCH'+new UID() ];
    }
    
    void open() {
        if(!entity.employments) entity.employments = [];
        employments = entity.employments;
        entity.employments = [];
        entity.employments.addAll(employments);
        
        if(!entity.otherincomes) entity.otherincomes = [];
        otherincomes = entity.otherincomes;
        entity.otherincomes = [];
        entity.otherincomes.addAll(otherincomes);        
    }
    
    public def doOk() {
        employments.clear();
        employments.addAll(entity.employments);
        entity.employments = employments;
        
        otherincomes.clear();
        otherincomes.addAll(entity.otherincomes);
        entity.otherincomes = otherincomes;
        
        super.doOk();
    }

    public def doCancel() {
        entity.employments = employments;
        entity.otherincomes = otherincomes;
        super.doCancel();
    }
    
    def employmentHandler = [
        fetchList: {o->
            if( !entity.employments ) entity.employments = []
            entity.employments.each{ it._filetype = 'employment' }
            return entity.employments;
        },
        onRemoveItem: {o->
            return removeEmploymentImpl(o);
        },
        getOpenerParams: {o->
            return [mode: mode]
        }
    ] as EditorListModel;

    def addEmployment() {
        def handler = {employment->
            employment.refid = entity.objid;
            entity.employments.add(employment);
            employmentHandler.reload();
        }
        return InvokerUtil.lookupOpener("employment:create", [handler:handler]);
    }
    
    void removeEmployment() {
        removeEmploymentImpl(selectedEmployment);
    }
    
    boolean removeEmploymentImpl(o) {
        if(mode == 'read') return false;
        if(MsgBox.confirm("You are about to remove this employment. Continue?")) {
            entity.employments.remove(o);
            return true;
        }
        return false;
    }

    def otherIncomeHandler = [
        fetchList: {o->
            if( !entity.otherincomes ) entity.otherincomes = []
            entity.otherincomes.each{ it._filetype = "otherincome" }
            return entity.otherincomes
        },
        onRemoveItem: {o->
            return removeOtherIncomeImpl(o);
        },
        getOpenerParams: {o->
            return [mode: mode]
        }
    ] as EditorListModel;

    def addOtherIncome() {
        def handler = {otherincome->
            otherincome.refid = entity.objid;
            entity.otherincomes.add(otherincome);
            otherIncomeHandler.reload();
        }
        return InvokerUtil.lookupOpener("otherincome:create", [handler:handler]);
    }
    
    void removeOtherIncome() {
        removeOtherIncomeImpl(selectedOtherIncome);
    }
    
    boolean removeOtherIncomeImpl(o) {
        if(mode == 'read') return false;
        if(MsgBox.confirm("You are about to remove this source of income. Continue?")) {
            entity.otherincomes.remove(o);
            return true;
        }
        return false;
    }
}
