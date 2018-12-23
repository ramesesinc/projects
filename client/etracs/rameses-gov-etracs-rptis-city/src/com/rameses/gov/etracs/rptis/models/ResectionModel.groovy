package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rptis.models.*;
import com.rameses.gov.etracs.rptis.tasks.*;


public class ResectionModel extends AbstractResectionModel
{
    def getApproverTask(task){
        if (task.state.matches('approver|cityapprover')){
            return new ApproveResectionTask();
        }
    }
    
}


