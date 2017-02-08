package com.rameses.enterprise.components;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.rameses.io.StreamUtil;
import java.io.FileInputStream;   
import com.rameses.rcp.camera.*;
import com.rameses.seti2.models.*;

class PhotoComponentModel extends ComponentBean implements IPhotoComponent {

    @Binding
    def binding;

    def handler;

    boolean allowCapture = true;
    boolean allowCamera = true;
    boolean allowChange = true;
    boolean generateThumbnail = true;

    void crop( def data ) { 
        def h = { o-> 
            photo = o; 
            def m = [image: o];
            if( generateThumbnail ) {
                m.thumbnail = ImageUtil.instance.createThumbnail(o);
            }
            if (handler) handler( m );
            binding.refresh("photo");
        } 
        ImageCropper.show([ image: data, handler: h ]);  
    } 
    
    def doCapture() {
        def jfc = new JFileChooser();
        int retval = jfc.showOpenDialog(null); 
        if (retval == JFileChooser.APPROVE_OPTION) {
            crop( jfc.getSelectedFile() ); 
        } 
    }

    def doCamera() {
        def h = [ 
            onselect : { o-> crop( o ) }
        ] as CameraModel;
        WebcamViewer.open( h ); 
    }

    void changePhoto() {
        photo = null;
    }

    void clear() {
        photo = null;
        binding?.refresh();
    }

    void updatePhoto( def o ) {
        photo = o;
        binding?.refresh();
    }

    def showBlank() {
        return "blank";
    }
    
    public def getPhoto() {
        return getValue();
    }
    
    public void setPhoto( def o ) {
        setValue( o );
    }
    
    public void setShowThumbnail(boolean b) {
        generateThumbnail = b;
    }
    
}