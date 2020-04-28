package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class MachineSmvLookupModel extends com.rameses.gov.etracs.rptis.models.ExprLookupEditorModel
{
    @Service("MachRYSettingLookupService")
    def svc
    
    def lguid;
    def barangayid;
    def ry;
    def hidedepreciate = true;
    
    List fetchList(Map params){
        params.lguid = lguid;
        params.barangayid = barangayid;
        params.ry = ry;
        return svc.lookupMachineSmvs(params);
    }
    
    def getVariables(){
        return svc.getVariables()
    }
    
    Column[] getColumns() {
        return [
            new Column(name:"code", caption:"Code", width:80),
            new Column(name:"name", caption:"Name", width:150),
            new Column(name:"smv.expr", caption:"Expr", width:250),
        ]
    }
}
    