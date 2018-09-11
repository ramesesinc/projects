package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


public class AssessmentNoticeModel extends CrudFormModel 
{
    @Service('RPTAssessmentNoticeService')
    def svc;
    def faas;
    
    boolean showNavigation = false;
    
    def viewNoa(){
        faas = entity;
        init();
        entity = [:]
        entity.putAll(svc.loadNotice(entity, faas));
        return open();
    }

    def viewReport() {
        try {
            return super.viewReport()
        } catch(e) {
            println e.message 
        }
        return null
    }

    def listHandler = [
        fetchList : { return entity.items },
    ] as EditorListModel


    def onreceive = {
        entity.putAll(svc.receive(it));
        binding.refresh();
    }

    def receive() {
        return Inv.lookupOpener('assessmentnotice:receive', [
            entity: entity, 
            onreceive: onreceive,
            deliveryTypes: svc.getDeliveryTypes(),
        ]);
    }

    def preview(def handlerName) { 
        return Inv.lookupOpener('assessmentnotice:report', [entity: entity])
    }

}
