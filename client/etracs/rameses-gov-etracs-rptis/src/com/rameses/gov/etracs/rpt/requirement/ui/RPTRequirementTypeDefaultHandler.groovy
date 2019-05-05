package com.rameses.gov.etracs.rpt.requirement.ui;
        
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import javax.swing.JFileChooser;
        
        
public class RPTRequirementTypeDefaultHandler 
{
    @Binding 
    def binding;
   
    @Caller
    def caller; 
    
    def svc;   // RPTRequirementService
            
    def onupdate;
            
            
    def entity;
            
    def MODE_READ = 'read';
    def MODE_EDIT = 'edit';
            
    def mode; 
    def images;
    def selectedItem;
            
            
    @PropertyChangeListener 
    def listener = [
                'entity.value.txnno' : { o ->
            entity.complied = 0;
            if (o) entity.complied = 1;
        }
    ]
            
    void init(){
        if ( ! entity.value )
        entity.value = [:];
        loadImages();    
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
            
    public String getCaption(){
        return entity.requirementtype.name + ' Detail'
    }
            
    void loadImages(){
        images = [];
        try{
            images = DBImageUtil.getInstance().getImages(entity?.objid);    
        }
        catch(e){
            println 'Load Images error ============';
            e.printStackTrace();
        }
        listHandler?.load();
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
            
    def listHandler = [
        getRows  : { return (images.size() + 1 )},
        fetchList : { return images },
    ] as BasicListModel
            
            
    def addImage(){
        return InvokerUtil.lookupOpener('upload:image', [
                entity : entity,
                parentid : entity.refid, 
                afterupload: {
                    loadImages();
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
        selectedItem.info = svc.getEntityInfo(entity.refid)
        return InvokerUtil.lookupOpener('image:view', [
                entity : selectedItem,
            ]);
    }
            
}
        