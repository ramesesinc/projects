package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

class BatchCaptureCollectionInitialModel  {

    @Binding
    def binding;

    @Service("QueryService")
    def qryService;
    
    @Service('DateService')
    def dateSvc; 
    
    @Service("BatchCaptureCollectionService")
    def batchSvc;
    
    def modeList = ["CAPTURE"];
    def afTypeList;
    
    def mode = "CAPTURE";
    def afType;
    def collectionType;
    def allCollectionTypes;
    
    String title = "Batch Cash Receipt Initial (Select Type of Collection)"
    
    @SubWindow
    def win;
    
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
        arr << " allowbatch = 1";

        def m = [_schemaname: "vw_collectiontype"];
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
    
    def lookupCollectorAF( param, boolean sub ) { 
        def p = [ entity: param ]; 
        def selAF = null; 
        p.onselect = { o-> 
            selAF = o;             
        }
        def s = "cashreceipt:select-af";
        if(sub == true) s = "cashreceipt:select-af:subcollector";
        Modal.show( s, p ); 
        return selAF;
    }
    
    def doNext() {
        def entity = [
            txnmode         : mode, 
            formno          : afType, 
            formtype        : collectionType.af.formtype, 
            collectiontype  : collectionType 
        ]; 

        def pass = false;
        def p = [:];
        p.fields = [
            [name: 'startseries', caption:'Start Series', type:'integer', required:true], 
            [name: 'receiptdate', caption:'Start Date', type:'date', required:true]
        ];
        p.data = [ receiptdate : dateSvc.getBasicServerDate() ]; 
        p.handler = { o-> 
            entity.putAll( o );  
            pass = true;
        }
        Modal.show( "dynamic:form", p, [title: 'Start Receipt Information'] );
        if ( !pass ) return null;
        
        def afc = null ;
        if ( !subcollectorMode ) {
            afc = lookupCollectorAF( entity, false ); 
        } else {
            afc = lookupCollectorAF( entity, true ); 
        }
        if ( afc == null ) return null; 
        
        println '** afc '
        afc.each{
            println '> '+ it;
        }
        
        entity.defaultreceiptdate = entity.receiptdate;        
        entity.stub = afc.stubno;
        entity.collector = afc.owner;
        entity.controlid = afc.objid; 
        entity.endseries = afc.endseries; 
        entity.prefix = afc.prefix; 
        entity.suffix = afc.suffix; 
        entity.serieslength = afc.af.serieslength; 
        entity = batchSvc.create( entity ); 
        
        def op = Inv.lookupOpener('batchcapture_collection:open', [entity: [objid: entity.objid]]);  
        op.target = 'self'; 
        win.setTitle('Batch Capture Collection'); 
        return op; 
    }    
}    