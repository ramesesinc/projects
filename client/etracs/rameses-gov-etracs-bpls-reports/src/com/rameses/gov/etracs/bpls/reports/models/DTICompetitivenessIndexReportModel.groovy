package com.rameses.gov.etracs.bpls.reports.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

public class DTICompetitivenessIndexReportModel extends AsyncReportController {

    @Service('BPReportDTICompetitivenessIndexService')
    def svc; 

    @Service('BusinessPermitTypeService')
    def permitTypeSvc;     

    String title = "DTI Competitiveness Index";
    String reportpath = 'com/rameses/gov/etracs/bpls/reports/'; 

    def permitTypes;
    def templates = []; 
    
    def formControl = [
        getControlList: { 
            return [
                [type:'combo', caption:'Template', name:'entity.template', required:true, items:'templates',  expression:'#{item.title}', preferredSize:'0,20', captionWidth:100], 
                [type:'combo', caption:'Permit Type', name:'entity.permittype', required:true, items:'permitTypes',  expression:'#{item.title}', preferredSize:'0,20', captionWidth:100], 
                [type:'integer', caption:'Year', name:'entity.year', required:true, depends:'entity.period', preferredSize:'100,20', captionWidth:100 ] 
            ]; 
        } 
    ] as FormPanelModel; 

    def initReport() {
        def outcome = super.initReport(); 
        permitTypes = permitTypeSvc.getList(); 
        entity.permittype = (permitTypes? permitTypes[0] : null); 
        
        templates = [
            [objid: 'default', title: 'Default', report: reportpath + 'dti_competitiveness_index.jasper'] 
        ]; 
        entity.template = templates.first(); 
        return outcome; 
    } 
    
    public String getReportName() {
        if ( !entity.template?.report ) 
            throw new Exception('Please select a template first'); 
            
        return entity.template.report; 
    }
    
    void buildReportData(entity, asyncHandler) { 
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
