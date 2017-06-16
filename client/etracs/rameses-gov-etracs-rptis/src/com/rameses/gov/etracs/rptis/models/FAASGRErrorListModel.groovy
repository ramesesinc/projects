package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class FAASGRErrorListModel extends com.rameses.seti2.models.CrudListModel 
{
    @Service('GeneralRevisionService')
    def grSvc;

    def revise(){
        if (MsgBox.confirm('Revise selected FAAS?')){
            selectedItem.datacapture = true;
            selectedItem.faas = [objid:selectedItem.objid] 
            def faas = grSvc.createDataCaptureGeneralRevision(selectedItem);
            binding?.refresh();
            return InvokerUtil.lookupOpener('faas:capture:gr', [entity:faas]);
        }
        return null;
    }
}

