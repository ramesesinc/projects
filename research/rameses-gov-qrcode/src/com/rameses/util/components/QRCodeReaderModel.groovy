package com.rameses.util.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.camera.*;
import util.*;
import org.apache.commons.io.FileUtils;

class QRCodeReaderModel extends ComponentBean {

    def photo;
    def thumbnail;

    def addPhoto() {
        def h = [ 
            onselect : { o->
                photo = o;
                thumbnail = ImageUtil.scale(o, 250, 250);
                binding.refresh();

                FileUtils.writeByteArrayToFile(new File("PHOTO.jpg"), photo);

                QRGenerator qr = new QRGenerator();
                String result = qr.readQRCode(photo);
                MsgBox.alert('RESULT : ' + result);
            }
        ] as CameraModel;
        WebcamViewer.open( h ); 
    }

}