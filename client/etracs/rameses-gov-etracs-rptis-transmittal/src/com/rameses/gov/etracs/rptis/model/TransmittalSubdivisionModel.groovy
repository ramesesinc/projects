package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rpt.util.RPTUtil;

class TransmittalSubdivisionModel extends TransmittalModel
{
    public String getFileType(){
        return 'subdivision';
    }
    
    def getLookupHandler(){
        return Inv.lookupOpener('subdivision:lookup', [
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
    
    public def buildItemData(subdivision){
        if ('municipality'.equalsIgnoreCase(entity.tolgu.lgutype) && subdivision.lguid != entity.tolgu.objid )
            throw new Exception('Subdivision is invalid. Only subdivision from ' + entity.tolgu.name + ' is accepted.');
            
        if (! subdivision.state.matches('FORAPPROVAL'))
            throw new Exception('Subdivision state is invalid. Only FORAPPROVAL state is allowed.')
        def item = [:]
        item.refid = subdivision.objid;
        item.refno = subdivision.txnno;
        item.state = subdivision.state;
        return item;
    }
    
        
    List getTransmittalTypes(){
        return ['FORAPPROVAL']
    }
    
}
