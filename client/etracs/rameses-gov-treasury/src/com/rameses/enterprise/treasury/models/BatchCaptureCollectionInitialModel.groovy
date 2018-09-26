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
        //lookup stubs assigned to this collector having this number
        def str = "owner.objid = :userid";
        if( subcollector) {
            str = "assignee.objid = :userid"
        }
        def m = [_schemaname:'af_control'];
        m.where = ["startseries = :startseries AND currentseries <= endseries AND txnmode='CAPTURE' AND " + str, 
                    [startseries: startseries, userid: userid ]];
        def list = qryService.getList(m);
        if(!list) throw new Exception("No items found with the specified start series assigned for this user");
        def selectedItem = null;
        if( list.size() > 1 ) {
            throw new Exception("There are two active accountable forms having the same start series")
        }
        else {
            selectedItem = list[0];
        }
        
        def collectiontype = null;
        def h = { o->
            collectiontype = o;
        }
        def filter = ["orgid IS NULL AND allowbatch=1"];
        if( selectedItem.respcenter?.objid ) {
            filter = ["orgid =:orgid AND allowbatch=1", [orgid: selectedItem.respcenter.objid ]];
        }
        Modal.show("collectiontype:lookup", [customFilter: filter, onselect: h]);
        if(!collectiontype) {
            return null;
        }
        def entity = [
            state: "DRAFT",
            defaultreceiptdate: startdate, 
            txnmode: "CAPTURE",
            stub: selectedItem.stubno,
            formno: selectedItem.afid,
            formtype: selectedItem.af.formtype,
            controlid: selectedItem.objid,
            serieslength: selectedItem.af.serieslength,
            prefix:selectedItem.prefix,
            suffix:selectedItem.suffix,
            startseries: selectedItem.startseries,
            endseries: selectedItem.endseries,
            totalamount: 0,
            totalcash: 0,
            totalnoncash : 0,
            collector: selectedItem.owner,
            capturedby:selectedItem.assignee, 
            org: selectedItem.respcenter,
            collectiontype: collectiontype
        ]
        
        entity = batchSvc.create( entity ); 
        def op = Inv.lookupOpener('batchcapture_collection:open', [entity: [objid: entity.objid]]);  
        op.target = 'self'; 
        win.setTitle('Batch Capture Collection (' + entity.startseries + ')' ); 
        return op; 
    } 
       
}    