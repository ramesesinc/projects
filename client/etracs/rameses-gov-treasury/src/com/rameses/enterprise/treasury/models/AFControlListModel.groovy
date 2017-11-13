package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AFControlListModel extends CrudListModel {

    @Script("User")
    def user;
    
    def afTypes;
    
    void afterInit() {
        def m = [_schemaname: 'af', select:'objid', _limit:100 ];
        afTypes = queryService.getList( m ).collect{ it.objid } 
    }

    @PropertyChangeListener
    def listener = [
        "query.formno" : { o->
            reload();
        }
    ];
    
    def getCustomFilter() {
        def str = [];
        def m = [:];
        if( query.formno ) {
            str << " afid=:afid ";
            m.afid = query.formno;
        }
        if( tag == 'COLLECTION' ) {
            str << " owner.objid = :userid ";
            m.userid = user.userid;
        };
        if( !str )  return null;
        return [ str.join( " AND "), m ];
    }
    
}    