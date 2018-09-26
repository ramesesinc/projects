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
    
    @Service("AFControlService")
    def afControlSvc;    
    
    def modeList = ["ONLINE", "OFFLINE"];
    def collectionTypes;
    
    def mode = "ONLINE";
    def afType;
    def collectionType;
    
    boolean subcollectorMode;
    
    String title = "Cash Receipt Initial (Select Type of Collection)"
    
    def getAfTypeList() {
        return collectionTypes.getAfTypes();
    }

    def getCollectionTypeList() {
        return collectionTypes.getCollectionTypes();
    }
    
    @PropertyChangeListener
    def listener = [
        "mode" : { o->
            collectionTypes.mode = o;
            afType = null;
            collectionType = null;
            collectionTypes.afType = null;
        },
        "afType" : { o->
            collectionType = null;
            collectionTypes.afType = o;
        }
    ];
    
    void init() {
        collectionTypes = ManagedObjects.instance.create( CollectionTypeListUtil.class );
        collectionTypes.mode = mode;
    }
    
    void initSubCollector() {
        subcollectorMode = true; 
        init();
    }
    
    def lookupCollectorAF( param, boolean sub ) { 
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
        def strc = "cashreceipt:select-af";
        if( sub == true ) {
            strc = "cashreceipt:select-af:subcollector";
        }
        Modal.show(strc, p ); 
        return selAF;
    }
    
    def initReceipt(def entity) {
        def af = null ;
        if ( !subcollectorMode ) {
            af = lookupCollectorAF( entity, false ); 
        } else {
            af = lookupCollectorAF( entity, true ); 
        }
        if ( af == null ) return null; 
        return cashReceiptSvc.init( entity );
    }
    
    def doNext() {
        collectionTypes.checkHasItems( collectionType );
        def params = [
            txnmode         : mode, 
            formno          : afType, 
            formtype        : collectionType.af.formtype, 
            collectiontype  : collectionType 
        ]; 
        
        def entity = [:];
        entity.putAll(params); 
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
        def ch = {
            def newEntity = [:];
            newEntity.putAll( params );
            return initReceipt( newEntity );
        }
        def opener = Inv.lookupOpener("cashreceipt:"+ collectionType.handler, [entity: info,createHandler:ch]);  
        if(!opener )
            throw new Exception('No available handler found');
        opener.target = "self";
        return opener;
    }    
    
    def loadBarcode() {
        def h = { o->
            def e = [
                txnmode         : "ONLINE", 
                formno          : o.collectiontype.formno, 
                formtype        : o.collectiontype.af.formtype, 
                collectiontype  : o.collectiontype 
            ];   
            def op = null;
            if ( o._paymentorderid ) {
                def info = initReceipt( e );
                info.payer = o.info.payer;
                info.paidby = o.info.paidby;
                info.paidbyaddress = o.info.paidbyaddress;
                info.amount = o.info.amount;
                info.items = o.info.items.collect{ [item: it.item, amount:it.amount, remarks:it.remarks ] };
                info.remarks = o.info.particulars;
                op = Inv.lookupOpener("cashreceipt:"+ e.collectiontype.handler, [entity: info, _paymentorderid:o._paymentorderid ]);
            }
            else {
                def ee = initReceipt( e );
                def pp = [:];
                pp.info = o.info;
                pp.entity = ee;
                pp.barcodeid = o.barcodeid;
                op = Inv.lookupOpener("cashreceipt:barcode:"+ o.prefix, pp );
            }
            binding.fireNavigation( op ); 
        }
        return Inv.lookupOpener( "cashreceipt_barcode", [handler: h] );
    }
}    