package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;

public class BusinessRequirementTypeModel extends CrudFormModel {
    
    
    def types = ["DOC", "WORKFLOW"];
    def workflowStates;
    
    void afterInit() {
        def m = [_schemaname:'sys_wf_node'];
        m.select = "name,title";
        m.findBy = [processname: 'business_application'];
        m.where = [ " name NOT IN ('start','end') "];
        m.orderBy = "idx";
        workflowStates = queryService.getList(m);
    }
    

}