package com.rameses.gov.treasury.remittance;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import javax.swing.*
import com.rameses.io.*
        
class RemittanceImportController
{
    @Binding
    def binding;
    
    @Service('RemittanceImportExportService')
    def svc;
    
    def MODE_INIT = 'init';
    def MODE_READ = 'read';
    
    def data;
    def mode;
    
    def asyncHandler;
    def has_error = false; 
    
    String getTitle(){
        return 'Import Remittance';
    }
    
    void init(){
        data = [
            state : 'UNPOSTED',
        ];
        mode   = MODE_INIT;
    }
    
    def getEntity(){
        return data?.entity
    }
    
    def upload(){
         asyncHandler = [
            onError: {o-> 
                MsgBox.err(o.message); 
                has_error = true
                doCancel()
            }, 
            onTimeout: {
                asyncHandler.retry(); 
            },
            onMessage: {o-> 
                if (o == com.rameses.common.AsyncHandler.EOF) {
                   if( has_error )
                        MsgBox.alert('Process with errors  .');
                   else 
                        MsgBox.alert('Remittance has been successfully uploaded.'); 
                   doCancel()
                } 
            } 
        ] as com.rameses.common.AbstractAsyncHandler 
        
        
        if (MsgBox.confirm('Upload Remittance?')){
            has_error = false 
            svc.importRemittance(data, asyncHandler);
            return "processing"
        }
        return null
    }
    
    void doImport(){
        def chooser = new JFileChooser();
        int i = chooser.showOpenDialog(null);
        if(i==0) {
            data = FileUtil.readObject( chooser.selectedFile );
            data.state = 'POSTED';
            receiptListHandler.reload();
            mode = MODE_READ;
        }      
    }
          
    
    def doCancel() {
        return "default";
    }
    
    /*=================================================================
     *
     * HANDLERS 
     *
     *================================================================= */
    
    def receiptListHandler = [
            fetchList : { return data.cashreceipts; }
    ] as BasicListModel;
    
}

