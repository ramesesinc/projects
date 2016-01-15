package com.rameses.clfc.admin.roletemplate

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class RoleTemplateUserInfoController 
{
    @Service("RoleTemplateService")
    def service;
    
    def entity, domainList, templateList;
    def selectedTemplate, selectedDomain;
    
    void init() {
        templateList = service.getTemplatesByUser([userid: entity.objid]);
    }
    
    void setSelectedTemplate( selectedTemplate ) {
        this.selectedTemplate = selectedTemplate;
        if (selectedTemplate) {
            domainList = selectedTemplate.domains;
        } else if (!selectedTemplate) {
            domainList = [];
        }
    }
    
    def templateListHandler = [
        fetchList: { o->
            if (!templateList) templateList = [];
            return templateList;
        }
    ] as BasicListModel;
    
    def listHandler = [
        fetchList: { o->
            if (!selectedDomain) return [];
            return selectedTemplate?.roles.findAll{ it.domain==selectedDomain }
        }
    ] as BasicListModel;
    
    def addTemplate() {
        def handler = { o->
            if (!templateList) templateList = [];
            
            def item = templateList.find{ it.objid==o.objid }
            if (item) throw new Exception("Template has already been selected.");
            
            item = service.saveTemplate([templateid: o.objid, userid: entity.objid]);
            if (!templateList) templateList = [];
            templateList.add(item);
            templateListHandler?.reload();
        }
        return Inv.lookupOpener('roletemplate:lookup', [onselect: handler, state: 'ACTIVE']);
    }
    
    void removeTemplate() {
        if (!MsgBox.confirm("You are about to remove this template. Continue?")) return;
        
        def params = [
            templateid      : selectedTemplate?.objid,
            usertemplateid  : selectedTemplate?.usertemplateid,
            userid          : entity.objid
        ]
        service.removeTemplate(params);
        templateList?.remove(selectedTemplate);
        templateListHandler?.reload();
    }
}

