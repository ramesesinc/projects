package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.CrudFormModel;

class AFRequestModel extends CrudFormModel {

    @Invoker
    def invoker;
    
    def selectedItem;

    String printFormName = "afris";
    
    public void beforeSave( mode ) {
        if ( !entity.items ) 
            throw new Exception('Please specify at least one item'); 
    } 

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
        
    public void afterColumnUpdate(String name, def o, String colName ) {
        if( colName == "item" )  {
            o.item.objid = o.item.itemid
            o.unit = o.item.unit;
        }        
    }

    public void approve() {
        boolean b = MsgBox.confirm('You about to approve this transaction. Proceed?'); 
        if ( !b ) return; 

        def m = [_schemaname: 'afrequest', _action:'approve'];  
        m.findBy = [ objid: entity.objid ]; 
        m.objid = entity.objid;
        m.state = 'OPEN'; // a mark ready for issuance 
        persistenceService.update( m ); 
        entity.state = m.state;  
    } 

    public void disapprove() {
        boolean b = MsgBox.confirm('You about to disapprove this transaction. Proceed?'); 
        if ( !b ) return; 

        def m = [_schemaname: 'afrequest', _action:'disapprove'];  
        m.findBy = [ objid: entity.objid ]; 
        m.objid = entity.objid; 
        m.state = 'CANCELLED'; // a mark for cancelled trxns
        m.remarks = 'Disapproved RIS'; 
        persistenceService.update( m ); 
        entity.state = m.state;  
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