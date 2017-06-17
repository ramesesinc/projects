package com.rameses.entity.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class FingerPrintListModel {

    @Service('EntityFingerPrintService') 
    def fingerPrintSvc; 

    @Binding 
    def binding; 
    
    @Caller 
    def caller; 
    
    def title = 'Finger Print';
    def entity;

    final def base64 = new com.rameses.util.Base64Cipher();
    
    final def types = [         
        [key: 'right_thumb', type: FingerPrintModel.RIGHT_THUMB, title: 'RIGHT THUMB'], 
//        [key: 'right_index', type: FingerPrintModel.RIGHT_INDEX, title: 'RIGHT INDEX'], 
//        [key: 'right_middle', type: FingerPrintModel.RIGHT_MIDDLE, title: 'RIGHT MIDDLE'], 
//        [key: 'right_ring', type: FingerPrintModel.RIGHT_RING, title: 'RIGHT RING'], 
//        [key: 'right_little', type: FingerPrintModel.RIGHT_LITTLE, title: 'RIGHT LITTLE'], 
        [key: 'left_thumb', type: FingerPrintModel.LEFT_THUMB, title: 'LEFT THUMB']  
//        [key: 'left_index', type: FingerPrintModel.LEFT_INDEX, title: 'LEFT INDEX'], 
//        [key: 'left_middle', type: FingerPrintModel.LEFT_MIDDLE, title: 'LEFT MIDDLE'], 
//        [key: 'left_ring', type: FingerPrintModel.LEFT_RING, title: 'LEFT RING'], 
//        [key: 'left_little', type: FingerPrintModel.LEFT_LITTLE, title: 'LEFT LITTLE'] 
    ]; 
            
    def selectedItem;
    def listHandler = [
        fetchList: { 
            fetchFingerPrints(); 
            return types; 
        }, 
        onselect: { o-> 
            
        } 
    ] as ListPaneModel;
    
    def getMasterEntity() {
        try { 
            return caller.entity; 
        } catch(Throwable t) {
            return null; 
        }
    }

    def fingerPrints; 
    private void fetchFingerPrints() {
        if ( fingerPrints == null ) {
            fingerPrints = fingerPrintSvc.getList([ entityid: masterEntity?.objid ]); 
        }
    }

    void refresh() {
        fingerPrints?.clear(); 
        fingerPrints = null; 
        fetchFingerPrints(); 
    }
    
    def fingerPrintHandler = [ 
        getFingerType: {
            return selectedItem.type; 
        }, 
        
        onselect: { o-> 
            def map = [:]; 
            def fp = fingerPrints.find{( it.fingertype.toString().toLowerCase()==selectedItem.key )} 
            map.objid = fp?.objid; 
            map.fingertype = selectedItem.key; 
            map.entityid = masterEntity?.objid;
            
            Object fmdobj = o.getFmdData( selectedItem.type ); 
            map.data = base64.encode( fmdobj );  

            def bytes = o.getData( selectedItem.type ); 
            def scaler = new ImageScaler(); 
            def imageobj = scaler.scale( new javax.swing.ImageIcon(bytes), 300, 300); 
            bytes = scaler.getBytes( imageobj ); 
            map.image = base64.encode((Object) bytes );  
            def resp = fingerPrintSvc.save( map ); 
            if ( map.objid ) {
                fp.image = map.image; 
            } else {
                resp.image = map.image; 
                fingerPrints << resp;
            }
            binding.refresh('image');
        }
    ] as FingerPrintModel; 
    
    void capture() { 
        com.rameses.rcp.fingerprint.FingerPrintViewer.open( fingerPrintHandler );  
    }
    
    void init() {
    }
    
    
    def getImage() { 
        if ( !selectedItem ) return null; 
        if ( !fingerPrints ) return null; 
        
        def o = fingerPrints.find{( it.fingertype.toString().toLowerCase()==selectedItem.key )} 
        if ( o?.actualimage ) return o.actualimage;         
        if ( !o?.image ) return null; 
        if ( o.image instanceof String ) { 
            if ( base64.isEncoded(o.image)) { 
                o.image = base64.decode( o.image ); 
            } 
        } 
        if ( o.image ) o.actualimage = o.image; 
        return o.actualimage;
    }
}