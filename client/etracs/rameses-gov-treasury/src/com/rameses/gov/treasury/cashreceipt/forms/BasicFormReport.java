/*
 * MyModel.java
 *
 * Created on August 13, 2013, 9:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.gov.treasury.cashreceipt.forms;

import com.rameses.osiris2.reports.ReportModel;
import com.rameses.osiris2.reports.SubReport;

/**
 *
 * @author Elmo
 */
public class BasicFormReport extends ReportModel {
    
    private String dirPath = "com/rameses/gov/treasury/cashreceipt/forms/";
    
    /** Creates a new instance of MyModel */
    public BasicFormReport() {
    }
    
    public SubReport[] getSubReports() {
        return new SubReport[]{
            new SubReport("detail","")
        };
    }

    public Object getReportData() {
        return null;
    }

    public String getReportName() {
        return null;
        
    }
}
