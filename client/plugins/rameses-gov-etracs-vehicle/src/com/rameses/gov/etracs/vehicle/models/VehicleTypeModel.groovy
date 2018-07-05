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
    
    def selectedCluster;
    def _guiHandlers; 
    
    void afterCreate() {
        entity.issued = 0;
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
    
    public def getGuiHandlers() {
        if(_guiHandlers) return _guiHandlers;
        _guiHandlers = Inv.lookupOpeners( "vehicle:guihandler" ).collect { it.properties.name }
        return _guiHandlers;
    }
    
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
}