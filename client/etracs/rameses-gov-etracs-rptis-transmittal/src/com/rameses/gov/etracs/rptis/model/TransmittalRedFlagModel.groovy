package com.rameses.gov.etracs.rptis.model;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rpt.util.RPTUtil;

class TransmittalFAASModel extends TransmittalModel
{
    public String getFileType(){
        return 'rptredflag';
    }
    
    def init(){
        if (!OsirisContext.env.ORGCLASS.equalsIgnoreCase('PROVINCE')){
            throw new Exception('Red Flag transmittal for ' + OsirisContext.env.ORGCLASS.toLowerCase() + ' is not yet supported.');
        }
        return super.init();
    }
    
    def getLookupHandler(){
        return Inv.lookupOpener('rptredflag:lookup', [
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
    
    public def buildItemData(redflag){
        if (redflag.state != 'OPEN')
            throw new Exception('Red Flag is invalid. Only open state is allowed.');
            
        if ('municipality'.equalsIgnoreCase(entity.tolgu.lgutype) && redflag.lguid != entity.tolgu.objid )
            throw new Exception('Red Flag is invalid. Only record from ' + entity.tolgu.name + ' is allowed.');
        

        def item = [:]
        item.refid = redflag.objid;
        item.refno = redflag.caseno;
        item.state = redflag.state;
        return item;
    }
    
    List getTransmittalTypes(){
        return ['FORAPPROVAL']
    }
    
}
