package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rpt.util.RPTUtil;

class TransmittalConsolidationModel extends TransmittalModel
{
    public String getFileType(){
        return 'consolidation';
    }
    
    def getLookupHandler(){
        return Inv.lookupOpener('consolidation:lookup', [
            onselect : {
                def item = buildItemData(it);
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
    
    public def buildItemData(consolidation){
        if ('municipality'.equalsIgnoreCase(entity.tolgu.lgutype) && consolidation.lguid != entity.tolgu.objid )
            throw new Exception('Consolidation is invalid. Only Consolidation from ' + entity.tolgu.name + ' is accepted.');
            
        if (! consolidation.state.matches('FORAPPROVAL'))
            throw new Exception('Consolidation state is invalid. Only FORAPPROVAL state is allowed.')
        def item = [:]
        item.refid = consolidation.objid;
        item.refno = consolidation.txnno;
        item.state = consolidation.state;
        return item;
    }
    
        
    List getTransmittalTypes(){
        return ['FORAPPROVAL']
    }
    
}
