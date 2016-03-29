import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import java.rmi.server.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;

class WaterworksAccountBill{

    @Service("ReportParameterService")
    def paramsvc;

    @Service("DateService")
    def dateSvc;

    @Service("WaterworksBillingService")
    def billSvc;

    @Caller
    def caller;

    def entity;
    def title = 'Water Bill'; 

    def reportdata;
    def reportPath = "com/rameses/gov/etracs/waterworks/reports/";

    void viewReport() {
        reportdata = billSvc.generateReportData(entity);
        report.viewReport();
    }

    def report = [
        getReportName : { return reportPath + 'WaterBill.jasper' },
        getReportData : { return reportdata }, 
        getSubReports : {
            return [ 
                new SubReport("BillingDetails", reportPath + "BillingDetails.jasper"),
                new SubReport("BarChart", reportPath + "BarChart.jasper")
            ] as SubReport[];    
         },
        getParameters : { 
            def params = paramsvc.getStandardParameter() 
            params.LOGOLGU = EtracsReportUtil.getInputStream("lgu-logo.png")
            return params;
        },
    ] as ReportModel;
}