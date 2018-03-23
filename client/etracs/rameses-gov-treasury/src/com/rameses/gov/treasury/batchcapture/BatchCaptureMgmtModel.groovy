package com.rameses.gov.treasury.batchcapture; 

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
        
public class BatchCaptureMgmtModel {

    @Binding 
    def binding;    
    
    @Service('BatchCaptureManagementService')
    def svc; 
        
    @PropertyChangeListener
    def listener = [
        'info.*' : { 
            loadAfControls();
        },
            
        'info.collector|info.afserial' : {
            info.currentseries = null;
            binding.refresh('info.currentseries');
        } ,
        
        'collector|startseries' : {
            loadCapturedItems()
        }
    ]
                
    String title = 'Batch Capture Management';
                
    
    def info;
    def selectedAfControl;
    def selectedAssignedItem;
    
    
    def afcontrols;
    def assignedcontrols;
    def batchcaptures;
            
    
    
    void init(){
        info = [:];
        afcontrols = [];
        assignedcontrols = [];
        batchcaptures = [];
    }
    
    void loadAfControls(){
        afcontrols = [];
        assignedcontrols = [];
        if (info.afserial && info.collector){
            info.formno = info.afserial.formno;
            info.userid = info.collector.objid  
            afcontrols = svc.getOpenIssuanceList(info);
            assignedcontrols = svc.getAssigneeIssuanceList(info);
        }
        afcontrolListHandler.reload();
        assignedListHandler.reload();
    }
    
    
    def afcontrolListHandler = [
        fetchList : { 
            if (info.currentseries != null )
                return afcontrols.findAll{ info.currentseries >= it.startseries && info.currentseries <= it.endseries}
            return afcontrols 
        },
    ] as BasicListModel;
            
        
    def assignedListHandler = [
        fetchList : { return assignedcontrols },
    ] as BasicListModel;
    
        
    def assignAfSerial(){
        if(selectedAfControl.active==1) {
            throw new Exception("Entry is currently active. Deactivate first");
        }    
        return InvokerUtil.lookupOpener("subcollector:lookup", [
            onselect:{x->
                if(MsgBox.confirm("You are about to assign this stub to " + x.name + ". Continue?")) {
                    svc.assignToSubcollector( [objid:selectedAfControl.objid, assignee:x] );
                    loadAfControls();
                }
            }
         ]);   
    }
            
    
    def changeMode(){
        return doChangeMode(selectedAfControl)
    }
    
    def changeModeAssigned(){
        return doChangeMode(selectedAssignedItem)
    }
    
    def doChangeMode(item){
        if (MsgBox.confirm('Change mode?')){
            return InvokerUtil.lookupOpener('af:changemode', [
                entity  : item,
                
                onSaveHandler : {
                    loadAfControls();
                }
            ])
        }
        return null
    }
    
    void unassignControl(){
        if (MsgBox.confirm('Unassign selected control?')){
            svc.unassignControl(selectedAssignedItem);
            loadAfControls();
        }
    }
    
    
    
    def getAfSerialList(){
        return svc.getAfSerialList();
    }
    
    
    
    
    /*==================================================================
     *
     * BATCH CAPTURED RECEIPT POSTING SUPPORT 
     *
     *==================================================================*/
    
    @Service('BatchCaptureCollectionService')
    def batchSvc
            
    
    def collector;
    def selectedCaptured;
    def captureditems = []
    def startseries
    
    
    def capturedListHandler = [
            fetchList : { return captureditems },
    ] as BasicListModel;
    

    void loadCapturedItems(){
        captureditems.clear();
        if ( collector ) {
             def params = [ collectorid: collector.objid,  startseries : startseries  ]
             captureditems = svc.getSubmittedBatchCapturedReceipts(params);
        }
        capturedListHandler.reload();
    }
    
    
    def openBatchCaptured(){
        return InvokerUtil.lookupOpener('cashreceipt:batchcapture:open', [
                entity : selectedCaptured,
                
                onPost : {
                    loadCapturedItems();
                }
        ])
    }
    
    
    
}