package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;
import com.rameses.util.BreakException;

class RemittanceInitialModel {

    @Binding
    def binding;
    
    @Service("DateService")
    def dateSvc; 
    
    @Service("RemittanceService")
    def remSvc;   
    
    @Service("QueryService")
    def queryService;
    
    @Service("Var")
    def var;
    
    @Script("User")
    def userInfo;
    
    def startdate;
    def startime;
    
    def amount = 0; 
    def totalvoid = 0;
    
    def summaryList;
    def voidList = [];
    def user;
        
    String title = "Remittance";
    
    def draftremid; 
    def remittanceid; 
    
    void init() {
        def today = dateSvc.parseCurrentDate();
        startdate = new java.text.SimpleDateFormat("yyyy-MM-dd").format( today.date );
        startime = today.hour.toString().padLeft(2,"0")+":"+today.minute.toString().padLeft(2,"0");
//        def tm =  var.getProperty("remittance_cutoff_time", st ).split(":");
//        def p = [
//            date: today.date,
//            hour: tm[0],
//            min: tm[1],
//            includeTime: true,
//            handler: { o->
//                startdate = o.date;
//                startime = o.hour + ":" + o.minute;
//            }
//        ];
//        Modal.show("date:prompt", p, [title:'Enter Remittance cut off date'] )
//        if(!startdate) throw new BreakException();
        
        def app = userInfo.env;
        user = [objid: app.USERID, name: app.NAME, fullname: app.FULLNAME, username: app.USER ];
        
        def param = [collectorid: user.objid, remdate: startdate +" "+ startime +":00"];
        //build summaryList
        
        def resp = remSvc.init( param ); 
        if ( !resp.items ) throw new Exception("No cash receipts to remit");

        summaryList = resp.items; 
        amount = summaryList.sum{( it.amount ? it.amount : 0.0 )} 
        if ( amount == null ) amount = 0.0; 
        
        voidList = resp.voiditems; 
        totalvoid = voidList.sum{( it.amount ? it.amount : 0.0 )} 
        if ( totalvoid == null ) totalvoid = 0.0; 
        
        draftremid = resp.objid; 
    }
    
    def getFormattedAmount() {
        if ( amount == null ) amount = 0.0; 
        return new java.text.DecimalFormat("#,##0.00").format( amount ); 
    }
    def getFormattedTotalVoid() {
        if ( totalvoid == null ) totalvoid = 0.0; 
        return new java.text.DecimalFormat("#,##0.00").format( totalvoid ); 
    }
    
    def afSummaryHandler = [
        fetchList: { o->
            return summaryList;
        },
        onOpenItem: {o,col->
            def p = [:];
            p.put( "query.afcontrolid", o.controlid );
            p.put( "query.fromseries", o.fromseries );
            p.put( "query.toseries", o.toseries );
            return Inv.lookupOpener("cashreceipt_list:afseries", p );
        }
    ] as BasicListModel;
    
    def voidReceiptHandler = [
        fetchList: { o->
            return voidList;
        },
        onOpenItem: {o,col->
            return Inv.lookupOpener("cashreceipt:open", [entity: [objid: o.objid ]] );
        }
    ] as BasicListModel;
    
    void submitForRemittance() { 
        def p = [ objid: draftremid ]; 
        def resp = remSvc.create( p ); 
        remittanceid = resp?.objid; 
        
        def op = Inv.lookupOpener("remittance:open", [ entity: p ]); 
        Inv.invoke( op );  
        binding.fireNavigation('_close'); 
    } 
}    