package com.rameses.enterprise.accounting.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.* 
import com.rameses.enterprise.treasury.models.*;
import javax.swing.*;
import com.rameses.io.*;

class DailyCollectionModel extends LiquidationModel { 

    public String getFormName() {
        return "daily_collection";
    }
    
    void doExport() {
        def chooser = new JFileChooser();
        chooser.setSelectedFile(new File(entity.txnno + '.liq'));
        int i = chooser.showSaveDialog(null);
        if(i==0) {
            FileUtil.writeObject( chooser.selectedFile, exportSvc.exportLiquidation(entity.objid) );
            MsgBox.alert("Liquidation has been successfully exported!");
        }   
    }

} 