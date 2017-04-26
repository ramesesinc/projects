package com.rameses.gov.etracs.rpt.report;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;
import com.rameses.rcp.framework.TaskManager;

abstract class AbstractBatchReportController
{
    @Binding
    def binding

    @Service('LGUService')
    def lguSvc
            
    @Service('Var')
    def var 
            
    def title='FAAS Batch Printing'

    def params;
            
    def msg 
    def mode 
    def pagetypes;
    def selectiontypes;
    def states;
    boolean interrupt;
            
    public void afterInit(params){
    }
    
    public void init() {
        params = [:]
        params.revisionyear = var.get('current_ry');
        states = ['CURRENT', 'CANCELLED'];
        params.state = 'CURRENT';
        params.selectiontype = selectiontypes.find{it.type=='bysection'}
        params.pagetype = null;
        params.printinterval = 1;
        params.copies = 1;
        params.showprinterdialog = false;
        afterInit(params)
        mode='init';
    }
            
    def updateMessage = { msg ->
        this.msg = msg
        binding.refresh('msg')
    }

    def onFinish = { msg -> 
        this.msg = msg
        this.mode = 'init';
        binding.refresh();
    }

    def onError = { err ->
        mode = 'init';
        msg = err;
        binding.refresh();
    }

    def cancelPrinting() {
        interrupt = true;
        this.mode = 'init'
        msg = 'Printing of faas has been cancelled.  '
        binding.refresh();
    }

    public void print() {
        if (params.copies <= 0) 
            throw new Exception('No. of Copies must be equal or greater than 1.');
            
        mode = 'processing';
        Thread t = new Thread( batchTask)
        t.start();
    }
    
    def getSelectiontypes(){
        if (!selectiontypes)
        selectiontypes = [
            [type:'bysection', caption:'By Section'],
            [type:'bytdrange', caption:'TD No. Range'],
            [type:'bybrgy', caption:'By Barangay'],
        ]
        return selectiontypes;
    }
            
    def getPagetypes(){
        if (!pagetypes)
        pagetypes = ['FRONT', 'BACK']
        return pagetypes;
    }
    
    def getLgus(){
        def orgclass = OsirisContext.env.ORGCLASS
        def orgid = OsirisContext.env.ORGID

        if ('PROVINCE'.equalsIgnoreCase(orgclass)) {
            return lguSvc.lookupMunicipalities([:])
        }
        else if ('MUNICIPALITY'.equalsIgnoreCase(orgclass)) {
            return [lguSvc.lookupMunicipalityById(orgid)]
        }
        else if ('CITY'.equalsIgnoreCase(orgclass)) {
            return [lguSvc.lookupCityById(orgid)]
        }
        return []
    }
    
    def getBarangays(){
        if (! params.lgu)
            return [];
        return lguSvc.lookupBarangaysByRootId(params.lgu?.objid);
    }
    
    public def getItems(params){}
    public def getReportInvokerName(){}    
    public def getReportParameters(){ return [:] }
    public def continueOnError(){return false}
    public def getItemMessage(data, copycount){
        return "Printing copy # " + copycount + "." 
    }
    
    public def getReportData(entity){
        return [entity:entity]
    }
        
    
    def batchTask = [
        run : {
            def error = false;
            def list = null;
            try {
                list = getItems(params);
            }
            catch(e){
                onError(e.message);
                return;
            }

            if( !list){
                onError('No records found.');
                return;
            }

            def data = list.remove(0);
            def reportparams = [PRINTPAGE:params.pagetype?.toLowerCase()];

            while(!interrupt && data) {
                def reportdata = getReportData(data);
                def p = getReportParameters();
                if(!p) p = [:];
                reportparams += p;
                reportdata.reportparams = reportparams

                try{
                    updateMessage(getItemMessage(data, 0));
                    
                    def reportInvoker = Inv.lookupOpener(getReportInvokerName(), reportdata )
                    def report = reportInvoker.handle.report.report
                    
                    if (params.copies >= 1){
                        1.upto(params.copies){copycnt -> 
                            ReportUtil.print( report, params.showprinterdialog) ;
                            updateMessage(getItemMessage(data, copycnt));
                            Thread.sleep(params.printinterval * 1000)
                        }
                    }
                } catch(e) {
                    e.printStackTrace();
                    if (!continueOnError()){
                        error = true;
                        onError( e );
                        break;
                    }
               }
               data = (list ? list.remove(0) : null);
            }    
            def msg = "Batch printing has been successfully completed."
            if(interrupt) 
                msg = 'Batch printing has been interrupted.'
            else if (!error)
                onFinish(msg );
        }
    ] as Runnable
            
    
    
}
