import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.osiris2.reports.*;

class GenerateBusinessBillingReportModel extends com.rameses.osiris2.reports.ReportController {
    
    @Service('BPBillReportService') 
    def billReportSvc; 

    def title = "Generate Billing";
    def reportPath = "com/rameses/gov/etracs/bpls/reports/billing/";
    String reportName = reportPath + "BPBilling.jasper";

    def data; 
    def businessname; 

    def initReport() { 
        def o = billReportSvc.init();  
        if ( o ) entity.putAll( o ); 
        
        return super.initReport(); 
    } 

    def entity = [:]; 
    def listhandler = [ 
        getRows: { 
            return 25; 
        }, 
        fetchList: { o-> 
            billReportSvc.getBusinessList( o ); 
        }, 
        onselect: { o-> 
            entity.business = o; 
            businessname = o.businessname; 
            binding.refresh('entity.businessname'); 
            binding.notifyDepends('entity.businessname'); 
        }, 
        onempty: { 
            entity.business = null; 
            businessname = null; 
        }
    ] as SuggestModel; 

    def getReportData() { 
        return data; 
    } 
    SubReport[] getSubReports(){ 
        return [
            new SubReport("BPBillingItem", reportPath + "BPBillingItem.jasper"),                
        ] as SubReport[]; 
    } 
    
    def doClose() {
        return '_close'; 
    }
    def doPreview() { 
        entity.applicationid = entity.business?.currentapplicationid; 
        if ( !entity.applicationid ) {
            MsgBox.alert('Please specify a Business Name');
            return null; 
        }

        data = billReportSvc.getBilling( entity ); 
        return preview(); 
    } 
} 