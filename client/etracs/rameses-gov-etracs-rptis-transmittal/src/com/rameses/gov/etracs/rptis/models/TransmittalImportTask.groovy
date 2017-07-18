package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rptis.util.*;


public class TransmittalImportTask implements Runnable{
    def file;
    def reader;
    def updateItem;
    def oncomplete;
    def onerror;
    def importSvc; 
    def importModel;
    def transmittal;
    
    public void run(){
        try{
            reader = new ObjectReader(file);
            def data = reader.readObject();
            transmittal = data.transmittal;
            while(data){
                if (data.filetype == 'transmittal'){
                    importSvc.importTransmittal(data.transmittal);
                }
                else{
                    def item = importModel.importData(transmittal, data);
                    updateItem(item);
                }
                data = reader.readObject();
            }
            oncomplete('Transmittal is successfully imported.');
        }
        catch(e){
            onerror(e.message);
        }
        finally{
            try { 
                reader.close();
            } 
            catch(e){
                //ignore
            }
        }
    }
       
}