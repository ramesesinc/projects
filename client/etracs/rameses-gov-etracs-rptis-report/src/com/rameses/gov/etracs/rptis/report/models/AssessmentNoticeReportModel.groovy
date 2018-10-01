package com.rameses.gov.etracs.rptis.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;
import com.rameses.gov.etracs.rptis.util.*;

class AssessmentNoticeReportModel 
{
    @Service('ReportParameterService')
    def paramSvc;

    def entity;
    
    String title = 'Notice of Assessment Report';


    String reportpath = 'com/rameses/gov/etracs/rpt/report/notice/';
    
    def preview(){
        saveSignatures(entity);
        report.viewReport();
        return 'preview';
    }

    def report = [
        getReportName : { return reportpath + 'notice.jasper' },
        getSubReports : { 
            return [
                new SubReport( 'NoticeItem', reportpath + 'noticeitem.jasper' ),
            ] as SubReport[]
        },
        getReportData : { 
            entity.items2 = entity.items
            return entity 
        },
        getParameters : { 
            def params = paramSvc.getStandardParameter()
            params.LOGOLGU = EtracsReportUtil.getInputStream("lgu-logo.png")
            params.BACKGROUND = EtracsReportUtil.getInputStream("background.png")
            params.LOGOASSESSOR = EtracsReportUtil.getInputStream("lgu-assessor.png")
            params.LOGOBLGF = EtracsReportUtil.getInputStream("lgu-blgf.png")
            return params
        }
    ] as ReportModel
	
    void saveSignatures(reportdata){
        reportdata.signatories.each{ k, v ->
            def objid = v.objid + '-' + v.state 
            if (v.signature?.image){
                v.signatureis = DBImageUtil.getInstance().saveImageToFile(objid, v.signature.image)
                v.signature2is = DBImageUtil.getInstance().saveImageToFile(objid+'2', v.signature.image)
            }
                
        }
    }    
}
