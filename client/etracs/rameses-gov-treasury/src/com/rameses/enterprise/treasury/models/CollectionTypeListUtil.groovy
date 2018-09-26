package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

class CollectionTypeListUtil  {

    @Service("QueryService")
    def qryService;
    
    def _list;
    def _afTypes;
    def _collectionTypes;
    
    def _mode;
    def _org;
    def _afType;
    
    def getWhereQuery() {
        def arr = [];
        def parm = [:];
        if(org) {
            arr << "orgid = :orgid";
            parm.orgid = org.objid;
        }
        else if( OsirisContext.env.ORGROOT == 1 ) {
            arr << "orgid IS NULL";
        } 
        else { 
            arr << "orgid = :orgid";
            parm.orgid = OsirisContext.env.ORGID;
        }
        if( mode == "ONLINE") {
            arr << " allowonline = 1";
        }
        else {
            arr << " allowoffline = 1";
        }
        arr << " af.formtype = 'serial' ";
        return [arr, parm];
    }
    
    public void setOrg(def o) {
        _org = o;
    }
    
    public def getOrg() {
        return _org;
    }
    
    public void setMode( def o ) {
        _mode = o; 
        _afTypes = null;
        _collectionTypes = null;
    }
    
    public def getMode() {
        return _mode;
    }
    
    public void setAfType( def af ) {
        _afType = af;
        _collectionTypes = null;
    }
    
    public def getAfType() {
        return _afType;
    }
    
    public def getAfTypes() {
        if(!mode) return [];
        if(!_afTypes) {
            def wq = getWhereQuery();
            def m = [_schemaname: "vw_collectiontype"];
            m.select = "formno";
            m.where = [wq[0].join(" AND "), wq[1]];
            _afTypes = qryService.getList( m )*.formno.unique();
        }
        return _afTypes;
    }
    
    public def getCollectionTypes() {
        if(!_afType) return [];
        if(!_collectionTypes) {
            def wq = getWhereQuery();
            wq[0] << "formno = :aftype";
            wq[1].aftype = afType;
            def m = [_schemaname: "vw_collectiontype"];
            m.where = [wq[0].join(" AND "), wq[1]];
            m.orderBy = "sortorder,title";
            //m.debug = true;
            _collectionTypes = qryService.getList(m); 
        }
        return _collectionTypes;
    }
    
    //this will set collection type if has items
    public void checkHasItems( def col )  {
        def m = [_schemaname: "collectiontype_account"];
        m.findBy = [collectiontypeid: col.objid ];
        m.select = "cnt:{COUNT(*)}";
        def z = qryService.findFirst(m);
        if( z.cnt > 0 ) {
            col.hasitems =true;
        }
        else {
            col.hasitems = false;
        }
    }
    
    
}    