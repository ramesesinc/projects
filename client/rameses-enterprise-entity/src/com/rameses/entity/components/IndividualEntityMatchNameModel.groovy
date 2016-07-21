package com.rameses.entity.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class IndividualEntityMatchNameModel extends ComponentBean {

    @Service("IndividualEntityNameMatchService")
    def matchService;

    @Service("QueryService")
    def qryService;
    
    def caller;
    
    boolean hasMatch;
    def matchList;
    def selectedItem;
    
    def mode = "ask-name";
    
    def onselect;
    def oncreate;
    
    boolean allowSelect = true;
    
    def getEntity() {
        return caller.entity;
    }
    
    void verifyName() {
        boolean b = checkHasMatch();
        if( b ) {
            mode = "show-list";
        }
        else {
            mode = "ask-name";
            if(oncreate) oncreate(entity);
        }
    }
    
    void back() {
        mode = "ask-name";
    }
    
    boolean checkHasMatch() {
        matchList =  matchService.getMatches(entity);
        if( entity.objid ) {
            def r = matchList.find{ it.objid == entity.objid };
            if(r) matchList.remove(r);
        }
        if(matchList.size()>0){
            selectedItem = matchList[0];
            return true;
        }    
        else {
            return false;
        }
    }

    def listModel = [
        fetchList: { o->
            return matchList;
        }
    ] as BasicListModel;

    def selectMatchedEntity() {
        if(!selectedItem) return;
        onselect( selectedItem );
        return "_close";
    }


    def getSelectedPhoto() {
        if(!selectedItem) return null;
        if(!selectedItem.photo) {
            def m = [_schemaname:'entityindividual'];
            m.select = 'photo';
            m.findBy = [objid: selectedItem.objid];
            def pho = qryService.findFirst( m )?.photo;
            selectedItem.photo = pho;
        }
        return selectedItem.photo;
    }

    void add() {
        if( matchList.find{ it.match == 100.0 } ) {
            boolean allowed = false;
            try {
                def test = Inv.lookupOpener( "entityindividual:approveduplicate", [:] );
                if(test) allowed = true;
            }
            catch(e) {;}
            if( !allowed )  {
                throw new Exception("There is an exact match for the record. You do not have enough rights to override.");                    
            }    
        } 
        mode = "ask-name";
        oncreate( entity );
    }
    
    void select() {
        if(!selectedItem?.objid) throw new Exception("Please select an item");
        mode = "ask-name";
        onselect( selectedItem );
    }
    
}
