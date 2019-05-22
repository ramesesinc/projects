package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;

class AuctionReportCertificateOfSaleModel extends ReportController
{
    @Service('PropertyAuctionReportCertificateOfSaleService')
    def svc;
    
    public String getReportName(){
        return 'com/rameses/gov/etracs/landtax/reports/certificate_of_sale.jasper';
    }
    
    def init() {
        return preview();
    }
    
    public def getReportData(){
        return svc.getReport(entity);
    }
    
}