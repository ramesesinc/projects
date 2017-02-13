package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;
import com.rameses.osiris2.reports.*;

/************************************************************************
* This abstract class is extended by 
*************************************************************************/
public abstract class AbstractBusinessApplicationPageFlow extends PageFlowController {

    void printTrackingno() {
        def info = [:];
        info.trackingno = entity.barcode;
        if( !info.trackingno) info.trackingno = '51005:'+entity.appno;
        info.message = "Present this when doing business transactions";
        //Modal.show( "show_trackingno", [info: info] );
        
        def op = Inv.lookupOpener("show_trackingno", [info: info]);
        op.handle.print();
        //ReportUtil.print( "", Object reportData, boolean withPrintDialog )
        
    }

}