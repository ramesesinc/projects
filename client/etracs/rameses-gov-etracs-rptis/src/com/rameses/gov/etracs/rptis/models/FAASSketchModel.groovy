package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.rcp.draw.figures.ImageFigure
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import com.rameses.rcp.draw.utils.DataUtil;
import com.rameses.rcp.draw.figures.ImageFigure

import com.rameses.rcp.draw.figures.ImageFigure
import java.awt.Rectangle

class FAASSketchModel
{
    @Binding
    def binding;
    
    @Service('RealPropertyService')
    def rpSvc;
    
    @Service('DBImageService')
    def imageSvc;
    
    @Service('FAASService')
    def faasSvc;

    def svc;
    
    String title = 'Sketch'
    
    def MODE_READ = 'read';
    def MODE_EDIT = 'edit';
    
    def entity;
    def mode;
    def drawing;
    def image;
    def converting;
    def oncloseSketch = {}
    def committed;
    
    void init(){
        mode = MODE_READ;
        converting = false;
        committed = false;
        openDrawing();
    }

    void initConvert() {
        init();
        converting = true;
    }
        
    def handler = [
        fetchCategories : {'drawing'},
        fetchData : { drawing },
        isReadonly : { mode == MODE_READ },
    ] as DrawModel;
    
    
    
    def back(){
        if (converting && committed) {
            oncloseSketch()
        }
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
        handler?.refresh();
    }
    
    def cancel(){
        entity.rp.north = boundary.north;
        entity.rp.east = boundary.east;
        entity.rp.west = boundary.west;
        entity.rp.south = boundary.south;
        mode = MODE_READ;
        handler?.refresh();
        return null;
    }
    
    void update(){
        rpSvc.updateBoundaries(entity.rp);
        saveSketch();
        committed = true;
        mode = MODE_READ;
        handler?.refresh();
    }
    
    void saveSketch(){
        if (image && !MsgBox.confirm('Existing image sketch will be replaced. Proceed?')){
            return;
        }
        
        faasSvc.saveSketch([objid:entity.objid, drawing:handler.data]);
        openDrawing();
    }
    
    void openDrawing(){
        def sketch = faasSvc.openSketch([objid:entity.objid]);
        if (sketch && sketch.drawing.figures){
            drawing = sketch.drawing;
        } else {
            createImageFigure();
        }
    }
    
    void createImageFigure(){
        drawing = [connectors:[], figures:[]];
        
        def image_bytes = imageSvc.getImageBytes([objid:entity.objid]);
        image = DataUtil.decodeImage(image_bytes);
        
        if (image){
            def figure = new ImageFigure();
            figure.setImage(image);

            def r = new Rectangle();
            r.x = 5;
            r.y = 5; 
            r.width = image.width;
            r.height = image.height;
            figure.setDisplayBox(r);
            drawing.figures << figure.toMap();
        }
    }
}
