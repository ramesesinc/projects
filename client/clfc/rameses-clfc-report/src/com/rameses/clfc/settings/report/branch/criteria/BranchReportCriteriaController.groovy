package com.rameses.clfc.settings.report.branch.criteria

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class BranchReportCriteriaController extends CRUDController
{
    String serviceName = "BranchReportCriteriaService";
    String prefix = 'BCR';
    
    boolean allowDelete = false;
    boolean allowApprove = false;
    
    Map createPermission = [domain: 'ADMIN', role: 'ADMIN_SUPPORT'];
    Map editPermission = [domain: 'ADMIN', role: 'ADMIN_SUPPORT'];
    
    
}

