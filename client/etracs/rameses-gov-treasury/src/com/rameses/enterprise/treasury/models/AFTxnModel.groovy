package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.rcp.framework.ValidatorException;


class AFTxnMainModel extends CrudPageFlowModel {

    @Service("DateService")
    def dateSvc;
    
    def txnTypes;
    def txntype;
    def dtfiled;
    boolean withrequest = false;
    def afrequest;
    def reqtype = null;
    def handler;
    def mode;
    

    def startCreate() {
        afrequest = null; 
        dtfiled = dateSvc.getBasicServerDate();
        try {
            txnTypes = Inv.lookupOpeners("aftxn:handler").collect { [caption:it.caption, name:it.properties.name] };
        }
        catch(e){;}
        mode  = "create";
        return super.start("default");
    }
    
    def startOpen() {
        entity._schemaname = "aftxn";
        entity = persistenceService.read( entity );
        mode = "open";
        if( entity.state  == "DRAFT") {
            buildOpener("open");
            return super.start("process");
        }
        else {
            return super.start("view");
        }
    }
    
    void initCreate() {
        initData();
        buildOpener("create");
    }

    
    void initData() {
        entity = [:];
        entity.state = "DRAFT";
        entity.items = [];
        entity.txntype = txntype;
        entity.dtfiled = dtfiled;
        if(withrequest) {
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
                if(m.txntype.matches("ISSUE")) {
                    m.txntype = null;
                }
                m.afunit = it.afunit;
                m.linetotal = (m.qty ? m.qty : 0) * (m.cost ? m.cost : 0.0);
                entity.items << m;
            };
        }
    }
    
    void buildOpener(def _mode) {
        String hname = "aftxn:handler:"+ entity.txntype.toLowerCase();
        try {
            def h = [
                back : {
                    def op = super.signal("back");
                    binding.fireNavigation( op );
                },
                forward: {
                    entity = persistenceService.read( [_schemaname:'aftxn', objid: entity.objid ] );                    
                    def op = super.signal("forward");
                    binding.fireNavigation( op );
                },
                exit: {
                    def op = super.signal("exit");
                    binding.fireNavigation( op );
                }
            ];
            handler = Inv.lookupOpener(hname, [handler: h, entity: entity, afrequest: entity.request, mode: _mode ]);
        }
        catch(e) {
            throw new Exception("Txn Type " + hname + " not found!" );
        }    
    }    
    
    
     //the only lookup here is for the request because the other request os for purchase (af receipt)
    public def getLookupRequest() {
        if( txntype == "ISSUE" ) {
            return Inv.lookupOpener( "afrequest_collection:lookup", [:] );
        }
        else {
            return Inv.lookupOpener( "afrequest_purchase:lookup", [:] );
        }
    }

    public def getInfo() {
        return TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/views/AFTxnViewInfo.gtpl", [entity:entity] );
    }
    
    /*
    void clearItems() {
        afType = null;
        entity.issuefrom = null;
        entity.issueto = null;
        entity.respcenter = null;        
        afListModel.reload();
    }
    */
   
    //for printing purposes
    def preview() {
        buildCompatibility( entity ); 

        def path = 'com/rameses/gov/treasury/ris/report/';
        def mainreport = path + 'ris.jasper';
        def subreports = new SubReport[1]; 
        subreports[0] = new SubReport("ReportRISItem", path + "risitem.jasper"); 
        def params = [ title: 'RIS Report']; 
        params.reportHandler = [ 
           getData : { return entity; }, 
           getReportName : { return mainreport; },
           getSubReports : { return subreports; } 
        ]; 
        return Inv.lookupOpener('simple_form_report', params);
    }
    
    private void buildCompatibility( data ) {
        data.reqno = data.request?.reqno;
        if ( !data.reqno ) data.reqno = data.controlno;
        if ( !data.requester ) data.requester = data.issueto; 
        data.items.each{ o-> 
            if ( o.qtyreceived == null ) o.qtyreceived = o.qtyserved;
        }
        if ( !data.reqtype ) {
            data.reqtype = null; 
            if ( data.reqtype.toString().toUpperCase().startsWith('PURCHASE')) {
                data.reqtype = 'PURCHASE'; 
            } else if ( data.reqtype.toString().toUpperCase().startsWith('ISSU')) {
                data.reqtype = 'ISSUANCE'; 
            }
        }
        data.totalcost = 0;
        data.items.each{ o-> 
            data.totalcost += (o.linetotal ? o.linetotal : 0.0); 
            if ( o.series ) return; 
            def buff = []; 
            if ( o.afunit?.formtype == 'serial') {
                o.items.each {
                    buff << (
                        it.startseries.toString() +' - '+ it.endseries.toString() + 
                        ' ( '+ it.startstub +' - '+ it.endstub +' )' 
                    ); 
                }
            } else if ( o.afunit?.formtype != 'serial') { 
                o.items.each {
                    buff << (
                        'Stub No. ( '+ it.startstub.toString() +' - '+ it.endstub.toString() +' )'
                    ); 
                }
            } 
            o.series = buff.join(', ');
        }
    }
    
}    