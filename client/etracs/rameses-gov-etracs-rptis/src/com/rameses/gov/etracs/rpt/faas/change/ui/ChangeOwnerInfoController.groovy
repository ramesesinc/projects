package com.rameses.gov.etracs.rpt.faas.change.ui;
        

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.faas.change.ui.*;


public class ChangeOwnerInfoController extends ChangeFaasInfoController
{
    String title = 'Modify Owner Information';
    
    
    public def getModifiedEntity(){
        return [
            taxpayer      : entity.taxpayer,
            owner         : entity.owner,
            administrator : entity.administrator,
        ]
    }
    
    public void updateEntityInfo(newinfo){
        entity.taxpayer      = newinfo.taxpayer
        entity.owner         = newinfo.owner
        entity.administrator = newinfo.administrator
    }
    
    
    def getLookupTaxpayer(){
        return InvokerUtil.lookupOpener('entity:lookup',[
            onselect : { 
                updateOwnershipInfo(it);
            },
            onempty  : { 
                changeinfo.newinfo.taxpayer = null;
                changeinfo.newinfo.owner    = null;
            } 
        ])
    }     
    
    
    void updateOwnershipInfo(taxpayer){
        def address = taxpayer.address.text 
        changeinfo.newinfo.taxpayer = taxpayer;
        changeinfo.newinfo.taxpayer.address = address
        changeinfo.newinfo.owner = [name:taxpayer.name, address:address];
        binding.refresh('changeinfo.*');
    }    
     
}
       