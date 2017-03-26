package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class FAASGRErrorModel 
{
    @Binding
    def binding;
    
    @Service("BatchGRService")
    def svc
    
    @Service('GeneralRevisionService')
    def grSvc;
    
    
    
    String title = 'Batch General Revision Errors'
    
    def errors;
    def selectedItem;
            
    void init(){
        errors = svc.getBatchGRErrors();
    }
    
    void refresh(){
        init();
        listHandler.reload();
    }
    
    def listHandler = [
        fetchList : { return errors }
    ] as BasicListModel
    
    void setSelectedItem(item){
        selectedItem = item;
        if (item){
            item.msg = svc.getErrorMessage(item);
        }
    }
    
    
    def revise(){
        if (MsgBox.confirm('Revise selected FAAS?')){
            selectedItem.datacapture = true;
            selectedItem.faas.objid = selectedItem.objid 
            def faas = grSvc.createDataCaptureGeneralRevision(selectedItem);
            refreshList();
            return InvokerUtil.lookupOpener('faas:capture:gr', [entity:faas]);
        }
        return null;
    }
    
    void refreshList(){
        def item = errors.find{it.objid == selectedItem.objid}
        if(item){
            errors.remove(item)
            listHandler.reload();
        }
        else{
            init();
            listHandler.reload();
        }
    }
}

