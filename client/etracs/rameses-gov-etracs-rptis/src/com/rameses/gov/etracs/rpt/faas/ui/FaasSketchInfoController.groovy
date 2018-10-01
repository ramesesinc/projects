package com.rameses.gov.etracs.rpt.faas.ui;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;

class FaasSketchInfoController
{
    @Binding
    def binding;
    
    @Service('RealPropertyService')
    def rpSvc;

    def svc;
    
    String title = 'Sketch'
    
    def MODE_READ = 'read';
    def MODE_EDIT = 'edit';
    
    def entity;
    def image;
    def mode;
    
       
    void init(){
        mode = MODE_READ;
        image = DBImageUtil.getInstance().getImage(entity.objid);
    }

    void initNew() {
        mode = MODE_EDIT;
    }
    
    void afterUpload(){}
    void afterRemove(){}
    
    def uploadSketch(){
        return InvokerUtil.lookupOpener('upload:image', [
            objid           : entity.objid,
            entity          : entity,
            autoClose       : true,
            showheader      : false,

            afterupload: {
                image = DBImageUtil.getInstance().getImage(entity.objid);
                afterUpload();
                binding.refresh('.*');
            }
        ]);
    }
    
    void removeSketch(){
        if ( MsgBox.confirm('Remove sketch?') ) {
            DBImageUtil.getInstance().deleteImage(entity.objid);
            image = null;
            afterRemove();
            binding.refresh('.*');
        }
    }
    
    void paste(){
        def header = [objid:entity.objid, refid:entity.objid, title:title];
        if ( DBImageUtil.getInstance().saveClipboardImage( header) > 0 ){
            image = DBImageUtil.getInstance().getImage(entity.objid);
            afterUpload();
            binding.refresh('.*');
        };
    }
    
    def back(){
        return "_close";
    }
    
    def getAllowEdit(){
        if (entity.state.matches('CURRENT|CANCELLED')) return false;
        if (entity.datacapture==1 || entity.datacapture==true) return true;
        if (!entity.taskstate) return false;
        if (entity.taskstate.matches('assign.*|provapprover')) return false;
        return true;
    }
    
    
    def boundary = [:]
    
    void edit(){
        boundary.north = entity.rp.north;
        boundary.east = entity.rp.east;
        boundary.west = entity.rp.west;
        boundary.south = entity.rp.south;
        mode = MODE_EDIT;
    }
    
    void cancel(){
        entity.rp.north = boundary.north;
        entity.rp.east = boundary.east;
        entity.rp.west = boundary.west;
        entity.rp.south = boundary.south;
        mode = MODE_READ;
    }
    
    void update(){
        rpSvc.updateBoundaries(entity.rp);
        mode = MODE_READ;
    }

    def oncloseSketch = {
        binding.fireNavigation("_close");
    }

    def convertToDrawing() {
        if (MsgBox.confirm('Are you sure you want to convert image sketch to drawing format?')){
            return Inv.lookupOpener('sketch:drawing:open', [
                entity : entity,
                oncloseSketch: oncloseSketch,
            ])
        }
        return null;
    }
    
}
