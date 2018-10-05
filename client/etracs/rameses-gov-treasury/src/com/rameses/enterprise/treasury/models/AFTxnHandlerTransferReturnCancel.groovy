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
    
    void init() {
        def m = [_schemaname: 'af'];
        m.where = ["1=1"];
        m.orderBy = "objid";
        afTypes = queryService.getList(m);
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

    void initReturn() {
        title = "Return Accountable Form";
        init();
    }
    
    void initTransfer() {
        title = "Transfer Accountable Form";
        init();
    }
    void initCancel() {
        title = "Cancel Accountable Form";
        init();
    }
    
    void save() {
        
    }
    
}    