package com.rameses.clfc.migrationsupport.borrowerresolver

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class BorrowerResolverController extends CRUDController
{
    @Binding
    def binding;
    
    String serviceName = 'MigrationBorrowerResolverService';
 
    boolean allowCreate = false;
    boolean allowDelete = false;
    boolean allowApprove = false;
    boolean allowEdit = true;
    
    @PropertyChangeListener
    def listener = [
        "civilstatus": { o->
            println 'status ' + o;
        }
    ]
    
    def civilstatus, gender, customertype;
    
    def getCivilStatusTypeList() {
        return service.getCivilStatusTypes();
    }
    
    def getGenderList() {
        return service.getGenderList();
    }
    
    def customerTypeList = [
        [caption: 'Individual', value: 'INDIVIDUAL', name: 'individual'],
        [caption: 'Juridical', value: 'JURIDICAL', name: 'juridical']
    ]
    
    void check( data ) {
        civilstatus = getCivilStatusTypeList().find{ it.key == data.maritalstatus }
        if (data.gender) gender = getGenderList().find{ it.key == data.gender }
        if (data.isresolved==true) {
            allowEdit = false;
        } else {
            allowEdit = true;
        }
        binding?.refresh('formActions|civilstatus');
    }
    
    void beforeSave( data ) {
        data.type = customertype?.value;
        //data.maritalstatus = civilstatus?.key;
        data.gender = gender?.key;
    }
    
    void afterOpen( data ) {
        check(data);
        customertype = customerTypeList.find{ it.value == data.type }
    }
    
    void resolve() {
        if (!MsgBox.confirm("You are about to resolve this document. Continue?")) return;
        
        entity = service.resolve(entity);
        check(entity);
    }
    
    def resolveLoans() {
        return Inv.lookupOpener('loanresolver:list', [borrower: entity]);
    }
    
    def getOpener() {
        if (!customertype) return null;
        
        def params = [
            entity      : entity,
            mode        : mode,
            civilstatus : civilstatus,
            gender      : gender
        ]
        return  Inv.lookupOpener('borrower' + customertype?.name + ':plugin', params);
    }
    
    /*
    @Binding
    def binding;
    
    @Service("MigrationBorrowerResolverService")
    def service;
    
    @ChangeLog
    def changeLog;
    
    boolean allowResolve = true;
    
    def entity, mode = 'read';
    
    void checkEditable( data ) {
        if (data.isresolved==true) {
            allowResolve = false;
        } else {
            allwoResolve = true;
        }
        binding?.refresh('formActions');
    }
    
    void open() {
        mode = 'read';
        entity = service.open(entity);
        checkEditable(entity);
    }
    
    void resolve() {
        mode = 'edit';
        binding?.refresh('formActions');
    }
    
    def close() {
        return '_close';
    }
    
    void cancel() {
        if (!MsgBox.confirm("Cancelling will undo the changes made. Continue?")) return;
        
        mode = 'read';
        if (changeLog.hasChanges()) {
            changeLog.undoAll();
            changeLog.clear();
        }
        binding?.refresh('formActions');
    }
    */
    
}

