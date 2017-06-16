package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;

public class MasterSyncModel  extends com.rameses.gov.etracs.rptis.util.AbstractSyncModel
{
    @Service('PersistenceService')
    def persistence;

    public void sync(data){
        data.each{ 
            it._schemaname = entity.schemaname;
            persistence.save(it);
            loghandler.writeln('Updating data : ' + (it.name ? it.name : it.code) + '.');
        }
    }
}