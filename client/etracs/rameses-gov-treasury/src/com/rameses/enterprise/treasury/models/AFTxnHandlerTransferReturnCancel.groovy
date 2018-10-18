package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.rcp.framework.ValidatorException;


class AFTxnHandlerTransferReturnCancel extends AFTxnHandler {

    def afType;
    def afTypes;
    
    @PropertyChangeListener
    def listener = [
        "afType" : { o->
            afListModel.reload();
        }
    ];
    
    def init() {
        def m = [_schemaname: 'af'];
        m.where = ["1=1"];
        m.orderBy = "objid";
        afTypes = queryService.getList(m);
        return super.init();
    }
    
    def afListModel = [
        isMultiSelect: {
            return true;
        },
        fetchList: { o->
            if(!afType) return [];
            //if not manual issue it must require issuefrom
            if(entity.txntype!='MANUAL_ISSUE' && !entity.issuefrom?.objid ) return [];
            
            def list = [];
            def p = [:];
            def m = [_schemaname:"af_control"];
            
            if( afType ) {
                list << "afid = :afid";
                p.afid = afType.objid;
            }
            
            if( entity.txntype == "MANUAL_ISSUE") {
                list << "state = 'OPEN' ";
            }
            else {
                list << "owner.objid = :ownerid";
                list << "owner.objid = assignee.objid";
                list << "currentseries <= endseries";
                list << "NOT(txnmode = 'REMOTE')";
                p.ownerid = entity.issuefrom?.objid;
            }
            list << "active = 0";
            m.where = [ list.join(" AND "), p ];
            m.orderBy = "stubno";
            //m.debug = true;
            return queryService.getList( m );    
        }
    ] as BasicListModel;

    def save() {
        if ( (entity.txntype == "TRANSFER") && (entity.issuefrom.objid == entity.issueto.objid) ) 
                throw new Exception("Issued To must not be the same with the Issued From. Please select another");
        
        if(!MsgBox.confirm("You are about to save this record. Proceed?")) return null;
        entity._schemaname = "aftxn";

        entity.afitems = afListModel.selectedValue; 
        entity.afitems.each{
            it.remove('currentdetail'); 

            def m = [:]; 
            m.item = it.afunit; 
            m.item.putAll( it.af ); 
            m.unit = it.unit;
            m.txntype = entity.txntype; 
            m.qtyserved = m.qty = 1; 
            m.linetotal = m.cost = it.cost; 
            entity.items << m; 
        }
        def e = persistenceService.create(entity);
        entity.clear();
        entity.putAll(e);
        return forward();
    }    
}    