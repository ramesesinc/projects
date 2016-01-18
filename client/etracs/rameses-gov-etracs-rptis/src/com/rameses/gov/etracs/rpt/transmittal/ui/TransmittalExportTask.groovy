package com.rameses.gov.etracs.rpt.transmittal.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rpt.util.*;


public class TransmittalExportTask implements Runnable{
    def exportSvc;
    def entity;
    def file;
    def items;
    def oncomplete;
    def onerror;
    def showinfo;
    def writer;
    

    public void run(){
        writer = new ObjectWriter(file);
        try{
            exportTransmittal();
            items.each{
                def exporter = TransmittalFactory.createExporter(it.filetype, writer);
                exporter.service = exportSvc;
                exporter.showInfo = showinfo;
                exporter.export(it)
            }
            writer.close();
            oncomplete('Transmittal is successfully exported.');
        }
        catch(e){
            writer.cancel();
            onerror('\n\n' + e.message )
        }
    }
    
    void exportTransmittal(){
        showinfo('Exporting Transmittal ' + entity.txnno + '... ');
        def data = [
            filetype    : 'transmittal',
            transmittal : entity,
            items       : exportSvc.getTransmittalItems(entity),
        ]
        writer.writeObject(data);
        showinfo('done.\n');
    }
    
}