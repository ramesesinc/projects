package com.rameses.gov.etracs.rpt.subdivision.ui;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rpt.util.*;
import com.rameses.util.MapBeanUtils;
import com.rameses.gov.etracs.rpt.subdivision.task.*;

public class SubdivisionController extends com.rameses.gov.etracs.rpt.subdivision.ui.AbstractSubdivisionController 
{
    def getApproverTask(task){
        if (task.state.matches('approver|cityapprover'))
            return new ApproveSubdivisionTask();
    }
    
}


