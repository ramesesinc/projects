package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;

class TransmittalSubdivisionModel extends TransmittalModel
{
    public String getFileType(){
        return 'subdivision';
    }
    
    def getLookupHandler(){
        return Inv.lookupOpener('subdivision:lookup', [
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
    
    public def buildItemData(subdivision){
        def item = [:]
        item.refid = subdivision.objid;
        item.refno = subdivision.txnno;
        item.state = subdivision.state;
        return item;
    }
    
        
    List getTransmittalTypes(){
        return ['SYNC', 'FORAPPROVAL']
    }
    
    void doValidateItems(items){
        items.each{subdivision ->
            if ('municipality'.equalsIgnoreCase(entity.tolgu.lgutype) && subdivision.lguid != entity.tolgu.objid )
            throw new Exception('Subdivision ' + subdivision.txnno + 'is invalid. Only subdivision from ' + entity.tolgu.name + ' is accepted.');
            
            if (entity.type == 'FORAPPROVAL' && ! subdivision.state.matches('FORAPPROVAL'))
                throw new Exception('Subdivision ' + subdivision.txnno + ' state is invalid. Only For Approval state is allowed.')
                
            if (entity.type == 'SYNC' && ! subdivision.state.matches('APPROVED'))
                throw new Exception('Subdivision ' + subdivision.txnno + ' state is invalid. Only Approved state is allowed.')

            def exist = entity.items.find{it.refid == subdivision.objid}
            if (exist) throw new Exception('Subdivision ' + subdivision.txnno + ' has already been added.');
        }
    }
    
}
