package com.rameses.clfc.borrower;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.util.*;

class BorrowerEducationController
{
    //feed by the caller
    def borrowerContext;
    
    @Binding
    def binding;
    
    def htmlBuilder = new BorrowerInfoHtmlBuilder(); 
    
    void init() {
        borrowerContext.addDataChangeHandler('education', {educationHandler.reload()});
    }    
    
    def selectedEducation;
    def educationHandler = [
        fetchList: {o->
            if( !borrowerContext.borrower.educations ) borrowerContext.borrower.educations = []
            borrowerContext.borrower.educations.each{ it._filetype = "education" }
            return borrowerContext.borrower.educations;
        },
        onRemoveItem: {o->
            return removeItemImpl(o); 
        },
        getOpenerParams: {o->
            return [ mode: borrowerContext.mode, caller:this ]
        }
    ] as EditorListModel;
    
    def addEducation() {
        def handler = {education->
            education.borrowerid = borrowerContext.borrower?.objid;
            borrowerContext.borrower.educations.add(education);
            educationHandler.reload();
        }
        return InvokerUtil.lookupOpener("education:create", [handler:handler]);
    }    
    
    void removeEducation() {
        removeItemImpl(selectedEducation);
    }
            
    boolean removeItemImpl(o) {
        if (borrowerContext.mode == 'read') return false;
        if (MsgBox.confirm("You are about to remove this item. Continue?")) {
            borrowerContext.borrower.educations.remove(o);
            return true;
        } else { 
            return false; 
        } 
    }
    
    def getHtmlview() {
        return htmlBuilder.buildEducation(selectedEducation);
    }
}