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
        } else { 
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
    
    boolean subcollectorMode;
    
    void init() {
        //MsgBox.alert( "ctx " + OsirisContext.env.ORGID + " is root? " + OsirisContext.env.ORGROOT );
        loadCollectionTypes();
    }
    
    void initSubCollector() {
        subcollectorMode = true; 
        loadCollectionTypes();
    }
    
    def lookupCollectorAF( param, def role ) { 
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
        if( role == "subcollector" ) {
            strc = "cashreceipt:select-af:subcollector";
        }
        Modal.show(strc, p ); 
        return selAF;
    }
    
    /*
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
    */
   
    def initReceipt(def entity) {
        def af = null ;
        if ( !subcollectorMode ) {
            af = lookupCollectorAF( entity, null ); 
        } else {
            af = lookupCollectorAF( entity, "subcollector" ); 
        }
        
        if ( af == null ) return null; 
        return cashReceiptSvc.init( entity );
    }
    
    def doNext() {
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