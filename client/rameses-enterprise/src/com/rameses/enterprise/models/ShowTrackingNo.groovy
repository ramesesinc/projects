package com.rameses.enterprise.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import java.rmi.server.*;
import com.rameses.util.*;
import com.rameses.osiris2.reports.*;

public class ShowTrackingNo extends ReportModel {
        
    def info;

    String reportName = "com/rameses/enterprise/reports/show_trackingno.jasper";

    def getReportData() {
        return info;
    }

    String viewReport() {
        super.viewReport();
        return "default";
    }
    
}