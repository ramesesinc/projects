package com.rameses.gov.etracs.rpt.transmittal.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rpt.util.*;


public class TransmittalImportTask implements Runnable{
    def file;
    def importSvc;
    def oncomplete;
    def onerror;
    def showinfo;
    def reader;
    def importer;
    
    public void run(){
        try{
            reader = new ObjectReader(file);
            importer = new TransmittalImporter(importSvc, showinfo);
            
            def data = reader.readObject();
            validateImport(data);
            while(data){
                importer.importData(data);
                data = reader.readObject();
            }
            oncomplete('Transmittal is successfully imported.');
        }
        catch(e){
            onerror('\n\n' + e.message )
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
    
    void validateImport(data){
        importSvc.validateImport([filetype:data.filetype, transmittal:data.transmittal]);
    }
       
}