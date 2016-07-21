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

class ImageComponentModel extends ComponentBean implements IPhotoComponent {

    @Binding
    def binding;

    def handler;

    boolean allowCapture = true;
    boolean allowCamera = true;
    boolean allowChange = true;
    boolean generateThumbnail = true;

    def doCapture() {
        def jfc = new JFileChooser();
        int retval = jfc.showOpenDialog(null); 
        if (retval == JFileChooser.APPROVE_OPTION) {
            def file = jfc.getSelectedFile();
            image = StreamUtil.toByteArray(new FileInputStream(file));
            def m = [image:image];
            if( generateThumbnail ) {
                m.thumbnail = ImageUtil.instance.createThumbnail( image );
            }
            if(handler) handler( m );
            binding.refresh('image'); 
        } 
    }

    def doCamera() {
        def h = [ 
            onselect : { o->
                image = o;
                def m = [image: o];
                if( generateThumbnail ) {
                    m.thumbnail = ImageUtil.instance.createThumbnail(o);
                }
                if(handler) handler( m );
                binding.refresh("image");
            }
        ] as CameraModel;
        WebcamViewer.open( h ); 
    }

    void changeImage() {
        image = null;
    }

    void clear() {
        image = null;
        binding?.refresh();
    }

    void updateImage( def o ) {
        image = o;
        binding?.refresh();
    }

    def showBlank() {
        return "blank";
    }
    
    public def getImage() {
        return getValue();
    }
    
    public void setImage( def o ) {
        setValue( o );
    }
    
    public void setShowThumbnail(boolean b) {
        generateThumbnail = b;
    }
    
}