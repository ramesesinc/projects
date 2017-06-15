package com.rameses.util.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.io.*;
import org.apache.commons.io.*;
import util.QRGenerator;

class QRCodeGeneratorModel extends ComponentBean {

    def image;
    def entity;

    def doGenerate(){
        def data = getValue();
        if(!data){
            MsgBox.alert('QR Data is required!');
            return;
        }
        QRGenerator qr = new QRGenerator();
        qr.createQrCode(data);
        File file = new File("QR.png");
        image = IOUtils.toByteArray(new FileInputStream(file));
        binding.refresh();

        //print
        entity = [:];
        entity.image = qr.getImageInputStream();
        Inv.lookupOpener("report:qr",[entity:entity]);
    }

}