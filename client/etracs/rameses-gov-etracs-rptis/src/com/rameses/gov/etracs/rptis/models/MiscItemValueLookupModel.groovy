package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class MiscItemValueLookupModel extends ExprLookupEditorModel
{
    @Service("MiscRYSettingLookupService")
    def svc
    
    def lguid;
    def barangayid;
    def ry;
    
    List fetchList(Map params){
        params.lguid = lguid;
        params.ry = ry;
        params.barangayid = barangayid;
        return svc.lookupMiscItemValues(params);
    }
    
    def getVariables(){
        return svc.getVariables()
    }
    
    Column[] getColumns() {
        return [
            new Column(name:"miscitem.code", caption:"Code", width:80),
            new Column(name:"miscitem.name", caption:"Name", width:150),
            new Column(name:"expr", caption:"Rate", width:250),
        ]
    }
}