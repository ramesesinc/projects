package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.rcp.framework.ValidatorException;


class AFTxnModel extends CrudPageFlowModel {

    @Service("AFTxnService")
    def svc;
    
    @Service("DateService")
    def dateSvc;
    
    boolean withrequest = false;
    def afrequest;
    
    def formTypes;
    def afTypes;
    def afType;
    def reqtype = null;
    def form;
    def itemTxnTypes;
    
    @FieldValidator
    def validators = [
        "form.startseries" : { o->
            try {
                Integer.parseInt(o);
            }
            catch(e) {
                throw new ValidatorException("Start series must be a number" );
            }
            if( !o.endsWith("1") )
                throw new ValidatorException("Start series must end with 1" );
            if(o.length() != form.afunit.serieslength ) {
                throw new ValidatorException("Start series length must be " + form.afunit.serieslength );
            }      
        }
    ];
    
    @PropertyChangeListener
    def listener = [
        "entity.issueto" : { o->
            entity.respcenter = [objid:o.orgid, name:o.orgname];
        },
        "afType" : { o->
            afListModel.reload();
        },
        "entity.issuefrom" : { o->
            if( entity.txntype.matches("TRANSFER|RETURN") ) afListModel.reload();
        },
        "form.startseries" : { o->
            int len = form.afunit.serieslength;
            if( (o+"").length() > len )
                throw new Exception("Series length must be less than or equal to "+len);
            int starting = Integer.parseInt(""+o);    
            int ending = (starting + form.afunit.qty);
            form.startseries = String.format( "%0"+len+"d", starting);
            form.endseries = String.format( "%0"+len+"d", ending - 1);
            binding.refresh("form.(start|end)series");
        }
    ];
    
    void create() {
        entity = [:];
        entity.state = 'DRAFT';
        entity.items = [];
        afrequest = null;
        itemListHandler.reload();
        entity.dtfiled = dateSvc.getBasicServerDate();
        
        def m = [_schemaname:'aftxn_type'];
        m.select = "formtype";
        m.where = ["1=1"];
        m.orderBy = "sortorder";
        formTypes = queryService.getList(m)*.formtype.unique();
        
        //load formTypes
        m = [_schemaname: 'af'];
        m.where = ["1=1"];
        m.orderBy = "objid";
        afTypes = queryService.getList(m);
    }
    
    void copyRequest() {
        MsgBox.alert('copy trequest');
        def req = persistenceService.read( [_schemaname:'afrequest', objid: afrequest.objid ] );
        entity.request = [objid:req.objid , reqno: req.reqno];
        entity.issueto = req.requester;
        entity.respcenter = req.respcenter;
        req.items.each {
            def m = [:];
            m.item = it.item;
            m.unit = it.unit;
            m.qtyrequested = it.qty;
            m.qty = it.qty;
            m.qtyserved = 0;
            m.cost = it.afunit.saleprice;
            m.txntype = req.reqtype;
            m.afunit = it.afunit;
            computeLineTotal(m);
            entity.items << m;
        }
        itemTxnTypes = [ req.reqtype ];
    }
    
    void loadItemTxnTypes() {
        def m = [_schemaname:"aftxn_type"];
        m.findBy = [formtype: entity.txntype];
        m.orderBy = "sortorder";
        itemTxnTypes = queryService.getList( m )*.txntype;
    }
    
    void initSingleForm() {
        MsgBox.alert("init single form");
        form = [:];
    }
    
    //the only lookup here is for the request because the other request os for purchase (af receipt)
    public def getLookupRequest() {
        if( entity.txntype == "ISSUE" ) {
            return Inv.lookupOpener( "afrequest_collection:lookup", [:] );
        }
        else {
            return Inv.lookupOpener( "afrequest_purchase:lookup", [:] );
        }
    }
    
    def itemListHandler = [
        fetchList : { o->
            return entity.items;
        },
        onAddItem: { o-> 
            //if ( !o.qtyrequested ) o.qtyrequested = o.qtyissued;
            entity.items << o;
        },
        onRemoveItem: { o->
            entity.items.remove(o);
        },
        onColumnUpdate: { o,colName->
            if ( colName == "item" ) {
                o.item.objid = o.item.itemid;
                o.unit = o.item.unit; 
                o.cost = o.item.saleprice; 
                computeLineTotal( o ); 
            }
            else if(colName=="qty") {
                if ( afrequest && o.qty > o.qtyrequested ) 
                    throw new Exception("Qty must be less than qty requested");
                computeLineTotal( o ); 
            } 
            else if(colName=="cost") {
                computeLineTotal( o ); 
            } 
        }
    ] as EditorListModel;
    
    private void computeLineTotal( o ) {
        o.linetotal = (o.qty ? o.qty : 0) * (o.cost ? o.cost : 0.0);
    }
    
    public def getInfo() {
        return TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/views/AFTxnViewPage.gtpl", [entity:entity] );
    }
    
    void buildItems() {
        def selectedItems = afListModel.selectedValue;
        MsgBox.alert( selectedItems*.objid );
        MsgBox.alert( selectedItems.size() );
    }
    
    void clearItems() {
        afType = null;
        entity.issuefrom = null;
        entity.issueto = null;
        entity.respcenter = null;        
        afListModel.reload();
    }
    
    def afListModel = [
        isMultiSelect: {
            return true;
        },
        fetchList: { o->
            if(!afType) return [];
            if(!entity.issuefrom?.objid ) return [];
            def list = [];
            def p = [:];
            def m = [_schemaname:"af_control"];
            list << "owner.objid = :ownerid";
            list << "afid = :afid";
            list << "owner.objid = assignee.objid";
            list << "currentseries <= endseries";
            list << "active = 0";
            list << "NOT(txnmode = 'REMOTE')";
            p.ownerid = entity.issuefrom.objid;
            p.afid = afType.objid;
            m.where = [ list.join(" AND "), p ];
            m.orderBy = "stubno";
            return queryService.getList( m );    
        }
    ] as BasicListModel;
    
    void saveCreate() { 
        if ( entity.txntype == 'TRANSFER' ) {
            if ( entity.issuefrom.objid == entity.issueto.objid ) 
                throw new Exception("Issued To must not be the same with the Issued From. Please select another");
            buildItems();    
        }
        else if( entity.txntype == 'RETURN' ) {
            buildItems();
        }
        else if( entity.txntype == 'FORWARD' ) {
            
        }
        def b = MsgBox.confirm( "You are about to save the draft document. Proceed?" );
        if(!b) throw new BreakException();
        super.saveCreate();
    }
    
    void open() {
        def e = [_schemaname:'aftxn', objid: entity.objid];
        entity = persistenceService.read( e );
    }
    
    def startOpen() {
        open();
        String state = (entity.state == 'DRAFT') ? 'view' : 'posted';
        return super.start( state );
    }
    
    void post() {
        svc.post([objid:entity.objid ]);
        reloadEntity();
    }

    void reloadEntity() {
        open();
        binding.refresh();
    }
    
    //**************************************************************************
    //PURCHASE RECEIPT 
    //**************************************************************************/
    def addBatch( def o ) {
        def item = entity.items.find{ it.objid == o.aftxnitemid };
        return Inv.lookupOpener( "af_control:addbatch", [ 
            refitem:item, handler:{ vv-> reloadEntity(); } 
        ]);
    }
    
    void removeBatch(def o) {
        if( !MsgBox.confirm('You are about to remove the entered accountable forms. Proceed?') ) return;
        o.refid = entity.objid;
        o.txntype = entity.txntype;
        try {
            svc.removeBatch(o);
            reloadEntity();
        }
        catch(e) {
            MsgBox.err(e);
        }
    }
    
    void revertBatch(def o) {
        if( !MsgBox.confirm('You are about to revert the entries. Proceed?') ) return;
        o.refid = entity.objid;
        o.txntype = entity.txntype;
        try {
            svc.revertBatch(o);
            reloadEntity();
        }
        catch(e) {
            MsgBox.err(e);
        }
    }

    def editBatch( def o ) {
        def item = entity.items.find{ it.objid == o.aftxnitemid };
        def e= [:];
        e.afid = item.item.objid;
        e.unit = item.unit;
        e.afunit = item.afunit;
        return Inv.lookupOpener("af_control:editbatch", ['aftxnitemid': o.aftxnitemid, entity: e, txntype:entity.txntype ]);
    }
    
    def issueBatch( def o ) {
        try {
            svc.issueBatch([ aftxnitemid: o.aftxnitemid ]);
            reloadEntity();
        }
        catch(e) {
            MsgBox.err(e);
        }
    }
    
    def moveBatch( def o ) {
        def item = entity.items.find{ it.objid == o.aftxnitemid };
        item.parent = entity;
        def strname = "af_control_batch:" + item.afunit.formtype + ":lookup"
        def h = { v ->
            try {
                svc.moveBatch( item, v );
                reloadEntity();
            }
            catch(e) {
                MsgBox.err(e);
            }
        }
        return Inv.lookupOpener(strname, [refitem: item, onselect: h ] );
    }
    
    
}    