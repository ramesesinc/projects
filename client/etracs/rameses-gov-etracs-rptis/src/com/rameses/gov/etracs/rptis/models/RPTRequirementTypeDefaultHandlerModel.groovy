package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import javax.swing.JFileChooser;
        
        
public class RPTRequirementTypeDefaultHandlerModel 
{
    @Binding 
    def binding;
   
    @Caller
    def caller; 
    
    def svc;   // RPTRequirementService

    @Service('RPTAttachmentService')
    def attachmentSvc;
            
    def onupdate;
            
            
    def entity;
    def attachment;
            
    def MODE_READ = 'read';
    def MODE_EDIT = 'edit';
            
    def mode; 
            
    void init(){
        if ( ! entity.value )
        entity.value = [:];
        mode = MODE_READ;
    }
            
    void edit(){
        mode = MODE_EDIT;
    }
            
    void update(){
        svc.update(entity);
        if (onupdate) onupdate();
        mode = MODE_READ;
    }

    def complied() {
        entity.complied = true;
        return new PopupOpener(outcome:'complied');
    }
    
    def postComplied() {
        svc.update(entity);
        if (onupdate) onupdate();
        mode = MODE_READ;
        caller?.binding?.refresh('entity.*');
        return '_close';
    }
    
    void uncomplied() {
        if (MsgBox.confirm('Uncomplied selected requirememt?')) {
            entity.complied = false;
            entity.value.txnno = null;
            entity.value.txndate = null;
            entity.value.remarks = null;
            svc.update(entity);
            binding?.refresh('entity.*');
        }
    }

    void deleteTxnAttachment() {
        if (attachmentHandler.selectedCard && MsgBox.confirm('Delete attachment ' + attachmentHandler.selectedCard.title + '?')) {
            def txn = [:];
            txn.refid = entity.objid;
            txn.attachmentid = attachmentHandler.selectedCard.objid;
            attachmentSvc.deleteTxnAttachment(txn);
            attachmentHandler.reload();   
        }
    }

    def getLookupAttachment() {
        return Inv.lookupOpener('attachment:lookup', [
            onselect : { 
                createTxnAttachment(it);
                attachment = null;
                binding.requestFocus('attachment');
                attachmentHandler.reload();
            }, 
            onempty: {
            }
        ])
    }

    def getAttachments() {
         return attachmentSvc.getTxnAttachments([objid: entity.objid]);
    }

    def getSelectedCard() {
        return attachmentHandler.selectedCard;
    }

    def attachmentHandler = [
        fetchList: { getAttachments(); },
        isAllowAdd: { false },
        isAllowRemove: { false },
    ] as FileViewModel;

    void createTxnAttachment(attachment) {
        def txn = [objid: 'AI' + new java.rmi.server.UID()];
        txn.attachmentid = attachment.objid;
        txn.refid = entity.objid;
        attachmentSvc.createTxnAttachment(txn);
    }
}
        