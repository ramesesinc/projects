package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;
import com.rameses.seti2.models.*;

public class NewMarketApplication extends WorkflowTaskInitialModel {
    
    public void afterSave() {
        MsgBox.alert("App No. " + entity.appno + " created");
        def opener = Inv.lookupOpener( "market_application:open", [refid: entity.objid] );
        binding.fireNavigation( opener  );
    }

}