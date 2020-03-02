package com.rameses.gov.etracs.rptis.models;
                
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class AttachmentModel
{
    @Binding
    def binding;

    @Service('FAASHistoryService')
    def historySvc;

    @Service('RPTAttachmentService')
    def attachmentSvc;

    def entity;
    def attachments;
    def historyList;
    def selectedFaas;
    
    void init() {
        historyList = historySvc.getHistory([objid: entity.objid, tdno: entity.tdno]); 
        attachments = getAttachments([objid: entity.objid]);
    }

    def getAttachments(docref) {
         return attachmentSvc.getAttachments(docref);
    }

    void setSelectedHistory(faas) {
        selectedFaas = faas;
        reloadAttachments();
    }

    void reloadAttachments() {
        attachments = getAttachments([objid: selectedFaas.objid]);
        attachmentHandler.reload();
    }

    def getSelectedHistory() {
        return selectedFaas;
    }

    def getDocRefNo() {
        return (entity.tdno ? entity.tdno : entity.fullpin);
    }

    def historyListHandler = [
        fetchList: { historyList },
        getRows: { historyList.size() },
    ] as BasicListModel

    def attachmentHandler = [
        fetchList: { attachments },
        addItem: { o ->
            o.docrefid = entity.objid;
            o.docrefno = getDocRefNo();
            attachmentSvc.create(o)
        },
    ] as FileViewModel;
}

