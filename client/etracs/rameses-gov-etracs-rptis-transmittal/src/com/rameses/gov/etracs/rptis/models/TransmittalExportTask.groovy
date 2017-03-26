package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rptis.util.*;


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
                showinfo('Exporting Item ' + it.refno);
                def ei = exportModel.exportItem(it);
                if (!ei) throw new Exception('Item data must be exported.')
                writer.writeObject(ei);
                doSleep(500);
            }
            oncomplete('Transmittal has been successfully exported.');
        }
        catch(e){
            e.printStackTrace();
            writer.cancel();
            onerror(e.message )
        }
        finally{
            writer.close();
        }
    }
    
    void doSleep(ms){
        try{
            sleep(ms)
        }
        catch(e){
            //ignore 
        }
            
    }
    
    void exportTransmittal(){
        showinfo('Exporting Transmittal ' + entity.txnno);
        def data = [
            filetype    : 'transmittal',
            transmittal : entity,
        ]
        writer.writeObject(data);
    }
    
}