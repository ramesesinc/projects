package com.rameses.gov.etracs.rpt.faas.change.ui;
        

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import javax.swing.JFileChooser;


public class AddFaasRequirementDocumentController 
{
    String title = 'Add Requirement Document';
    
    @Invoker
    def invoker;
    
    @Service('RPTRequirementService')
    def reqSvc;
    
    def entity; //faas 
    
    def requirement = [:]; 
    def requirements;
    def changeinfo = [:]
    def images;
    def selectedItem;
    def oldimageids = [];
    def imagepass = false;
    
    
    void init(){
        if (entity._redflag.info?.requirementtype == null)
            throw new Exception('Red Flag requirement type info is required.')
            
        changeinfo = [
            objid       : 'CI' + new java.rmi.server.UID(),
            faasid      : entity.objid,
            rpid        : entity.rp?.objid,
            rpuid       : entity.rpu?.objid,
            action      : invoker.properties.actiontype,
            newinfo     : [:],
            previnfo    : [:],
            redflagid   : entity._redflag?.objid,
        ];
        loadRequirement();
    }
    
    void loadRequirement(){
        requirement.putAll(reqSvc.checkAndGetRequirement(entity.objid, entity._redflag.info.requirementtype));
    }
    
    def listHandler = [
        fetchList : { 
            loadImages();
            return images 
        },
    ] as BasicListModel
            
    
    void loadImages(){
        images = [];
        try{
            images = DBImageUtil.getInstance().getImages(requirement?.objid);    
            
            //preserve image ids to distinguish newly added from existing
            if (!imagepass){
                oldimageids = (images ? images.objid : []);
                imagepass = true;
            }
            
            changeinfo.requirement = requirement;
            changeinfo.imageids = images.objid - oldimageids;
                
        }
        catch(e){
            println 'Load Images error ============';
            e.printStackTrace();
        }
    }
    
    def addImage(){
        return InvokerUtil.lookupOpener('upload:image', [
                entity : requirement,
                    
                afterupload: {
                    loadImages();
                    listHandler?.load();
                }
            ]);
    }
            
    void deleteImage(){
        if (!selectedItem) return;
        if (MsgBox.confirm('Delete selected image?')){
            DBImageUtil.getInstance().deleteImage(selectedItem.objid);
            loadImages();
        }
    }    
           
    def viewImage(){
        if (!selectedItem) return null;
        return InvokerUtil.lookupOpener('image:view', [
                entity : selectedItem,
            ]);
    }    
          
}
       