package com.rameses.gov.etracs.rptis.municipality.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;

public class MasterSyncModel  extends com.rameses.gov.etracs.rptis.models.AbstractSyncModel
{
    public def sync(items){
        return syncSvc.syncMaster(entity);
    }
}