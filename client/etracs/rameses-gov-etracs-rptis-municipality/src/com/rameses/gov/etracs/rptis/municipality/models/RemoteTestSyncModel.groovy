package com.rameses.gov.etracs.rptis.municipality.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rptis.util.ServiceLookup;

public class RemoteTestSyncModel extends DataSyncModel
{
    boolean showry = false;
    
    def getTitle(){
        return 'Province Connectivity Test'
    }
    
}