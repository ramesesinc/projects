package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rpt.util.*;


public class TransmittalExportTask implements Runnable{
    def entity;
    def file;
    def oncomplete;
    def onerror;
    def showinfo;
    def writer;
    def exportModel;
    

    public void run(){
        writer = new ObjectWriter(file);
        try{
            exportTransmittal();
            entity.items.each{
                showinfo('Exporting Item ' + it.refno );
                def ei = exportModel.exportItem(it);
                if (!ei) throw new Exception('Item data must be exported.')
                writer.writeObject(ei);
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
        def m = [:]
        m.putAll(entity);
        
        def items = m.items;
        m.remove('items');
        
        def data = [
            filetype    : 'transmittal',
            transmittal : m,
        ]
        writer.writeObject(data);
        showinfo('done.\n');
    }
    
}