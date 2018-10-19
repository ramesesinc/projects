package com.rameses.enterprise.financial.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 

class AccountMappingModel { 

    @Service("QueryService")
    def querySvc;
    
    @Service("PersistenceService")
    def persistenceSvc;

    @Service("LOVService")
    def lov;
    
    
    def selectedItem;
    def mode = "initial";
    def mainGroupList;
    def mainGroup;
    def searchtext;
    
    String title;
    def itemAcctType;
    def itemAcctTypes;
    
    boolean showUnmapped = true;
    
    def init() {
        mainGroupList = querySvc.getList( [_schemaname:'account_maingroup', _limit: 1000 ] );
        itemAcctTypes = lov.getKeyList("ITEM_ACCOUNT_TYPES");
        itemAcctType = "REVENUE";
        title = "Account Mapping - Select a Main Group";
        mode = "initial";
        return mode;
    }
    
    def next() {
        if(!mainGroup) throw new Exception("Please select a main group");
        title = mainGroup.title + " Account Mapping ";
        mode = "view"
        return mode;
    }
    
    def back() {
        mode = "initial";
        return mode;
    }
    
    void searchItem() {
        listHandler.reload();
    }
    
    @PropertyChangeListener
    def listener = [
        "itemAcctType|showUnmapped" : { o->listHandler.moveFirstPage();}
    ];
    
    def listHandler = [
        isMultiSelect: {
            return true;
        },
        fetchList : { o->
            def conds = [];
            def p = [:];
            def m = [:];
            m.putAll(o);
            if(showUnmapped ) {
                m._schemaname = "itemaccount";
                m.select = "objid,code,title,account_code:{NULL},account_title:{NULL}";
                conds << ' objid NOT IN (${subquery}) ';
                m.vars = [subquery: "SELECT itemid FROM account_item_mapping WHERE maingroupid = '" + mainGroup.objid + "'"];
            }
            else {
                m._schemaname = "account_item_mapping";
                m.select = "objid,code:{item.code},title:{item.title},account.*";
                conds << "maingroupid = :maingroupid";
                p.maingroupid = mainGroup.objid
            }
            
            if( searchtext ) {
                if(showUnmapped) {
                    conds << '(code LIKE :searchtext OR title LIKE :searchtext)';
                }
                else {
                    conds << '(item.code LIKE :searchtext OR item.title LIKE :searchtext)';
                }
                p.searchtext = searchtext+'%';
            }
            if(itemAcctType) {
                if(showUnmapped) {
                    conds << " type = :type";
                }
                else {
                    conds << " item.type = :type";
                }
                p.type = itemAcctType;
            }
            m.where = [ conds.join( " AND "), p ];
            
            def list =  querySvc.getList(m); 
            list.each {
                it.action = (showUnmapped) ? "Map" : "Unmap"
            }
            return list;
        }
        
    ] as PageListModel;
    
    
    void mapAccount() {
        if(!listHandler.selectedValue) 
            throw new Exception("Please select ant least one item to map");
        def h = { o->
            def list = listHandler.selectedValue*.objid;
            list.each { it ->
                def m = [_schemaname:'account_item_mapping'];
                m.maingroupid = mainGroup.objid;
                m.item = [objid: it ];
                m.account = o;
                persistenceSvc.create(m);
            }
            listHandler.reload();
       }
       Modal.show( "account:lookup", ['query.maingroupid': mainGroup.objid, onselect: h]);
    }

    void unmapAccount() {
        if(!listHandler.selectedValue) 
            throw new Exception("Please select ant least one item to unmap");
        if(!MsgBox.confirm("You are about to unmap the selected accounts. Proceed?")) return;   
        listHandler.selectedValue*.objid.each {
            def m = [_schemaname:'account_item_mapping'];
            m.objid = it;
            persistenceSvc.removeEntity(m);
        }
    }

} 

