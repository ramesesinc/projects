package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class TransmittalConsolidationModel extends TransmittalModel
{
    public String getFileType(){
        return 'consolidation';
    }
    
    def getLookupHandler(){
        return Inv.lookupOpener('consolidation:lookup', [
            onselect : {items ->
                doValidateItems(items);
                items.each{
                    def item = buildItemData(it);
                    validateItem(item);
                    svc.saveItem(item);
                    entity.items << item;
                }
                listHandler.reload();
            },
            
            onempty : {
                selectedItem.refno = null;
                selectedItem.refid = null;
                selectedItem.state = null;
                selectedItem.message = null;
            },
            
            multiSelect : true,
        ])
    }
    
    public def buildItemData(consolidation){
        def item = [:]
        item.refid = consolidation.objid;
        item.refno = consolidation.txnno;
        item.state = consolidation.state;
        return item;
    }
    
        
    List getTransmittalTypes(){
        return ['SYNC', 'FORAPPROVAL']
    }
    
    void doValidateItems(items){
        items.each{consolidation ->
            if ('municipality'.equalsIgnoreCase(entity.tolgu.lgutype) && consolidation.lguid != entity.tolgu.objid )
            throw new Exception('Consolidation ' + consolidation.txnno + ' is invalid. Only Consolidation from ' + entity.tolgu.name + ' is accepted.');
            
            if (entity.type == 'FORAPPROVAL' && ! consolidation.state.matches('FORAPPROVAL'))
                throw new Exception('Consolidation ' + consolidation.txnno + ' state is invalid. Only For Approval state is allowed.')
                
            if (entity.type == 'SYNC' && ! consolidation.state.matches('APPROVED'))
                throw new Exception('Consolidation ' + consolidation.txnno + ' state is invalid. Only Approved state is allowed.')

            def exist = entity.items.find{it.refid == consolidation.objid}
            if (exist) throw new Exception('Consolidation ' + consolidation.txnno + ' has already been added.');
        }
    }
}
