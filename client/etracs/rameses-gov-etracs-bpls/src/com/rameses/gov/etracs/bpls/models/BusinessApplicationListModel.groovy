package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class BusinessApplicationListModel extends WorkflowTaskListModel {

    void afterInit() {
        query.appyear = new java.sql.Date(System.currentTimeMillis()).toString().split("-")[0];
    }

    @PropertyChangeListener
    def listener = [
        "query.appyear" : { o->
            reload();
        }
    ]
    
    def getCustomFilter() {
        if(query.appyear) {
            return ["appyear = :appyear", [appyear: query.appyear ] ];
        }
        else {
            return null;
        }
    }
    
}