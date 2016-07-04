package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rpt.util.*;


public class TransmittalImportTask implements Runnable{
    def file;
    def reader;
    def oncomplete;
    def onerror;
    def showinfo;
    def importSvc; 
    def importModel;
    
    public void run(){
        try{
            reader = new ObjectReader(file);
            def data = reader.readObject();
            validateImport(data);
            showinfo('Importing Transmittal ' + data.transmittal.txnno + '\n')
            while(data){
                if (data.filetype == 'transmittal'){
                    importSvc.importTransmittal(data.transmittal);
                }
                else{
                    showinfo('Importing Item ' + data.transmittalitem.refno + '\n')
                    importModel.importData(data);
                }
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
        def filetype = importModel.getFileType()
        println 'filetype -> ' + filetype;
        if (data.transmittal.filetype != filetype)
            throw new Exception('Transmittal is invalid. Only file of Type "' + filetype + '" is allowed.');
            
        importSvc.validateImport(data);
    }
       
}