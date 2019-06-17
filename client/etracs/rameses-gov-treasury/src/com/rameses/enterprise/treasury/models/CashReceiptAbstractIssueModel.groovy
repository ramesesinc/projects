package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;

abstract class CashReceiptAbstractIssueModel extends PageFlowController  {

    @Service("QueryService")
    def qryService;
    
    @Service("CashReceiptService")
    def cashReceiptSvc;
    
    @Service("AFControlService")
    def afControlSvc;  

    boolean subcollectorMode = false;

    def modeList = ["ONLINE", "OFFLINE"];
    def collectionTypes;
    
    //initial data holders
    def mode = "ONLINE";
    def afType;
    def collectionType;
    def receiptdate;
    
    def params;
    def entity;
    def afcontrol;
    
    abstract String getDefaultAfType();
    abstract String getFormType();
    
    @PropertyChangeListener
    def listener = [
        "mode" : { o->
            collectionTypes.mode = o;
            def formno = afType; 
            if ( formno ) formno = collectionTypes.getAfTypes().find{ it == formno }
            if( defaultAfType ) {
                if ( !formno ) formno = collectionTypes.getAfTypes().find{ it == defaultAfType } 
            }
            afType = formno; 
            receiptdate = null;
            collectionType = null;
            collectionTypes.afType = afType; 
        },
        "afType" : { o->
            collectionType = null;
            collectionTypes.afType = o;
        }
    ];
    
    def getAfTypeList() {
        return collectionTypes.getAfTypes();
    }

    def getCollectionTypeList() {
        return collectionTypes.getCollectionTypes();
    }
    
    void initStart() {
        collectionTypes = ManagedObjects.instance.create( CollectionTypeListUtil.class );
        collectionTypes.formType = getFormType();
        listener.mode('ONLINE'); 
    }
    
    def startCollector() {
        subcollectorMode = false;
        initStart();
        return super.start();
    }
    
    def startSubCollector() {
        subcollectorMode = true;
        initStart();
        return super.start();
    }
    
    //checks and loads the af here
    void createNew() {
        //sprinkle the selected collection type if it has items
        collectionTypes.checkHasItems( collectionType );
        params = [:];
        params.txnmode = mode;
        params.formno = afType; 
        params.formtype = collectionType.af.formtype; 
        params.collectiontype = collectionType;
        if(receiptdate) params.receiptdate = receiptdate;
        if(subcollectorMode) params._subcollector = true;
        
        //we create another object because params is intended for add another.
        entity = [:];
        entity.putAll( params );
        entity.active = 1;
        if(subcollectorMode==true) entity.subcollector = true;
        entity.formType = getFormType();
        def env = OsirisContext.env; 
        def p = [_schemaname: 'af_control']; 
        p.where = CashReceiptAFLookupFilter.getFilter( entity ); 
        def selAF = qryService.findFirst( p );
        if ( !selAF ) {
            //if there is no active stub
            entity.active = 0; 
            p = [ entity: entity ]; 
            p.onselect = { o-> 
                afControlSvc.activateSelectedControl([ objid: o.objid ]);
                selAF = o;             
            }
            def strc = (subcollectorMode==false)?"cashreceipt:select-af":"cashreceipt:select-af:subcollector";
            Modal.show(strc, p );
        } 
        if(!selAF) throw new BreakException();
        afcontrol = selAF;
        
        entity = cashReceiptSvc.init( entity );
        
        //validate if online mode
        if ( mode == 'OFFLINE' ) {
            cashReceiptSvc.verifyOffline( entity ); 
        } 
        //sprinkled cash receipt
        //if(!entity.items) entity.items = [];
    }

    

}    