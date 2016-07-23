package com.rameses.gov.etracs.bpls.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.rmi.server.UID;

class LobListModel extends ComponentBean {
        
    //ASSESSMENT TYPES: NEW, RENEW, RETIRE
    boolean showadd = true;
    boolean showrenew = true;
    boolean showretire = true;
    boolean showreclassify = false;
    
    def entity = [:];
    def selectedItem;
    
    def handler;
    
    def getItems() {
        if(!entity.lobs) entity.lobs = [];
        return entity.lobs;
    }

    def listModel = [
        fetchList: { o->
            return getItems();
        },
        onRemoveItem: { o->
            if(!showadd) return false;
            if(!o.assessmenttype.matches('NEW')) return false;
            return true;
        }
    ] as BasicListModel;
    
    boolean isAllowAdd() {
        if(!showadd) return false;
        return true;
    }
    
    boolean isAllowRemove() {
        if(!showadd) return false;
        if(!selectedItem) return false;
        if(!selectedItem.assessmenttype.matches('NEW|RENEW|RETIRE')) return false;
        return true;
    }
    boolean isAllowRenew() {
        if(!showrenew) return false;
        if(!selectedItem) return false;
        if(selectedItem.assessmenttype.matches('RENEW|RETIRE')) return false;
        return true;
    }
    boolean isAllowRetire() {
        if(!showretire) return false;
        if(!selectedItem) return false;
        if(!selectedItem.assessmenttype.matches('RENEW|NEW')) return false;
        return true;
    }
    
    boolean isAllowReclassify() {
        if(!showreclassify) return false;
        if(selectedItem.assessmenttype.matches('RETIRE')) return false;
        return true;
    }
    
     boolean isAllowUnretire() {
        if(!showretire) return false;
        if(!selectedItem) return false;
        if(selectedItem.assessmenttype != 'RETIRE') return false;
        return true;
    }
    
    def add() {
        return Inv.lookupOpener( "lob:lookup", [
            onselect: { o->
                if(items.find{ it.lobid == o.objid }!=null) 
                    throw new Exception("Item already added");
                def m = [:];
                m.objid = "BIZLOB"+ (new UID());
                m.lobid = o.objid;
                m.name = o.name;
                m.classification = o.classification?.objid;
                m.assessmenttype = 'NEW';
                items << m; 
                listModel.reload();
                if(handler) {
                    handler( m, "add" );
                }
            }
        ]);        
    }
    
    def reclassify() {
        return Inv.lookupOpener( "lob:lookup", [
            onselect: { o->
                if( selectedItem.lobid == o.objid )
                    throw new Exception("Please select an item that is different from the existing classification");
                if(items.find{ it.lobid == o.objid }!=null) 
                    throw new Exception("Item already added");
                selectedItem.lobid = o.objid;   
                selectedItem.name = o.name;
                selectedItem.classification = o.classification?.objid;
                listModel.reload();
                if(handler) {
                    handler( selectedItem, "reclassify" );
                }
            }
        ]);        
    }
    
    void remove() {
        if( !selectedItem ) return;
        items.remove(selectedItem);
        listModel.reload();
        if(handler) {
            handler(selectedItem, "remove");
        }
    }
    
    void renew() {
        if(!selectedItem) return;
        selectedItem.assessmenttype = 'RENEW';
        listModel.reload();
        if(handler) {
            handler(selectedItem, "renew");
        }
    }
    
    void retire() {
        if(!selectedItem) return;
        if(!selectedItem.assessmenttype?.matches('NEW|RENEW')) {
            throw new Exception("Assessment Type must be NEW or RENEW");
        }
        selectedItem.prevassessmenttype = selectedItem.assessmenttype;
        selectedItem.assessmenttype = 'RETIRE';
        listModel.reload();
        if(handler) {
            handler(selectedItem, "retire");
        }
    }
    
    void unretire() {
        if(!selectedItem) return;
        selectedItem.assessmenttype = selectedItem.prevassessmenttype;
        listModel.reload();
        if(handler) {
            handler(selectedItem, "unretire");
        }
    }
    
}