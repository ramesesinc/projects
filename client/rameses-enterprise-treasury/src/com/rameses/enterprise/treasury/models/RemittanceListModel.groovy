package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.seti2.models.*;

public class RemittanceListModel extends CrudListModel {

    @Script("User")
    def user;

    @Service('RemittanceService') 
    def remittanceSvc; 

    @FormId
    public String getFormId() {
        return "remittance-list-"+invoker.properties.tag;
    }

    public def getCustomFilter() {
        if( invoker.properties.tag == "collector" ) {
            return [ "collector.objid =:uid", [uid: user.userid ] ];
        }
        else {
            return null;
        }    
    }

    void revert() {
        if ( !selectedItem ) throw new Exception('Please select an item'); 
        if( selectedItem.state != 'OPEN' ) 
            throw new Exception("Revert is only applicable for remittances not yet approved");
        if ( MsgBox.confirm('You are about to revert this remittance. Continue?')) {
            remittanceSvc.revert([ objid: selectedItem.objid ]);
            MsgBox.alert('Successfully processed'); 
            reload();   
        } 
    } 
    
    def liquidate() {
        return Inv.lookupOpener( "liquidation:create" );
    } 
} 