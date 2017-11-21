package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

class RealtyTaxAccountMappingModel extends CrudListModel
{
    @Service('LandTaxLGUAccountMappingService')
    def svc;
    
    void buildAccounts(){
        if (MsgBox.confirm('Build account mappings?')){
            svc.buildAccounts([lgutype:selectedNode?.orgclass]);
            reloadNodes();
        }
    }
}