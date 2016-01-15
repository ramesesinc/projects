package com.rameses.clfc.encashment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class EncashmentCBSMainController extends CRUDController
{
    String serviceName = "EncashmentCBSService";
    String entityName = "encashmentcbs";

    def opener, selectedOption, optionList = [];

    boolean allowDelete = false;
    boolean allowApprove = false;
    boolean allowEdit = false;
    boolean allowCreate = false;    

    def optionsModel = [
        getItems: {
            if (!optionList) {
                optionList = [];
                def list = Inv.lookupOpeners('encashment-cbs-plugin');
                def props;
                list?.each{ o->
                    props = o.properties;
                    if (props) {
                        optionList.add(props);
                    }
                }
                optionList?.each{ o->
                    o.opener = Inv.lookupOpener('encashment:cbs:' + o.name, [entity: entity]);
                }
                optionList?.sort{ it.index }
            }
            return optionList;
        }, 
        onselect: { o->
            opener = o.opener;
            binding.refresh('opener');
        }
    ] as ListPaneModel;

    def close() {
        return "_close";
    }
    
}
/*
class EncashmentCBSMainController
{
    @Binding
    def binding;
    
    @Service('EncashmentCBSService')
    def service;
    
    String getEntityName = "encashmentcbs";
    
    def entity, optionList, opener, selectedOption;
    void open() {
        entity = service.open(entity);
    }
    
    def optionsModel = [
        getItems: {            
            if (!optionList) {
                optionList = [];
                def list = Inv.lookupOpeners('encashment-cbs-plugin');
                def props;
                list?.each{ o->
                    props = o.properties;
                    if (props) {
                        optionList.add(props);
                    }
                }
                optionList?.each{ o->
                    o.opener = Inv.lookupOpener('encashment:cbs:' + o.name, [entity: entity]);
                }
                optionList?.sort{ it.index }
            }
            return optionList;
        }, 
        onselect: { o->
            opener = o.opener;
            binding.refresh('opener');
        }
    ] as ListPaneModel;

    def close() {
        return "_close";
    }
}
*/
