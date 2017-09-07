package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
import javax.swing.JFileChooser;
        
public class LiquidationFundModel extends CashBreakdownModel  {
    
    
    @Service("LiquidationImportExportService")
    def exportSvc;

    public def getChecks() {
        return entity.payments.findAll{ it.reftype == 'CHECK' };
    }
    
    public def getCreditMemos() {
        return entity.payments.findAll{ it.reftype == 'CREDITMEMO' };
    }
    
    public void afterOpen() {
        super.afterOpen();
        editable = entity.remittance?.state == 'DRAFT';
    }
    
    public def openFromCollection() {
        MsgBox.alert( caller.entity );
        MsgBox.alert("refid " + entity?.refid );
        entity.objid = entity.refid;
        return super.open();
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