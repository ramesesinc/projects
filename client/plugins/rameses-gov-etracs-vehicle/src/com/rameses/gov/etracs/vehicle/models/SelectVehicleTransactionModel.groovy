package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;

public class SelectVehicleTransactionModel  {

    def txntype;
    
    @FormTitle
    String title;

    @FormId
    def formId;
    
    def nodeContext;
    def homeicon = 'home/icons/folder.png';
    
    void init() {
        formId = txntype.objid;
        title = txntype.title;
        if(nodeContext) {
            title += " " +nodeContext.caption;
            formId += "_"+nodeContext.id;
        }
    }                        

    def listModel = [
        fetchList: { o->
            if( nodeContext==null ) {
                return [
                    [caption: 'Application', id:'application', icon:homeicon],
                    [caption: 'Account', id:'account', icon:homeicon],
                    [caption: 'Permit', id:'permit', icon:homeicon],
                ];
            }
            else if(nodeContext.id == 'application') {
                return [
                    [caption: 'Listing', id:'list', type:'vehicle_application', icon:homeicon],
                    [caption: 'New', id:'new', type:'vehicle_application', icon:homeicon],
                    [caption: 'Renew', id:'renew', type:'vehicle_application', icon:homeicon],
                    [caption: 'Retire', id:'retire', type:'vehicle_application', icon:homeicon],
                ];
            }
        },
        onOpenItem: { o->
            if( nodeContext == null ) {
                return Inv.lookupOpener( 'vehicle_transaction:menu', [txntype:txntype, nodeContext: o] );
            }
            else {
                String t = o.type + "_" + txntype.uihandler + ":" + o.id;
                return Inv.lookupOpener( t, [txntype: txntype ] ); 
            }
        }
    ] as TileViewModel;

    
}