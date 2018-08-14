package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.models.*;

class MemorandaTemplateLookupModel extends ExprLookupEditorModel
{
    @Service('QueryService')
    def query;
    
    def schemaname = 'rptis_memoranda_template';
    def hidedepreciate = true;
    
    def list;
    
    void init(){
        def params = [_schemaname:schemaname];
        params.where = ['1=1'];
        params.orderBy = 'code';
        list = query.getList(params);
    }
    
    List fetchList(Map params){
        return list;
    }
    
    def getVariables(){
    }
    
    def buildParamList(){
        params = [];
        selectedItem.item.fields.each{
            params << [param:it];
        }
    }    
    
    Column[] getColumns() {
        return [
            new Column(name:"code", caption:"Code", width:80),
            new Column(name:"template", caption:"Template", width:150),
        ]
    }
        
}