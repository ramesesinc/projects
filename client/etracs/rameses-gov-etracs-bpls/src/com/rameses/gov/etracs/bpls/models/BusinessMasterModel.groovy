package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class BusinessMasterModel extends CrudFormModel {

    def selectedApp;
    def getQuery() {
        return [ 
            businessid: entity.objid, 
            currentappid : entity.currentapplicationid,
            appid: selectedApp?.objid
        ];
    }
    
    public def getBarcodeFieldname() {
        return "bin";
    }
    
    /*
    @Service("BusinessMasterService")
    def service;

    def barcodeid;
    def startstep;
    
    def menuInvokers = Inv.lookup('business:showeditmenu'); 
    
    
    void reloadSection() {
       binding.refresh("subform");
    }
    
    void loadSections() {
        sections = InvokerUtil.lookupOpeners( "business:section", [entity: entity ] ).findAll {
            def vw = it.properties.visibleWhen;
            return  ((!vw)  ||  ExpressionResolver.getInstance().evalBoolean( vw, [entity:entity] ));
        }
        if( sections.size()>0 ) {
            currentSection = sections[0];
        }  
    }
    
    void reloadCurrentSection() {
        MsgBox.alert( currentSection.name );
    }
    
    boolean isAllowShowEditMenu() { 
        return ( menuInvokers ? true : false ); 
    } 

    def showEditMenu() {
        def h = { t-> 
            if (t) open(); 
            binding.refresh();
        } 
        def list = Inv.lookupOpeners( "business:change", [entity:entity, handler:h] );
        def pop = new PopupMenuOpener();
        list.each {
            pop.add( it );
        }
        return pop;
    }
    
    
    def createSMS() {
        def phoneno = entity.mobileno;
        if(!phoneno) phoneno = entity.owner?.mobileno;
        return Inv.lookupOpener('business_sms:create', [phoneno: phoneno]); 
    }
    */
}