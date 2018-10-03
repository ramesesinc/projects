package com.rameses.gov.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
        
public class BatchCaptureController {

    @Service('BatchCaptureMgmtService')
    def svc; 
    
    String title = 'Batch Capture Management';
                
    def info;
    def selectedAfControl;
    def selectedAssignedItem;
    
    def afcontrols;
    def assignedcontrols;
    def batchcaptures;

    @Binding 
    def binding;    
    
    @PropertyChangeListener
    def listener = [
        'info.*' : { 
            loadAfControls();
        },

        'info.collector|info.afserial' : {
            info.currentseries = null;
            binding.refresh('info.currentseries');
        },

        'collector|startseries' : {
            loadCapturedItems();
        }
    ];
    
    void init(){
        info = [:];
        afcontrols = [];
        assignedcontrols = [];
        batchcaptures = [];
    }
    
    def getAfSerials() { 
        return svc.getAfSerials(); 
    } 
    
    void loadAfControls(){
        afcontrols = [];
        assignedcontrols = [];
        if (info.afserial && info.collector){
            info.formno = info.afserial.formno;
            info.userid = info.collector.objid  
            afcontrols = svc.getOpenAfSerials(info);
            assignedcontrols = svc.getAssignedAfSerials(info);
        }
        afcontrolListHandler.reload();
        assignedListHandler.reload();
    }
    
    
    def afcontrolListHandler = [
        fetchList : { 
            if (info.currentseries != null ) {
                return afcontrols.findAll{ info.currentseries >= it.startseries && info.currentseries <= it.endseries}
            }
            return afcontrols;
        } 
    ] as BasicListModel;
            
        
    def assignedListHandler = [
        fetchList : { return assignedcontrols }
    ] as BasicListModel;
    
        
    def assignAfSerial(){
        if (selectedAfControl.active == 1) {
            throw new Exception("Entry is currently active. Deactivate first");
        }    
        
        def param = [:]; 
        param.onselect = { x-> 
            if(MsgBox.confirm("You are about to assign this stub to " + x.name + ". Continue?")) {
                svc.assignToSubcollector([ objid: selectedAfControl.objid, assignee: x ]);
                loadAfControls();
            }
        }
        return Inv.lookupOpener("subcollector:lookup", param);   
    }
            
    void unassignControl() { 
        if (MsgBox.confirm('Unassign selected control?')) { 
            svc.unassignControl(selectedAssignedItem); 
            loadAfControls(); 
        } 
    } 
    
    def modeHandler1 = { mode-> 
        svc.changeMode([ objid: selectedAfControl.objid, txnmode: mode ]); 
        loadAfControls(); 
        return null;
    }
    def modeHandler2 = { mode-> 
        svc.changeMode([ objid: selectedAssignedItem.objid, txnmode: mode ]); 
        loadAfControls(); 
        return null;
    } 
    
    def changeMode(){
        def menu = new PopupMenuOpener(); 
        menu.add( new ChangeModeAction('ONLINE', modeHandler1) );
        menu.add( new ChangeModeAction('OFFLINE', modeHandler1) );
        menu.add( new ChangeModeAction('CAPTURE', modeHandler1) );
        return menu; 
    }
    
    def changeModeAssigned(){
        def menu = new PopupMenuOpener(); 
        menu.add( new ChangeModeAction('ONLINE', modeHandler2) );
        menu.add( new ChangeModeAction('OFFLINE', modeHandler2) );
        menu.add( new ChangeModeAction('CAPTURE', modeHandler2) );
        return menu; 
    }
    
   
    /*==================================================================
     *
     * BATCH CAPTURED RECEIPT POSTING SUPPORT 
     *
     *==================================================================*/
    
    @Service('BatchCaptureCollectionService')
    def batchSvc; 
            
    def collector;
    def startseries;
    def captureditems = [];
    def selectedCaptured;
    
    def capturedListHandler = [
        fetchList : { return captureditems; }, 
        openItem: { item,colname-> 
            return openBatchCaptured(); 
        }
    ] as BasicListModel;
    
    void loadCapturedItems(){
        captureditems.clear();
        if ( collector?.objid ) {
            def param = [ collectorid: collector.objid,  startseries: startseries ]; 
            captureditems = svc.getSubmittedBatches( param ); 
        } 
        capturedListHandler.reload();
    }
    
    def openBatchCaptured() { 
        if ( !selectedCaptured ) return null; 
        
        def param = [ entity: selectedCaptured ]; 
        param.onPost = { loadCapturedItems(); }
        return Inv.lookupOpener('batchcapture_collection:open', param);
    }
}