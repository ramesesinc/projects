package com.rameses.clfc.migrationsupport.loanresolver

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LoanResolverController extends CRUDController
{
    @Binding
    def binding;
    
    String serviceName = 'MigrationLoanResolverService';
    String entityName = 'loanresolver';
    
    boolean allowCreate = false;
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    def prevproducttype, prevroute, prevborrower;
    def producttype, route, borrower, hasborrower = false;
    def productTypeLookup = Inv.lookupOpener('product_type:lookup', [
         onselect: { o->
             entity.producttype = o;
             binding?.refresh("entity.producttype.*|producttype");
         }
    ]);
    def routeLookup = Inv.lookupOpener('route:lookup', [
         onselect: { o->
             entity.route = o;
             binding?.refresh('entity.route.*|route');
         }   
    ]);
    def borrowerLookup = Inv.lookupOpener('borrowerresolver:lookup', [
        onselect: { o->
            if (!o.name) {
                o.name = o.lastname + ', ' + o.firstname;
                if (o.middlename) o.name += ' ' + o.middlename;
            }
            entity.borrower = o;
            binding?.refresh('borrower');
        },
        state: 'RESOLVED'
    ]);
    
    void check( data ) {
        if (data.isresolved) {
            allowEdit = false;
        } else {
            allowEdit = true;
        }
        binding?.refresh('formActions');
    }    
    
    void afterOpen( data ) {
        check(data);
        if (data.isresolved==false && data.isedited==false) {
            data.borrower = borrower;
        } else {
            borrower = data.borrower;
        }
        binding?.refresh('borrower');
    }
    
    void afterEdit( data ) {
        if (entity.producttype) {
            prevproducttype = [:];
            prevproducttype.putAll(entity.producttype);
        }
        if (entity.route) {
            prevroute = [:];
            prevroute.putAll(entity.route);
        }
        if (entity.borrower) {
            prevborrower = [:];
            prevborrower.putAll(entity.borrower);
        }
    }
    
    void afterCancel() {
        entity.producttype = [:];
        if (prevproducttype) {
            entity.producttype.putAll(prevproducttype);
        }
        entity.route = [:];
        if (prevroute) {
            entity.route.putAll(prevroute);
        }
        entity.borrower = [:]
        if (prevborrower ) {
            entity.borrower.putAll(prevborrower);
        }
        binding?.refresh('producttype|entity.(producttype|route).*|route');
    }
    
    def getPaymentMethodList() {
        return service.getPaymentMethods();
    }
    
    def getAccountStatusList() {
        return service.getAccountStates();
    }
    
    void resolve() {
        if (!MsgBox.confirm("You are about to resolve this document. Continue?")) return;
        
        entity = service.resolve(entity);
        check(entity);
    }
}

