package com.rameses.gov.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.enterprise.treasury.models.*;

class CashTicketInitialModel  {

    @Binding
    def binding;

    @Service("QueryService")
    def qryService;
    
    @Service("CashReceiptService")
    def cashReceiptSvc;
    
    @Service("AFControlService")
    def afControlSvc;    

    String title = "Cash Ticket Initial (Select Type of Collection)"
    
    def afType;
    def afTypeList;
    def collectionTypes;
    def modeList = ["ONLINE", "OFFLINE"];

    def mode = "ONLINE";
    def collectionType;
    def allCollectionTypes;
    
    boolean subcollectorMode;
    
    void loadCollectionTypes() {
        def arr = [];
        arr << " af.formtype = 'cashticket' ";

        def parm = [:];    
        if( OsirisContext.env.ORGROOT == 1 ) {
            arr << "orgid IS NULL"
        } else { 
            arr << "orgid = :orgid";
            parm.orgid = OsirisContext.env.ORGID;
        }
        
        if( mode == "ONLINE") {
            arr << " allowonline = 1";
        }
        else {
            arr << " allowoffline = 1";
        }
        
        def m = [_schemaname: "vw_collectiontype"];
        m.where = [arr.join(" AND "), parm];
        m.orderBy = "formno,title,sortorder";
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
    
    private void init0() {
        collectionTypes = ManagedObjects.instance.create( CollectionTypeListUtil.class );
        collectionTypes.formType = 'cashticket'; 
    }
    
    void init() {
        init0();
        loadCollectionTypes();
    }
    
    void initSubCollector() {
        subcollectorMode = true; 
        init0();
        loadCollectionTypes();
    }
    
    def lookupCollectorAF( param ) { 
        param.active = 1;
        def env = OsirisContext.env; 
        def p = [_schemaname: 'af_control']; 
        p.where = CashReceiptAFLookupFilter.getFilter( param ); 
        def res = qryService.findFirst( p ); 
        if ( res ) return res; 
        
        param.active = 0; 
        p = [ entity: param ]; 
        
        def selAF = null; 
        p.onselect = { o-> 
            afControlSvc.activateSelectedControl([ objid: o.objid ]);
            selAF = o;             
        }
        Modal.show('cashreceipt:select-af', p ); 
        return selAF;
    }
    
    def lookupSubCollectorAF( param ) {
        param.active = 1; 

        def env = OsirisContext.env; 
        def p = [_schemaname: 'af_control', debug: true]; 
        p.where = CashReceiptAFLookupFilter.getFilter( param ); 
        def res = qryService.findFirst( p ); 
        if ( res ) return res; 

        param.active = 0; 
        p = [ entity: param ]; 
        
        def selAF = null; 
        p.onselect = { o-> 
            afControlSvc.activateSelectedControl([ objid: o.objid ]);
            selAF = o;             
        }
        Modal.show('cashreceipt:select-af:subcollector', p ); 
        return selAF;
    }
    
    def initReceipt(def entity) {
        def af = null ;
        if ( !subcollectorMode ) {
            af = lookupCollectorAF( entity ); 
        } else {
            af = lookupSubCollectorAF( entity ); 
        }
        
        if ( af == null ) return null; 
        return cashReceiptSvc.init( entity );
    }
    
    def doNext() {
        collectionTypes.checkHasItems( collectionType );
        def entity = [
            txnmode         : mode, 
            formno          : afType, 
            formtype        : collectionType.af.formtype, 
            collectiontype  : collectionType 
        ]; 

        def info = initReceipt( entity );
        if( info == null ) return null;
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
        def opener = Inv.lookupOpener("cashreceipt:"+ collectionType.handler, [entity: info]);  
        if ( !opener ) throw new Exception('No available handler found'); 
        
        opener.target = "self";
        return opener;
    } 
}    