package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import java.util.*;

class TxnLogModel
{
    @Service('LogService')
    def logSvc;
    
    def entity;
    
    String title = 'Transaction Logs';
    
    def listHandler = [
        fetchList : { 
            return logSvc.getLogs(entity.objid);
        }        
    ] as BasicListModel;
    
}    