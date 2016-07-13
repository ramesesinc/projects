package com.rameses.entity.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class IndividualEntityMatchListModel extends ComponentBean {

    @Service("IndividualEntityNameMatchService")
    def matchService;
    
    boolean hasMatch;
    def matchList;
    def selectedItem;
    
    void checkHasMatch() {
        hasMatch = false;
        matchList =  matchService.getMatches(entity);
        if(matchList.size()>0){
            hasMatch = true;
            selectedItem = matchList[0];
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

    def getInfo() {
        return entity;
    }

    def getSelectedPhoto() {
        if(!selectedItem) return null;
        if(!selectedItem.photo) {
            selectedItem.photo = service.getPhoto( [objid: selectedItem.objid] );
        }
        return selectedItem.photo;
    }

    void validateAdd() {
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
    }
    
}
