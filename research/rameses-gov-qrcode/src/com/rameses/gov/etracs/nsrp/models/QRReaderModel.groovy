package com.rameses.gov.etracs.nsrp.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.camera.*;
import com.rameses.seti2.models.*;
import util.QRGenerator;
import java.io.*;
import com.github.sarxos.webcam.Webcam;
import util.*;

class QRReaderModel
{
    @Binding
    def binding;

    def title = 'QR Reader';
    def reader; 
    def image;
    Webcam webcam;
    
    void init(){

    }

}