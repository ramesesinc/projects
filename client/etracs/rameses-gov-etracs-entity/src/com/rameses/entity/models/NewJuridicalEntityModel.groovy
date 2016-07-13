package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class NewJuridicalEntityModel extends PageFlowController {
    
    @Service("JuridicalEntityNameMatchService")
    def matchService;

    @Service("PersistenceService")
    def persistenceService;

    def onselect;
    def entity;
    def step;
    boolean hasMatch;
    def matchList;
    def selectedItem;
    def mode = "create";

    def orgTypes = LOV.ORG_TYPES.findAll{ it.key != 'SING' };
    
    def create() {
        doCreate();
        return super.start();
    }

    void doCreate(){
        step = "initial";
        entity = [:];
        entity.objid = 'JUR'+new UID();
        entity.address = [:];
    }
    

    void initNew(){
        entity = null;
        doCreate();
    }

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

    /*
    def getSelectedPhoto() {
        if(!selectedItem) return null;
        if(!selectedItem.photo) {
            selectedItem.photo = service.getPhoto( [objid: selectedItem.objid] );
        }
        return selectedItem.photo;
    }
    */

    void validateAdd() {
        if( matchList.find{ it.match == 100.0 } ) {
            boolean allowed = false;
            try {
                def test = Inv.lookupOpener( "entityjuridical:approveduplicate", [:] );
                if(test) allowed = true;
            }
            catch(e) {;}
            if( !allowed )  {
                throw new Exception("There is an exact match for the record. You do not have enough rights to override.");                    
            }    
        }    
    }

    def saveNew() {
        entity._schemaname = 'entityjuridical';
        entity = persistenceService.create( entity );
        if( onselect ) {
            onselect(entity);
        }    
        MsgBox.alert("Record successfully saved. Entity No is " + entity.entityno );
        return "_close";
    }

    
}
