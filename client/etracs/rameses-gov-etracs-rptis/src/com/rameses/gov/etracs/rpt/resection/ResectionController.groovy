package com.rameses.gov.etracs.rpt.resection;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.RPTUtil;
import com.rameses.util.MapBeanUtils;

public class ResectionController extends PageFlowController
{

    @Binding
    def binding;
    
            
    @Service('RPTTaskService')
    def taskSvc;
    
    @Service('ResectionService')
    def svc 
            
    @Invoker
    def invoker 
            
    @FormId
    def formId;
    
    @FormTitle
    def formTitle;
   
               
    def entity;
    def sections;
    def selectedSection;
    def messages = [];
    
    
    def init(){
        formTitle = 'Resection (New)'
        entity =  [objid:RPTUtil.generateId('RS'), newsectioncount:2]
        return super.signal('init')
    }
    
    
    def open(){
        initOpen()
        return super.signal('open');
    }
    
    
    void initOpen(){
        entity = svc.openResection(entity.objid);
        loadSections();
        formId = entity.txnno;
        formTitle = 'Resection: ' + entity.txnno;
    }
      
    void loadSections(){
        sections = InvokerUtil.lookupOpeners('resection:info', [entity:entity, svc:svc])
        if (sections){
            selectedSection = sections[0];
        }
    }    

    /*-----------------------------------------------------
     * 
     * DOCUMENT SUPPORT 
     *
     *----------------------------------------------------*/   
    
    void delete(){
        svc.deleteResection(entity);
    }
    
    List getBarangays(){
        return svc.getBarangays();
    }
    
    

    /*-----------------------------------------------------
     * 
     * WORKFLOW ACTIONS
     *
     *----------------------------------------------------*/
    void initResection(){
        entity =  svc.initResection(entity);
        initOpen();
    }
    
    
    void submitForTaxmapping(){
        checkMessages();
        entity = svc.submitForTaxmapping(entity);
        loadSections();
    }
    
    void submitForAppraisal(){
        checkMessages();
        entity = svc.submitForAppraisal(entity);
        loadSections();
    }    
    
    void submitForApproval(){
        checkMessages();
        entity = svc.submitForApproval(entity);
        loadSections();
    }
    
    /*
    
    void approveResection() {
        checkMessages();
        entity = svc.approveResection(entity);
        loadSections();
    }
     */
    void approveResection() {
        checkMessages();
        info = '';
        processing = true;
        approveTask = new ApproveTask(
                    svc             : svc, 
                    entity          : entity,
                    oncomplete      : oncomplete,
                    showinfo        : showinfo,
                    onerror         : onerror,
                );
        Thread t = new Thread(approveTask);
        t.start();
    }    


    void disapproveResection() {
        checkMessages();
        entity = svc.disapproveResection(entity);
        loadSections();
    }

    

    void submitToProvince() {
        checkMessages();
        entity = svc.submitToProvince(entity);
        loadSections();
    }

   
    void disapproveSubmitToProvice() {
        checkMessages();
        entity = svc.disapproveSubmitToProvice(entity);
        loadSections();
    }


    void approveSubmittedToProvince(){
        checkMessages();
        entity = svc.approveSubmittedToProvince(entity)
        loadSections();
    }
    
    
    void disapproveSubmittedToProvince(){
        checkMessages();
        entity = svc.disapproveSubmittedToProvince(entity)
        loadSections();
    }
    
    
    void approveByProvince() {
        checkMessages();
        entity = svc.approveByProvince(entity);
        loadSections();
    }


    void disapproveByProvince() {
        checkMessages();
        entity = svc.disapproveByProvince(entity);
        loadSections();
    }
    
    
    
    
    
    def approveTask = null;
    def info;
    def processing = false;
    def haserror = false;
    
    
    def oncomplete = {
        haserror = false;
        processing = false;
        approveTask = null;
        binding.refresh();
    }
    
    def onerror = {
        haserror = true;
        processing = false;
        approveTask = null;
        showinfo('ERROR: ' + it)
    }
    
    def showinfo = { msg ->
        info += msg;
        binding.refresh('info');
    }
    
        
    void checkErrors(){
        if (processing)
            throw new Exception('Resection Approval is ongoing.');
    }
    
    void checkFinish(){
        if (haserror)
            throw new Exception('Approval cannot be completed due to errors. Fix errors before finishing the transaction.')
        if (processing)
            throw new Exception('Resection Approval is ongoing.');
        initOpen();
    }

    
    
    
    
    
    
    
    void addMessage(msg){
        messages << msg;
    }
    
    
    void clearMessages(type){
        messages.removeAll( messages.findAll{it.type == type} )
    }
    

    void checkMessages(){
        if (messages)
            throw new Exception(messages[0].msg);
    }   
    
    

    
    void assignTaxmapper(){
        doAssignTask('fortaxmapping')
    }
    
    void assignAppraiser(){
        doAssignTask('forappraisal')
    }
    
    void assignApprover(){
        doAssignTask('forapproval')
    }
    
    void doAssignTask(newaction){
        def task = taskSvc.findCurrentTask(entity.objid);
        task.action = newaction;
        task.msg = '';
        taskSvc.createNextUserTask(task);
        initOpen();
    }    
    
        
}






public class ApproveTask implements Runnable{
    def svc;
    def entity;
    def oncomplete;
    def onerror;
    def showinfo;

    public void run(){
        try{
            showinfo('Initializing');
            svc.initApproveResectionAsync(entity);
            showinfo(' .... Done\n');
        
            
            showinfo('\nProcessing Land FAAS...\n');
            svc.getResectionAffectedRpus(entity.objid).findAll{it.rputype == 'land'}.each{ land ->
                if ( ! land.newfaasid) {
                    showinfo('Creating new Land FAAS for TD No. ' + land.tdno );
                    svc.createLandFaasRecord(entity, land);
                    showinfo(' .... Done\n');
                }
            }
            
            showinfo('\nProcessing Improvement FAAS...\n');
            svc.getResectionAffectedRpus(entity.objid).findAll{it.rputype != 'land'}.each{ arpu ->
                if ( ! arpu.newfaasid) {
                    showinfo('Creating new FAAS for TD No. ' + arpu.tdno );
                    svc.createImprovementFaasRecord(entity, arpu);
                    showinfo(' .... Done\n');
                }
            }
            
            
            showinfo('\nAssigning new TD No. to Lands and Affected Improvements...');
            svc.assignNewTdNos(entity);
            showinfo(' .... Done\n');
            
            showinfo('\nApproving new FAAS Records...\n');
            svc.getResectionAffectedRpus(entity.objid).each{ arpu ->
                showinfo('Approving New TD No. ' + arpu.tdno );
                svc.approveFaasRecordAsync(arpu);
                showinfo(' .... Done\n');
            }
                    
            

            showinfo('\nResection Approval')
            svc.approveResectionAsync(entity);
            entity.state = 'APPROVED';
            showinfo(' .... Done\n');
            
            
            oncomplete()
        }
        catch(e){
            onerror('\n\n' + e.message )
        }
    }
    
    void doSleep(){
        try{
            Thread.sleep(2000);
        }
        catch(e){
            ;
        }
    }
}