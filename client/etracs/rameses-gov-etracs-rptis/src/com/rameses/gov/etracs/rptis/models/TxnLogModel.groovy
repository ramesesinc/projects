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
            def logs = logSvc.getLogs(entity.objid).sort{a,b -> 
                if (a.txndate < b.txndate) return 1;
                if (a.txndate == b.txndate) return 0;
                return -1;
            };
            return logs;
        }        
    ] as BasicListModel;
    
}    