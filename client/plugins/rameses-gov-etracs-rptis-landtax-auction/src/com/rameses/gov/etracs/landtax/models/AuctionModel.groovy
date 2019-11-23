package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionModel extends CrudFormModel
{
    @Service('PropertyAuctionService')
    def svc;
    
    def sections;
    def section;
    
    
    public void afterCreate(){
        try{
            svc.checkOpenAuction();
            loadSections();
        }
        catch(Exception ex){
            mode = 'read';
            throw ex;
        }
    }
    
    public void afterOpen(){
        loadSections();
    }
    
    void loadSections(){
        sections = Inv.lookupOpeners('propertyauction:section', [entity:entity])
    }
    
    def getOpener(){
        try{
            section.handle.init();
        }
        catch(Exception e){
            e.printStackTrace()
        }
        return section;
    }
    
    
    void submitForApproval(){
        if (MsgBox.confirm('Submit for approval?')){
            entity.putAll(svc.submitForApproval(entity));
            reload();
        }
    }
    
    void approve(){
        if (MsgBox.confirm('Approve auction?')){
            entity.putAll(svc.approve(entity));
            reload();
        }
    } 
    
    void publish(){
        if (MsgBox.confirm('Publish NSDRP?')){
            entity.putAll(svc.publish(entity));
            reload();
        }
    }
    
    void generateFirstPublication() {
        if (MsgBox.confirm('Generate first publication?')){
            entity.putAll(svc.generateFirstPublication(entity));
            reload();
        }
    }
    
    void generateSecondPublication() {
        if (MsgBox.confirm('Generate second publication?')){
            entity.putAll(svc.generateSecondPublication(entity));
            reload();
        }
    }
    
    void conductAuction(){
        if (MsgBox.confirm('Conduct auction?')){
            svc.conductAuction(entity)
            reload();
            reloadSections();
        }
    }    
    
    void reloadSections(){
        sections?.each{
            try{
                it.reload();
            }
            catch(e){
                //
            }
        }
    }
    
    void closeAuction(){
        if (MsgBox.confirm('Close auction?')){
            entity.putAll(svc.closeAuction(entity))
        }
    }
    
    void concludeAuction(){
        if (MsgBox.confirm('Close auction?')){
            entity.putAll(svc.concludeAuction(entity))
        }
    }
    
}