package com.rameses.gov.etracs.rpt.annotation.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.MapBeanUtils;
import com.rameses.gov.etracs.rptis.util.*;

public class FAASAnnotationController extends PageFlowController
{

    @Binding
    def binding;
    
    @Service('FAASAnnotationService')
    def svc 
    
    @Invoker
    def invoker 
    
    def MODE_CREATE = 'create';
    def MODE_EDIT   = 'edit';
    def MODE_READ   = 'read';
    
    def STATE_INTERIM     = 'INTERIM';
    def STATE_FORAPPROVAL = 'FORAPPROVAL';
    def STATE_APPROVED    = 'APPPROVED';
    def STATE_CANCELLED   = 'CANCELLED';
    
    
    def faas;
    def mode;
    def entity;
    def selectedFaas;
    def images;
    
    String entityName = 'faasannotation';
    
    
    def init(){
        mode = MODE_CREATE
        return super.signal('init')
    }
    
    
    def open(){
        entity = svc.open(entity)
        faasListHandler.reload();
        loadImages();
        mode = MODE_READ;
        return super.signal('open');
    }
    
    def getLookupFaas() {
        return InvokerUtil.lookupOpener('faas:lookup', [:])
    }
    
    
    List getAnnotationTypes(){
        return svc.getAnnotationTypes()
    }
    
    boolean getAllowEdit(){
        if ( entity.state == STATE_APPROVED ||
            entity.state == STATE_CANCELLED )
        return false;
        if (mode == MODE_READ )
        return false;
        return true 
    }

    def faasListHandler = [ 
        fetchList : { return entity.items }
    ] as BasicListModel


    /*-----------------------------------------------------
     * 
     * DOCUMENT SUPPORT 
     *
     *----------------------------------------------------*/
    def oldEntity 
            
    void edit(){
        oldEntity = MapBeanUtils.copy(entity)
        mode = MODE_EDIT;
    }
    
    
    void cancelEdit(){
        if (oldEntity){
            entity.putAll(oldEntity)
        }
        oldEntity = null
        mode = MODE_READ;
    }
    
    void save(){
        if (mode == MODE_CREATE)
        entity = svc.create(entity)
        else 
        entity = svc.update(entity)
        mode = MODE_READ;
    }    
   
    
    void delete(){
        svc.delete(entity)
    }
    
    

    /*-----------------------------------------------------
     * 
     * WORKFLOW ACTIONS
     *
     *----------------------------------------------------*/
    void initAnnotation(){
        entity = svc.init(faas);
    }
    
    
    void submitAnnotationForApproval(){
        entity = svc.submitForApproval(entity);
    }
    
    
    void disapproveAnnotation(){
        entity = svc.disapprove(entity)
    }
    
    
    void approveAnnotation() {
        entity = svc.approve(entity);
    }

    
    
    
    /*-----------------------------------------------------
     * 
     * SUPPORT DOCUMENTS
     *
     *----------------------------------------------------*/
    def selectedImage;
    
    void loadImages(){
        images = [];
        try{
            images = DBImageUtil.getInstance().getImages(entity?.objid);    
        }
        catch(e){
            e.printStackTrace();
        }
        imageListHandler?.load();
    }
    
                
    def imageListHandler = [
        fetchList : { return images },
    ] as BasicListModel
    
    
    def addImage(){
        return InvokerUtil.lookupOpener('upload:image', [
                entity : entity,
                parentid : entity.objid,
                afterupload: {
                    loadImages();
                }
            ]);
    }
            
    void deleteImage(){
        if (!selectedImage) return;
        if (MsgBox.confirm('Delete selected image?')){
            DBImageUtil.getInstance().deleteImage(selectedImage.objid);
            loadImages();
        }
    }
            
            
    def viewImage(){
        if (!selectedImage) return null;
        return InvokerUtil.lookupOpener('image:view', [
                entity : selectedImage,
            ]);
    }
    
}
       