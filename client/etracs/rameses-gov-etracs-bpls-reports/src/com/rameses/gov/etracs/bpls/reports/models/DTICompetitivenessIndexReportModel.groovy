package com.rameses.gov.etracs.bpls.reports.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

public class DTICompetitivenessIndexReportModel extends AsyncReportController {

    @Script('ReportPeriod') 
    def periodUtil; 

    @Service('BPReportDTICompetitivenessIndexService')
    def svc; 

    @Service('BusinessPermitTypeService')
    def permitTypeSvc;     

    String title = "DTI Competitiveness Index";
    String reportpath = 'com/rameses/gov/etracs/bpls/reports/'; 

    def permitTypes;
    def periodTypes;
    def templates = []; 
    def daterefs = [];
    
    def formControl = [
        getControlList: { 
            return [
                [type:'combo', caption:'Template', name:'entity.template', required:true, items:'templates',  expression:'#{item.title}', preferredSize:'0,20', captionWidth:100], 
                [type:'combo', caption:'Permit Type', name:'entity.permittype', required:true, items:'permitTypes',  expression:'#{item.title}', preferredSize:'0,20', captionWidth:100], 
                [type:'combo', caption:'Date Ref', name:'entity.dateref', required:true, items:'daterefs',  expression:'#{item.title}', preferredSize:'0,20', captionWidth:100], 

                [type:'combo', caption:'Period', name:'entity.period', required:true, items:'periodTypes',  expression:'#{item.title}', preferredSize:'100,20', captionWidth:100],
                [type:'integer', caption:'Year', name:'entity.year', required:true, depends:'entity.period', visibleWhen:'#{ entity.period.type.matches("yearly|quarterly|monthly")==true }', preferredSize:'100,20', captionWidth:100 ],
                [type:'combo', caption:'Quarter', name:'entity.qtr', required:true, items:'periodUtil.quarters', depends:'entity.period', visibleWhen:'#{ entity.period.type=="quarterly" }', preferredSize:'100,20', captionWidth:100],
                [type:'combo', caption:'Month', name:'entity.month', required:true, items:'periodUtil.months', expression:'#{item.title}', depends:'entity.period', visibleWhen:'#{ entity.period.type=="monthly" }', preferredSize:'100,20', captionWidth:100],
            ]; 
        } 
    ] as FormPanelModel; 

    def initReport() {
        def outcome = super.initReport(); 
        permitTypes = permitTypeSvc.getList(); 
        entity.permittype = (permitTypes? permitTypes.first() : null); 
        
        periodTypes = periodUtil.types.findAll{( it.type.matches('yearly|quarterly|monthly') )} 
        entity.period = (periodTypes ? periodTypes.first() : null);
        
        templates = [
            [objid: 'default', title: 'Default', report: reportpath + 'dti_competitiveness_index.jasper'] 
        ]; 
        entity.template = templates.first(); 
        
        daterefs = [
            [objid: 'date-applied', title: 'Application Date'],
            [objid: 'date-released', title: 'Released Date']
        ]; 
        entity.dateref = daterefs.first(); 
        return outcome; 
    } 
    
    public String getReportName() {
        if ( !entity.template?.report ) 
            throw new Exception('Please select a template first'); 
            
        return entity.template.report; 
    }
    
    void buildReportData(entity, asyncHandler) { 
        def m = periodUtil.build( entity.period.type, entity ); 
        entity.startdate = m.startdate;
        entity.enddate = m.enddate; 
        
        svc.getReport( entity, asyncHandler );
    }
    
    void buildResult( data ) { 
        if ( !data.reportdata ) 
            throw new Exception('No available record(s) that matches your criteria.');
            
        report_param = data.info;
        data.reportdata.eachWithIndex{ o,idx-> 
            o.rownum = idx+1;
            if ( o.address?.text ) {
                o.address.text = o.address.text.toString().replace('\n',' '); 
            }
            if ( o.businessnature ) {
                def arr = o.businessnature.toString().split(','); 
                o.businessnature = resolveBusinessNature( arr ); 
            }
        }
    } 
    
    def resolveBusinessNature( arr ) {
        def list = []; 
        arr.each{ list << it }
        list.unique(); 
        return list.join(', '); 
    }
    
    def report_param;
    Map getParameters(){
        return report_param; 
    }
}
