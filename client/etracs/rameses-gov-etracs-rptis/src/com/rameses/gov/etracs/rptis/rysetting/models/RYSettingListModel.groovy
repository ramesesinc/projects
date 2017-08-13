package com.rameses.gov.etracs.rptis.rysetting.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.ServiceLookup;

public class RYSettingListModel extends ListController
{
    @Invoker
    def inv;
    
    def localsvc; 
    
    def params = [:]
    
    public boolean isAllowCreate() { 
        if (getDataList()==null)
            return true;
        return getDataList().size() == 0;
    } 
    
    public String getServiceName(){
        return inv.properties.serviceName;
    }
    
    String getEntityName(){
        return inv.properties.entityName;
    }  
    
    Column[] getColumns(){ 
        return [
            new Column(name:'ry', caption:'Revision Year', alignment:'left', format:'0000', width:120),
            new Column(name:'appliedto', caption:'Applied To', width:250),
            new Column(name:'remarks', caption:'Remarks', width:250),
        ]
    }
    
    public def copy(){
        if (MsgBox.confirm('Create a copy of the selected setting?')){
            svc.copy(selectedEntity);
            MsgBox.alert('Setting has been successfully copied.')
            search();
        }
    }
    
    public def doRevise(){
        svc.revise(selectedEntity, params);
        MsgBox.alert('Setting has been successfully revised.')
        search();
        return '_close';
    }
    
    public def revise(){
        if (MsgBox.confirm('Create a revision of the selected setting?')){
            return new PopupOpener(outcome:'revisepage')
        }
    }
    
    def getSvc(){
        if (!localsvc){
            localsvc = ServiceLookup.create(getServiceName());
        }
        return localsvc;
    }
}

