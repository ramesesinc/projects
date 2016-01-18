package com.rameses.gov.etracs.rpt.consolidation.ui;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rpt.consolidation.task.*;
import com.rameses.util.MapBeanUtils;

public class ConsolidationController extends AbstractConsolidationController
{
    def getApproverTask(task){
        if (task.state.matches('approver|provapprover'))
            return new ApproveConsolidationTask();
    }    
}
