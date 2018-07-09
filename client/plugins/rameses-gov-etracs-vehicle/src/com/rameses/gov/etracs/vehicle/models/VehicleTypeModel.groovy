package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.gov.etracs.vehicle.models.*;
import com.rameses.enterprise.models.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO; 

public class VehicleTypeModel extends CrudFormModel {
    
    @Service("SchemaService")
    def schemaSvc;
    
    def selectedCluster;
    
    def includedFields = [];
    def includedField;
    def excludedFields = [];
    def excludedField;
    
    void afterCreate() {
        entity.issued = 0;
        entity.allowedfields = ".*";
    }

    void afterOpen() {
        schemaSvc.getSchema( [name:"vehicle_application_unit" ] )?.fields.findAll{it.included == "true"}.collect{
            if(it.name.contains("_")) it.name = it.name.split("_")[0];
            def m = [name: it.name, caption: it.caption];
            if( m.name.matches(entity.allowedfields)) {
                includedFields << m;
            }
            else {
                excludedFields << m;
            }
        };
    }
    
    void addCluster() {
        def h = { m->
            m._schemaname = 'vehicletype_cluster';
            m.vehicletype = entity.objid;
            m.issued = 0;
            persistenceService.create(m);
            clusterList.reload();
        }    
        Modal.show("vehicletype_cluster", [handler: h] );
    }
    
    void removeCluster() {
        if(!selectedCluster?.objid) return;
        def m = [_schemaname: 'vehicletype_cluster'];
        m.objid = selectedCluster.objid;
        persistenceService.removeEntity(m);
        clusterList.reload();
    }
    
    def clusterList = [
        fetchList: { o->
            def m = [_schemaname: 'vehicletype_cluster'];
            m.findBy = [vehicletype: entity.objid];
            return queryService.getList(m);
        }
    ] as BasicListModel;
    
    void addImage() {
        def jfc = new JFileChooser();
        int retval = jfc.showOpenDialog(null); 
        if (retval == JFileChooser.APPROVE_OPTION) {
            def iicon = new ImageIcon(jfc.getSelectedFile().toURI().toURL());
            def bi = new BufferedImage(iicon.getIconWidth(), iicon.getIconHeight(), BufferedImage.TYPE_INT_ARGB); 
            def g = bi.createGraphics(); 
            g.drawImage(iicon.getImage(), 0, 0, null); 
            g.dispose(); 
            
            def baos = new java.io.ByteArrayOutputStream(); 
            ImageIO.write(bi, "JPG", baos); 
            entity.icon = baos.toByteArray(); 
        } 
    }
    
    
    void addField() {
        if( !excludedField) return;
        includedFields << excludedField;
        excludedFields.remove( excludedField );
    }
    
    void removeField() {
        if( !includedField ) return;
        excludedFields << includedField;
        includedFields.remove( includedField );
    }
    
    void beforeSave( def o ) {
        entity.allowedfields = includedFields*.name.join("|");
    }
}