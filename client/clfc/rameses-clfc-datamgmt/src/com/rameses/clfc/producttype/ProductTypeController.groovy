package com.rameses.clfc.producttype;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class ProductTypeController extends CRUDController
{
    @Binding
    def binding;
    
    String serviceName = 'LoanProductTypeService'
    String entityName = 'product_type'

    String createFocusComponent = 'entity.name';
    String editFocusComponent = 'entity.description';                         
    boolean showConfirmOnSave = false;
    boolean allowApprove = false;
    boolean allowDelete = false;

    Map createPermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'product_type.create']; 
    Map editPermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'product_type.edit']; 
    Map deletePermission = [domain:'DATAMGMT', role:'DATAMGMT_AUTHOR', permission:'product_type.delete']; 

    def viewLogs() {
        return Inv.lookupOpener('txnlog:open', [query: [txnid: entity.name]]); 
    }     
    
    def paymentschedule;
    def paymentScheduleLookup = Inv.lookupOpener("paymentschedule:lookup", [
         onselect: { o->
             entity.paymentschedule = o.name;
             binding?.refresh('paymentschedule');
         }
    ]);
}
