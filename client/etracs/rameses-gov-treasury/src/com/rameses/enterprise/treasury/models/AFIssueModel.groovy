package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

class AFIssueModel extends CrudFormModel {

    def selectedItem;
    
    def itemListHandler = [
        fetchList : { o->
            return entity.items;
        }
    ] as BasicListModel;
    
        
    def viewDetails() {
        return Inv.lookupOpener("afissueitem:view", [item:selectedItem] );
    }
    
    public def getInfo() {
        println 'get info ' + entity;
        return TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/templates/AFIssueDetail.gtpl", [entity:entity] );
    }
}    