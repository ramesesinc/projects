package com.rameses.gov.etracs.rpt.transmittal.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.util.*;


public class TransmittalFactory
{
    private TransmittalFactory(){
        
    }
    
    public static def createExporter(def type, def writer){
        if (type == 'faas')
            return new FAASTransmittalExporter(writer);
    }
}