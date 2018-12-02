package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

class BatchCaptureCollectionInitialModel  {

    @Binding
    def binding;

    @Caller
    def caller;

    @Service("QueryService")
    def qryService;
    
    @Service('DateService')
    def dateSvc; 
    
    @Service("BatchCaptureCollectionService")
    def batchSvc;
    
    String title = "Batch Cash Receipt Initial (Select Type of Collection)"
    
    @FormId
    String formId = "batchcapture_collection:create";
    
    @SubWindow
    def win;
    
    def startseries;
    def startdate;
    def userid;
    boolean subcollector = false;
    
    void init() {
        userid =  OsirisContext.env.USERID;
        startdate = dateSvc.getBasicServerDate();
        if( caller.tag == "subcollector") {
            subcollector = true;
            title += " (Subcollector)";
            formId += ":subcollector";
        }
    }
    
    def doNext() {
        def wheres = []; 
        def wheremap = [ userid: userid ]; 
        if ( subcollector ) { 
            wheres << " assignee.objid = :userid AND assignee.objid <> owner.objid "; 
        } else {
            wheres << " owner.objid = :userid AND assignee.objid = owner.objid "; 
        } 

        wheres << " currentseries = :currentseries AND currentseries <= endseries AND txnmode='CAPTURE' "; 
        wheremap.currentseries = startseries;
        
        def list = qryService.getList([ _schemaname:'af_control', where: [wheres.join(" AND "), wheremap]]); 
        if ( !list ) { 
            throw new Exception("No accountable forms matches the specified start series. Please verify"); 
        }
        if ( list.size() > 1 ) {
            throw new Exception("There are two active accountable forms having the same start series"); 
        }

        def afc = list.first(); 
        def collectiontype = null;
        def h = { o->
            def ctfundid = o.fund?.objid; 
            def affundid = afc.fund?.objid; 
            if ( affundid == null && ctfundid == null ) {;}
            else if ( affundid != null && ctfundid != null && affundid == ctfundid ) {;} 
            else throw new Exception(o.title +" is not allowed. Please select another."); 

            collectiontype = o;            
        }
        
        wheres = [" formno = :formno AND allowbatch=1 "]; 
        wheremap = [ formno: afc.afid ]; 
        if ( OsirisContext.env.ORGROOT.toString() == '0' ) { 
            wheres << " orgid = :orgid "; 
            wheremap.orgid = OsirisContext.env.ORGID; 
        }
        else if( afc.respcenter?.objid ) { 
            wheres << " orgid = :orgid "; 
            wheremap.orgid = afc.respcenter.objid;
        } 
        else {
            wheres << " orgid IS NULL "; 
        }
        
        Modal.show("collectiontype:lookup", [onselect: h, customFilter: [wheres.join(" AND "), wheremap]]);
        if ( !collectiontype ) return null;

        def entity = [
            state: "DRAFT", 
            txnmode: "CAPTURE",
            defaultreceiptdate: startdate, 
            stub: afc.stubno,
            formno: afc.afid,
            formtype: afc.af.formtype,
            controlid: afc.objid,
            serieslength: afc.af.serieslength,
            prefix: afc.prefix,
            suffix: afc.suffix,
            startseries: afc.currentseries,
            endseries: afc.endseries,
            totalcash: 0,
            totalnoncash: 0,
            totalamount: 0,
            collector: afc.owner,
            capturedby:afc.assignee, 
            org: afc.respcenter,
            collectiontype: collectiontype
        ]; 
        
        entity = batchSvc.create( entity ); 
        def op = Inv.lookupOpener('batchcapture_collection:open', [entity: [objid: entity.objid]]);  
        op.target = 'self'; 
        win.setTitle('Batch Capture Collection (' + entity.startseries + ')' ); 
        return op; 
    } 
       
}    