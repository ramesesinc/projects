package com.rameses.gov.etracs.landtax.report.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class BuildDelinquencyModel 
{
    @Binding
    def binding;
    
    @Service('LandTaxBuildDelinquencyService') 
    def svc;

    @Service('PersistenceService') 
    def persistence;
    
    @Service('DateService')
    def dtSvc;
    
    String title = 'Build Realty Tax Delinquency';
    
    def MODE_INIT = 'init';
    def MODE_BUILD = 'build';
    def MODE_LOAD_LEDGERS = 'loadledger';
    def MODE_DELETE = 'delete';
    def MODE_COMPLETED = 'completed';
    
    
    def entity;
    def mode;
    def processing = false;
    def task;
    def msg;
    def errors = [];
    def ignoredErrors = [];
    def threadCount = 1;
    def threadCounts = [1,2,3];


    
    def init(){
        entity = svc.openBuild();
        if (entity){
            showErrorMsg();
            mode = MODE_BUILD;
            return 'default';
        }else {
            entity = [:];
            entity.dtcomputed = dtSvc.getServerDate();
            mode = MODE_INIT;
            return 'init';
        }
    }
    
        
    def buildLedgers() {
        entity.putAll(svc.build(entity));
        listHandler.reload();
        
        task = new BuildDeliquentLedgerTask([
                svc           : svc,
                entity        : entity,
                updateStatus  : updateStatus,
                afterLoadLedgers : afterLoadLedgers
        ]);
    
        task.start();
        mode = MODE_LOAD_LEDGERS;
        processing = true;
        msg = 'Loading ledgers to build...';
        return 'default';
    }
    
    void showErrorMsg(){
        if (!processing && isHasErrors()){
            msg = 'There are build errors. Reschedule or clear the errors to complete the build process.'
        }
        binding?.refresh('msg|errorCount');
    }
    
    def isHasErrors(){
        return entity.barangays.find{it.errors > 1} != null;
    }
    
    def afterLoadLedgers = {
        task = null;
        processing = false;
        msg = null;
        entity.putAll(svc.openBuild());
        listHandler.reload();
        binding.refresh();
        buildDelinquency();
    }
    
    def updateStatus = {barangay ->
        listHandler.refreshItem(barangay);
        binding.refresh('ledgerCount|totalProcessed|totalErrors|selectedErrorBrgy|selectedIgnoredBrgy|selectAll|deselectAll');
    }
    
    def onComplete = {
        task = null;
        msg = null;
        processing = false;
        showErrorMsg();
        binding.refresh();
    }
    
    def selectedItem;

    def listHandler = [
        getRows  : { entity.barangays ? entity.barangays.size() : 0},
        fetchList : { entity.barangays },
    ] as BasicListModel


    void moveToTop() {
        if (!selectedItem) return;
        def idx = entity.barangays.indexOf(selectedItem);
        def brgy = entity.barangays.remove(idx);
        entity.barangays.add(0, brgy);
        renumberBarangays();
        listHandler.reload();
    }

    void renumberBarangays() {
        entity.barangays.eachWithIndex{brgy, idx ->
            brgy.idx = (idx + 1)
            brgy._schemaname = 'report_rptdelinquency_barangay'
            persistence.update(brgy);
        }
    }
    
    
    def getLedgerCount(){
        return entity.barangays.count.sum();
    }
    
    def getTotalProcessed(){
        return entity.barangays.processed.sum();
    }
    
    def getTotalErrors(){
        return entity.barangays.errors.sum();
    }
    
    def getTotalIgnored(){
        return entity.barangays.ignored.sum();
    }
    
    void buildDelinquency(){
        task = new BuildDelinquencyTask([
                svc           : svc,
                entity        : entity,
                updateStatus  : updateStatus,
                onComplete    : onComplete,
                threadCount   : threadCount,
        ]);
    
        task.start();
        mode = MODE_BUILD;
        processing = true;
        msg = 'Building delinquencies. Please wait...';
        binding.refresh();
    }
    
    def afterDelete = {
        listHandler.reload();
        refreshErrors();
        refreshIgnoredErrors();
        def outcome = init();
        binding.fireNavigation(outcome);
    }
    
    def delete(){
        if (MsgBox.confirm('Delete delinquency records?')){
            task = new DeleteDelinquencyTask([
                    svc     : svc,
                    entity  : entity,
                    afterDelete : afterDelete,
            ]);
            msg = 'Deleting delinquency records...';
            task.start();
            mode = MODE_DELETE;
            binding.refresh();
        }
    }    
    
    void cancel(){
        task.cancel();
        processing = false;
        showErrorMsg();
    }
    
    
    
    /*-------------------------------------------------------------
    **
    ** ERRORS TAB SUPPORT 
    ** 
    -------------------------------------------------------------*/
    def selectedErrorBrgy;
    def selectedError;
    def errorCount = 0;
    def selectDeselect = false;
    
    
    def errorListHandler = [
        getRows  : { errors.size() },
        fetchList : { errors },
    ] as EditorListModel
    
    
    void setSelectedErrorBrgy(selectedErrorBrgy){
        if (!selectDeselect){
            this.selectedErrorBrgy = selectedErrorBrgy;
            errors = svc.getBuildErrors([objid: entity.objid, barangayid: selectedErrorBrgy?.barangayid]);
            errorCount = errors.size();
            binding.refresh('errorCount|errorActions');
        }
        selectDeselect = false;
    }
    
    void refreshErrors(){
        errors = svc.getBuildErrors([objid: entity.objid, barangayid: selectedErrorBrgy?.barangayid]);
        errorListHandler.reload();
        showErrorMsg();
    }
    
    def getBarangaysWithErrors(){
        return entity.barangays.findAll{it.errors > 0 };
    }
    
    
    void rescheduleError(){
        def selecteditems = errors.findAll{it.selected}
        if (!selecteditems){
            MsgBox.alert("Select at least one item to reschedule.");
        }else {
            if (MsgBox.confirm('Reschedule selected errors for build?')){
                svc.rescheduleErrors(selecteditems);
                entity.putAll(svc.openBuild());
                listHandler.reload();
                refreshErrors();
            }
        }
    }
    
    void ignoreError(){
        def selecteditems = errors.findAll{it.selected}
        if (!selecteditems){
            MsgBox.alert("Select at least one item to ignore.");
        }else {
            def msg = 'Ignored error could no longer be computed.\n';
            msg += 'Proceed anyway?';

            if (MsgBox.confirm(msg)){
                svc.ignoreErrors(selecteditems);
                entity.putAll(svc.openBuild());
                listHandler.reload();
                refreshErrors();
                refreshIgnoredErrors();
                binding.refresh();
            }
        }
    }
    
     def getDisableSelectAll(){
        def disable = false;
        if (processing || errorCount == 0){
            disable = true;
        }else if (errors.find{ !it.selected } == null){
            disable = true;
        }
        return disable;
    }
    
    void selectAll(){
        errors.each{
            it.selected = true;
        }
        selectDeselect = true;
        errorListHandler.selectAll();
    }
        
    def getDisableDeselectAll(){
        def disable = false;
        if (processing || errorCount == 0){
            disable = true;
        }else if (errors.find{ it.selected } == null){
            disable = true;
        }
        return disable;
    }
    
    
    void deselectAll(){
        errors.each{
            it.selected = false;
        }
        selectDeselect = true;
        errorListHandler.deselectAll();
    }
    
    def openLedger(){
        def opener = Inv.lookupOpener('rptledger:open', [entity: selectedError]);
        opener.target = 'popup';
        return opener;
    }
    
    
    
    
    /*-------------------------------------------------------------
    **
    ** IGNORED ERRORS TAB SUPPORT 
    ** 
    -------------------------------------------------------------*/
    def selectedIgnoredBrgy;
    def selectedIgnoredError
    def ignoredCount = 0;
    
    
    
    def ignoredListHandler = [
        getRows  : { ignoredCount },
        fetchList : { getIgnoredErrors() },
    ] as BasicListModel
    
    def getIgnoredErrors(){
        ignoredErrors = svc.getIgnoredErrors([objid: entity.objid, barangayid: selectedIgnoredBrgy?.barangayid]);
        ignoredCount = ignoredErrors.size();
        binding.refresh('ignoredCount');
        return ignoredErrors;
    }
    
    void refreshIgnoredErrors(){
        ignoredErrors = svc.getIgnoredErrors([objid: entity.objid, barangayid: selectedIgnoredBrgy?.barangayid]);
        binding.refresh('selectedIgnoredBrgy');
        ignoredListHandler.reload();
    }
    
    def getBarangaysWithIgnoredErrors(){
        return entity.barangays.findAll{it.ignored > 0 };
    }
    
}


