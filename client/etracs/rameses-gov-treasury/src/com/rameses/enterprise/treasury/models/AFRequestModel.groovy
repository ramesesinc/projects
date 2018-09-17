package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AFRequestModel extends CrudFormModel {

    @Invoker
    def invoker;
    
    def selectedItem;
    
    String printFormName = "afris";
    
    void afterCreate() {
        entity.state = 'DRAFT';
        entity.reqtype = invoker.properties.reqtype;
        entity.itemclass = 'AF';
        entity.items = [];
        
        def env = OsirisContext.env;
        if ( env.ORGROOT == 0 ) {
            entity.respcenter = [
                objid: env.ORGID, 
                name:  env.ORGNAME
            ];
        }
    }
    
    public boolean isEditAllowed() { 
        if( entity.state == 'CLOSED') return false;
        return super.isEditAllowed();
    }
    
    public void afterColumnUpdate(String name, def o, String colName ) {
        if( colName == "item" )  {
            o.item.objid = o.item.itemid
            o.unit = o.item.unit;
        }        
    }
    
    def getPrintFormData() { 
        return entity; 
    }
    def getReportForm() {
        def path = 'com/rameses/gov/treasury/ris/report/';
        return [
            mainreport: path + 'ris.jasper', 
            subreports: [
                [name:'ReportRISItem', template:path + 'risitem.jasper']
            ]
        ];
    }
} 