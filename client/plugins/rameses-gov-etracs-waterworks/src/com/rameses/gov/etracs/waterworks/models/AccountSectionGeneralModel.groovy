package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class AccountSectionGeneralModel {
    
    def changeOwner(){
        return Inv.lookupOpener("waterworks_account:changeowner")
    }
    
    def changeMeter(){
        return Inv.lookupOpener("waterworks_account:changemeter")
    }
    
}