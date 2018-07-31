package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class RPUMachInfoAppraisalModel extends SubPageModel
{
    
    @Service('MachRPUService')
    def svc;
    
    def rpuSvc;
    
    void init(){
        setRpuAppraisalDate();
    }
    
    void calculateAssessment(){
        super.calculateAssessment();
        machineListHandler.load();
    }
    
    void setRpuAppraisalDate() {
        entity.rpu.dtappraised = entity.appraiser?.dtsigned;
    }
    
    
    /*------------------------------------------------------
     *
     * MACHINE APPRAISAL SUPPORT
     *
     ------------------------------------------------------*/
    def actualuse;
    def selectedMachine;
    
    List getMachuses(){
        return entity.rpu.machuses;
    }
    
    void modeChanged(String mode){
        actualuse = null;
        machineListHandler.reload();
        super.modeChanged(mode);
    }
    
    def machineListHandler = [
        getRows   : { return actualuse?.machines?.size() <= 25 ? 25 : actualuse?.machines?.size()+1 },
        fetchList : { return actualuse?.machines },
                
        onRemoveItem : { item ->
            if (! allowEdit ) return false;
            if (MsgBox.confirm('Remove selected item?')){
                actualuse.machines.remove(item);
                if (!entity.rpu._machines) entity.rpu._machines = [];
                entity.rpu._machines.add(item);
                calculateAssessment();
                return true;
            }
            return false;
        }
    ] as EditorListModel
     
            
    def onaddMachine = {
        actualuse.machines.add(it);
        calculateAssessment();
    }
                
    def addMachine(){
        if (!actualuse) return;
        setRpuAppraisalDate();
        return InvokerUtil.lookupOpener('machdetail:create', [ 
            svc     : svc,
            entity  : entity,
            rpu     : entity.rpu,
            machuse : actualuse,
            onadd   : onaddMachine,
        ])
    }
    
    def onupdateMachine = {md -> 
        def m = actualuse?.machines.find{it.objid == md.objid}
        calculateAssessment();
    }
    
    def editMachine(){
        return openMachine();
    }
    
    def openMachine(){
        setRpuAppraisalDate();
        return InvokerUtil.lookupOpener('machdetail:open', [ 
            svc         : svc,
            entity      : entity,
            rpu         : entity.rpu,
            machuse     : actualuse,
            machdetail  : selectedMachine, 
            onupdate    : onupdateMachine,
            allowEdit   : getAllowEdit(),
        ])
    }
    
    
    def getLookupFaas(){
        return Inv.lookupOpener('bldgmaster:lookup', [
            ry       : entity.rpu.ry, 
            pin      : entity.rp.pin, 
            rputype  : 'bldg',
            
            onselect : {
                entity.rpu.bldgmaster = it;
            },
            
            onempty  : {
                entity.rpu.bldgmaster = null;
            }

        ])
    }
    
    
}    
