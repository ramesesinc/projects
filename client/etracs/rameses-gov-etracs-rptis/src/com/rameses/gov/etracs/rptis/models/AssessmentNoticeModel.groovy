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
    def addoption = 'bytd';

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
    
    public void afterCreate(){
        entity.items = [];
        listHandler?.reload();
    }
    
    def save() {
        if (MsgBox.confirm('Save notice?')) {
            entity.items = entity.items.findAll{it.included == true}
            entity.putAll(svc.create(entity));
            open();
            listHandler.reload();
        }
        return 'default'
    }

    def getLookupTaxpayer(){
        return InvokerUtil.lookupOpener('entity:lookup',[
                onselect : {
                    entity.taxpayer = [objid:it.objid, name:it.name, address:it.address.text];
                    entity.taxpayeraddress = it.address.text
                    loadProperties();
                },
                onempty : {
                    entity.taxpayer = null;
                },
        ])
    }
    
    def getLookupFaas(){
        return InvokerUtil.lookupOpener('faas:lookup', [
            taxpayerid : entity.taxpayer.objid, 
            state      : 'CURRENT',
                
            onselect : { faas ->
                if (faas.state != 'CURRENT')
                    throw new Exception('FAAS is not current.')
                    
                if (! entity.items.find{it.faasid == faas.objid}) {
                    faas.faasid = faas.objid;
                    faas.barangay = faas.barangay.name 
                    faas.objid = 'ANI' + new java.rmi.server.UID();
                    faas.assessmentnoticeid = entity.objid;
                    faas.included = true; 
                    entity.items.add(faas);
                    listHandler.load();
                }
            },
        ])
    }
    
    
    void setAddoption(addoption){
        this.addoption = addoption;
        loadProperties();
    }
    
    
    void loadProperties(){
        if (addoption == 'all'){
            entity.items = svc.getApprovedFaasList(entity.taxpayer.objid)
            entity.items.each{
                it.assessmentnoticeid = entity.objid;
                it.included = true;
            }
            listHandler.load();
        }
    }
    
    
    
    void selectAll(){
        entity.items.each{
            it.included = true;
        }
        listHandler.load();
    }
    
    void deselectAll(){
        entity.items.each{
            it.included = false;
        }
        listHandler.load();
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
