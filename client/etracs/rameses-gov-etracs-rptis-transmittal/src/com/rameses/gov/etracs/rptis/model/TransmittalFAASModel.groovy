package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rpt.util.RPTUtil;

class TransmittalFAASModel extends TransmittalModel
{
    public String getFileType(){
        return 'faas';
    }
    
    def getLookupHandler(){
        return Inv.lookupOpener('faas:lookup', [
            onselect : {
                def item = buildItemData(it);
                validateItem(item);
                selectedItem.putAll(item);
            },
            
            onempty : {
                selectedItem.refno = null;
                selectedItem.refid = null;
                selectedItem.state = null;
                selectedItem.message = null;
            }
        ])
    }
    
    public def buildItemData(faas){
        if (entity.type == 'SYNC'){
            if (faas.state != 'CURRENT') 
                throw new Exception('FAAS state is invalid. Ony Current state is allowed.');
        }
        else{
            if (RPTUtil.isTrue(faas.datacapture)){
                if (faas.state != 'FORAPPROVAL')
                    throw new Exception('FAAS state is invalid. Only For Approval state is allowed.')
            }
            else{
                if (! faas.state.matches('INTERIM'))
                    throw new Exception('FAAS state is invalid. Only Interim state is allowed.')
                // service.checkTaskForApprovalState()
            }
        }
        
        def refno = (faas.tdno ? faas.tdno : faas.fullpin)
        def item = [:]
        item.refid = faas.objid;
        item.refno = refno;
        item.state = faas.state;
        return item;
    }
    
}
