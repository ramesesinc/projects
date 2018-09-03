package com.rameses.gov.etracs.rptis.report.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;


class RoaReportModel extends AsyncReportModel2
{
    @Service('RPTReportROAService') 
    def svc;
    
    @Service('DateService')
    def dtSvc; 
    
    @Service('BarangayService')
    def brgySvc;
    
    @Service('QueryService')
    def querySvc 
    
    String title = 'Record of Assessment';
    
    String reportName = 'com/rameses/gov/etracs/rptis/reports/rptroa.jasper';
    
    void buildReportData(entity, asyncHandler){
        svc.getROA(entity, asyncHandler);
    }
    
    
    def getFormControl(){
        def controls = []
        controls << new FormControl( "combo", [captionWidth:110, caption:'Revision Year', name:'entity.ry', items:'revisionyears', emptyText:'ALL']);
        controls << new FormControl( "combo", [captionWidth:110, caption:'Period Type', name:'entity.periodtype', required:true, items:'periodtypes', allowNull:false, expression:'#{item.caption}', immediate:true])
        controls << new FormControl( "integer", [captionWidth:110, caption:'Year', name:'entity.year', required:true, depends:'entity.periodtype', visibleWhen:'#{entity.periodtype?.type=="period"}'])
        controls << new FormControl( "combo", [captionWidth:110, caption:'Quarter', name:'entity.qtr', required:true, items:'quarters', depends:'entity.periodtype', visibleWhen:'#{entity.periodtype?.type=="period"}'])
        controls << new FormControl( "combo", [captionWidth:110, caption:'Month', name:'entity.month', depends:'entity.periodtype,entity.qtr', items:'months', expression:'#{item.caption}', dynamic:true, visibleWhen:'#{entity.periodtype?.type=="period"}'])
        controls << new FormControl( "date", [captionWidth:110, caption:'As of Date', name:'entity.asofdate', required:true, depends:'entity.periodtype', visibleWhen:'#{entity.periodtype?.type!="period"}'])
        controls << new FormControl( "combo", [captionWidth:110, caption:'LGU', name:'entity.lgu', required:true, allowNull:false, items:'lgus', expression:'#{item.name}'])
        controls << new FormControl( "combo", [captionWidth:110, caption:'Barangay', name:'entity.barangay', required:true, allowNull:false, items:'barangays', expression:'#{item.name}', depends:'entity.lgu', dynamic:true])
        controls << new FormControl( "combo", [captionWidth:110, caption:'Classification', name:'entity.classification', items:'classifications', expression:'#{item.name}'])
        controls << new FormControl( "text", [captionWidth:110, caption:'Section', name:'entity.section'])
        return [
            getFormControls :{controls}
        ] as FormPanelModel;
    }
    
    
    Map getParameters(){
        return data.parameters;
    }    
    
    List getClassifications(){
        def q = [_schemaname:'propertyclassification', where:['1=1'], orderBy:'orderno']
        return querySvc.getList(q)
    }
    
   def getPeriodtypes(){
        return [
            [type:'period', caption:'STANDARD'],
            [type:'asof', caption:'AS OF DATE'],
        ]
   }    
}
