package com.rameses.gov.etracs.rpt.transmittal.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.util.*;


public class FAASTransmittalExporter extends AbstractTransmittalExporter
{
    public FAASTransmittalExporter(def writer){
        super(writer);
    }
    
    public void export(item){
        doShowInfo('Initializing FAAS export.\n');
        item.refno = (item.tdno ? item.tdno : item.fullpin);
        doShowInfo('Exporting FAAS ' + item.refno + '... ');
        def data = [
            filetype      : 'faas',
            transmittalid : item.transmittalid, 
            faas          : getFaas(item),
            signatories   : getSignatories(item),
            requirements  : getRequirements(item),
            images        : getImages(item),
        ]
        writer.writeObject(data);
        doShowInfo('done.\n');
        doSleep();
    }
    
}