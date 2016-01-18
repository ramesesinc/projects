package com.rameses.entity.ui.reconcile;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import java.rmi.server.UID;

public class ReconcileIndividualEntityController extends PageFlowController {
            
    @Binding
    def binding;
        
    @Caller
    def caller;
        
    @Service("ReconcileEntityService")
    def reconcileSvc;
            
    @Service("IndividualEntityNameMatchService")
    def matchService;
        
    def matchList;
    def selectedList;
    def forProcessList;
            
    def selectedItem;
    def selectedMasterItem;
    def selectProcessItem;
    boolean searching;
    boolean processing;        
    
    def entity = [:];
            
    def listModel = [
        fetchList: { o->
            return matchList;
        }
    ] as EditorListModel;
            
    void searchMatch() {
        matchList =  matchService.getMatches(entity);
        if(!matchList){
            throw new Exception("No matches found");
        }  
        listModel.reload();
        searching = true;
    }
            
    void reset() {
        matchList = [];
        searching = false;
        selectedList = [];
        forProcessList = [];
        listModel.reload();
        selectedListModel.reload();
        forProcessListModel.reload();
    }
            
    void buildMatch() {
        selectedList = matchList.findAll{it.selected == true}
        if( !selectedList || selectedList.size() < 2 ) 
        throw new Exception("Please select at least two records");
    }
            
    def selectedListModel = [
        fetchList: { o->
            return selectedList;
        }
    ] as BasicListModel;
            
    void buildForProcess() {
        if( !selectedMasterItem )
            throw new Exception("Please select a master item first");
        forProcessList = selectedList.findAll{ 
            it.objid != selectedMasterItem.objid && (!it.processed) 
        };
    }
      
    def forProcessListModel = [
        fetchList: { o->
            return forProcessList;
        }
    ] as BasicListModel;
    
    
    void startProcess() {
        def z = {
            try {
                def m = [entity:selectedMasterItem];
                for( o in forProcessList ) {
                    m.old = o;
                    reconcileSvc.reconcile( m );     
                    o.processed = true;
                    forProcessListModel.reload();
                }  
                selectedMasterItem.processed = true;
                //clear the data.
                reset();

                def t = signal();
                binding.fireNavigation( t );
            }
            catch(e) {
                MsgBox.err(e);
            }
        } as Runnable;    
        Thread t = new Thread( z );
        t.start();
    }
         
    void doFinish() {
        MsgBox.alert("finished");
    }
    
} 