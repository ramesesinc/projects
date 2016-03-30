import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

public class SendoutBranchReportModel extends ReportController  
{
    @Service('SendoutReportService')
    def reportSvc; 

    String title = "Sendout Report By Branch";
    String reportpath = "com/rameses/mt/reports/";
    String reportName = reportpath + 'SendoutBranchReport.jasper'

    def rptparams = [:];
    
    def formControl = [ 
        getControlList: { 
            return [
                [type:'date', caption:'From Date', name:'entity.startdate', required:true, captionWidth:100, preferredSize:'0,20'],
                [type:'date', caption:'To Date', name:'entity.enddate', required:true, captionWidth:100, preferredSize:'0,20'],
            ]; 
        } 
    ] as FormPanelModel; 

    def permitTypes = []; 

    def initReport() { 
        def outcome = super.initReport(); 
        entity.startdate = new java.sql.Date( System.currentTimeMillis() ); 
        entity.enddate = new java.sql.Date( System.currentTimeMillis() ); 
        return outcome; 
    } 

    Map getParameters() { 
        return rptparams; 
    }

    def getReportData() {  
        return reportSvc.getBranchReport( entity ); 
    }
}