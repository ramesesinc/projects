package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class AttachmentPreviewModel {
    
    @Binding 
    def binding; 
    
    def listeners;
    def entity;
    def image;
    
    void init() {
        listeners << refresh;
        loadImage(); 
    } 
    
    void loadImage() {
        def file = new java.io.File( entity.filepath ); 
        image = new javax.swing.ImageIcon( file.toURI().toURL() );   
    }
    
    def refresh = { o-> 
        entity = o; 
        loadImage(); 
        binding.refresh(); 
    } 
    
    @Close
    void unregister() {
        listeners.clear(); 
    }
} 