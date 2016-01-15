package com.rameses.clfc.signatory;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class SignatoryController extends CRUDController
{
    String serviceName = "LoanSignatoryService";
    String entityName = "signatory";

    String createFocusComponent = 'entity.name';
    String editFocusComponent = 'entity.name';                         
    String prefixId = "SIG";
    boolean showConfirmOnSave = false;

    Map createPermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'signatory.create']; 
    Map editPermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'signatory.edit']; 
    Map deletePermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'signatory.delete']; 

    def viewLogs() {
        return Inv.lookupOpener('txnlog:open', [query: [txnid: entity.objid]]); 
    }   
}