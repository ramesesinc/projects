package com.rameses.gov.etracs.rpt.report.td;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;

public class TDTrueCopyController extends com.rameses.gov.etracs.rpt.report.certification.AbstractCertificationController
{
    @Service('RPTCertificationService')
    def svc;
    
    @Service('TDReportService')
    def tdSvc;
    
    boolean certbytd = true
            
    def getService(){
        return svc;
    }
    
    def getLookupFaas(){
        return InvokerUtil.lookupOpener('faas:lookup',[
            onselect : { 
                if (it.state != 'CURRENT' && it.state != 'CANCELLED'){
                    throw new Exception('FAAS is not current or cancelled.')
                }
                entity.faasid = it.objid;
                entity.tdno= it.tdno;
                entity.taxpayer = it.taxpayer;
                entity.requestedby = it.taxpayer.name;
                entity.requestedbyaddress = it.taxpayer.address;
            },
            onempty  : { 
                entity.faasid = null;
                entity.tdno= null;
                entity.taxpayer = null;
                entity.requestedby = null;
                entity.requestedbyaddress = null;
            },
        ])
    }
          
    
    def getReportData(){
        def e = tdSvc.buildTaxDec(entity.faasid);
        e.putAll(entity)
        return e;
    }
    
    def reportPath = 'com/rameses/gov/etracs/rpt/report/td/';
    String reportName = reportPath + 'tdreport.jasper';

    SubReport[] getSubReports() {
        return [
            new SubReport( 'TDReportLand',  reportPath + 'tdreportland.jasper'),
            new SubReport( 'TDReportLandESig',  reportPath + 'tdreportlandesigned.jasper'),
            new SubReport( 'TDReportBldg',  reportPath + 'tdreportbldg.jasper'),
            new SubReport( 'TDReportMach',  reportPath + 'tdreportimprovement.jasper'),
            new SubReport( 'TDReportPlant', reportPath + 'tdreportimprovement.jasper'),
            new SubReport( 'TDReportMisc',  reportPath + 'tdreportimprovement.jasper'),
            new SubReport( 'TDReportAnnotation', reportPath + 'tdreportannotation.jasper'),
        ] as SubReport[]
    }
    
    Map getParameters(){
        def info = [:];
        entity.each{ k, v -> info[ k.toUpperCase() ] = v };
        info.CERTIFICATIONORAMOUNT = entity.oramount;
        info.CERTIFICATIONORDATE   = entity.ordate;
        info.CERTIFICATIONORNO     = entity.orno;
        info.PLAINTRUECOPY         = false;
        info.LOGOLGU = EtracsReportUtil.getInputStream("lgu-logo.png")
        info.BACKGROUND = EtracsReportUtil.getInputStream("background.png")
        info.LOGOASSESSOR = EtracsReportUtil.getInputStream("assessor-logo.png")
        info.LOGOBLGF = EtracsReportUtil.getInputStream("lgu-blgf.png")
        return info;
    }
    
    
    def getCertificationTypes(){
        return [
            [type:'byfaas', caption:'By FAAS'],
        ]
    }     
}