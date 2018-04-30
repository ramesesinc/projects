package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

class CashReceiptInitialModel  {

    @Binding
    def binding;

    @Service("QueryService")
    def qryService;
    
    @Service("CashReceiptService")
    def cashReceiptSvc;
    
    
    def modeList = ["ONLINE", "OFFLINE"];
    def afTypeList;
    
    def mode = "ONLINE";
    def afType;
    def collectionType;
    def allCollectionTypes;
    
    String title = "Cash Receipt Initial (Select Type of Collection)"
    
    void loadCollectionTypes() {
        def arr = [];
        def parm = [:];    
        if( OsirisContext.env.ORGROOT == 1 ) {
            arr << "org.objid IS NULL"
        }
        else {
            arr << "org.objid = :orgid";
            parm.orgid = OsirisContext.env.ORGID;
        }
        arr << " af.formtype = 'serial' ";
        if( mode == "ONLINE") {
            arr << " allowonline = 1";
        }
        else {
            arr << " allowoffline = 1";
        }
        def m = [_schemaname: "vw_collectiontype_org"];
        m.where = [arr.join(" AND "), parm];
        m.orderBy = "sortorder,title";
        allCollectionTypes = qryService.getList( m );
        afTypeList = allCollectionTypes*.formno.unique();
    }
    
    def getCollectionTypeList() {
        if( !afType ) return allCollectionTypes;
        return allCollectionTypes.findAll{ it.formno == afType }; 
    }
    
    @PropertyChangeListener
    def listener = [
        "mode" : { o->
            afType = null;
            collectionType = null;
            loadCollectionTypes();
        }
    ]
    
    void init() {
        //MsgBox.alert( "ctx " + OsirisContext.env.ORGID + " is root? " + OsirisContext.env.ORGROOT );
        loadCollectionTypes();
    }
    
    def doNext() {
        def entity = [
            txnmode         : mode, 
            formno          : afType, 
            formtype        : collectionType.af.formtype, 
            collectiontype  : collectionType 
        ]; 
        try { 
            
            def info = cashReceiptSvc.init( entity );
            
            if( mode == "OFFLINE" ) {
                boolean pass = false;
                Modal.show( "date:prompt", [ entity  : [date: info.receiptdate], 
                    handler : {v-> 
                        info.receiptdate = v; 
                        pass = true;
                    }
                ]);
                if ( !pass ) return null;
            }
            
            //
            def openerParams = [entity: info]; 
            openerParams.createHandler = {
                def op = findOpener( info ); 
                if ( !op ) return;
                binding.fireNavigation( op ); 
            };
            
            def opener = Inv.lookupOpener("cashreceipt:"+ collectionType.handler, openerParams);  
            if(!opener )
                throw new Exception('No available handler found');
            opener.target = "self";
            return opener;
        } 
        catch(BreakException be) { 
            return null;
        } catch(Warning w) { 
            String m = "cashreceipt:" + w.message;
            Modal.show(m, [entity: entity]);
            return null;
        }
    }
    
}    