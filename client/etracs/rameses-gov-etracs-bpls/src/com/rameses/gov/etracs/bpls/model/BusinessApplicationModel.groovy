package com.rameses.gov.etracs.bpls.model;

import com.rameses.rcp.annotations.*; 
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;
import java.rmi.server.*;
import com.rameses.util.*;

class  BusinessApplicationModel extends WorkflowController {

    @Service("BusinessApplicationWorkflowService")
    def service;

    @Service("BusinessPermitService")
    def permitSvc;

    @Service("BusinessApplicationService")
    def appService;

    @Script("BusinessAssessmentInfoUtil")
    def assessmentInfo;

    @Script("BusinessInfoUtil")
    def appinfo;

    @Service("BusinessPaymentService")
    def paymentSvc;


    def sections = [];
    def currentSection;
    def subform;
    def barcodeid;

    @FormId
    def formId;

    @FormTitle
    def formTitle;

    @Caller
    def caller;

    String entityName = "business_application:section";
    def prevmode = '';
    def tmpmode = '';

    boolean has_loaded_assessment = false;

    def _option;

    def showUnifiedFormMenu() {
        def h = { t->
            if(t) open();
            binding.refresh();
        }
        def list = Inv.lookupOpeners( "business_application:unifiedform", [entity:entity, handler:h] );
        def pop = new PopupMenuOpener();
        list.each {
            pop.add( it );
        }
        return pop;
    }

    def open() { 
        if ( entity.taskid ) {
            super.open(); 
            
        } else {
            entity = appService.open( [objid: entity.objid] );
            buildExtActions();
            afterOpen(entity);
        } 
        
        _option = entity._option; 
    } 

    void openBarcode() {
        entity = service.getTaskFromBarcode( [barcodeid: barcodeid ]);
        if( entity ) {
            super.open();
        }
        else {
            entity = appService.openByAppno( [appno: barcodeid] );
            buildExtActions();
            afterOpen(entity);
        } 
        
        _option = entity._option;         
    }

    void afterOpen(o) {
        formId = entity.objid;
        formTitle = "BA:"+entity.appno;
        reloadSections();
    }

    def popupReports() {
        def popupMenu = new PopupMenuOpener();
        def list = InvokerUtil.lookupOpeners( "unifiedform", [entity:entity] );
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }

    void reloadSections()  {
        def handlers = Inv.lookupOpeners("business_application:section", [entity:entity, task:task]);
        sections.clear();
        sections.addAll( 
            handlers.findAll {
                def vw = it.properties.visibleWhen;
                return  ((!vw)  ||  ExpressionResolver.getInstance().evalBoolean( vw, [entity:entity, task:task] ));     
            }
        );    
        if(sections && !currentSection) {
            currentSection = sections[0];
        }
    }

    public String getTitle() {
        return " Application No. " + entity?.appno + (task!=null ?  " ["+ task.title + "]" : "" );
    }

    public void beforeSignal(o) {
        tmpmode = o.state; 
        if (o.state.contains('assessment')) {
            if (!has_loaded_assessment) throw new Exception('Please run assessment first'); 
            if (!entity.taxfees) throw new Exception('At least one tax/fee is required'); 
        } 
    }

    public void onEnd() { 
        if( task?.state == 'release' ) {
            entity.state = 'COMPLETED';
            entity.currenttask = task;  
            
        } else if( task.state == 'assessment' ) {
            //this should have moved but it is retained. 
            //this is a horrible workaround but no choice at this moment bec. of the workflowcontroller
            task.owner = false;
        } 

        def callbackhandler = callerListHandler;
        if( callbackhandler != null ) callbackhandler.reload();
    }      

    public void afterSignal( o ) {
        if(o.assessor) entity.assessor = o.assessor;
        if(o.approver) entity.approver = o.approver;
        prevmode = tmpmode;                
        reloadSections();        
        entity.currenttask = task; 
        def callbackhandler = callerListHandler;
        if( callbackhandler != null ) callbackhandler.reload();
    }

    public final boolean isAllowIssuePermit() { 
        if ( entity.apptype == 'RETIRE' ) return false; 
        else if ( entity.permit?.objid ) return false; 
        else if ( entity.state == 'COMPLETED' ) {
            def b = _option?.businesspermit_issuance_on_release; 
            return (b==true ? false : true); 
            
        } else if ( entity.state == 'RELEASE' ) { 
            def taskstate = entity.currenttask?.state; 
            boolean taskdone = ( entity.currenttask?.enddate ? true : false ); 
            def b = (_option?.businesspermit_issuance_on_release==true); 
            if ( taskstate=='release' && !taskdone && b ) return true; 
        } 
        return false; 
    } 
    public final boolean isAllowPrintPermit() { 
        if ( entity.apptype == 'RETIRE' ) return false; 
        else if ( entity.state.toString().matches('RELEASE|COMPLETED') ) { 
            return (entity.permit?.objid? true: false ); 
        } 
        return false; 
    } 

    def issuePermit() {
        if( entity.txnmode == 'CAPTURE' ) {
            boolean pass = false;
            def h = { pass = true; }
            def p = [applicationid:entity.objid, businessid:entity.business.objid ];
            Modal.show( "business_permit:capture", [entity:p, handler:h] ); 
            if(!pass) throw new BreakException();
            
            entity.permit = permitSvc.create(p);
            return printPermit();
        }
        
        def printout = null;
        if ( !entity.parentapplicationid ) {
            def h = { o->
                def p = [applicationid:entity.objid, businessid:entity.business.objid ];
                p.plateno = o.plateno;
                p.remarks = o.remarks;
                entity.permit = permitSvc.create( p );
                printout = printPermit();
            }
            Modal.show( "businessplate:ask", [handler: h] );
            if( printout ) return printout;
            
        } else {
            def p = [
                applicationid:entity.objid, businessid:entity.business.objid, 
                parentapplicationid: entity.parentapplicationid 
            ];
            entity.permit = permitSvc.create( p );
            return printPermit();
        } 
        throw new BreakException();
    }

    def printPermit() {
        return  Inv.lookupOpener("business_permit:print", [entity: entity] );
    }


    void cancel() {
        appService.cancel( entity );
    }

    def handleWarning( Warning w ) {
        Modal.show( 'business_redflag:warning', [list: w.info.list] );
        throw new BreakException();
    }  

    def viewBusiness() {
       return Inv.lookupOpener( "business:open", [entity:entity.business] );
    }

    def getCallerListHandler() {
        try { 
            return caller.listHandler;  
        } catch(e) { 
            return null; 
        }             
    } 
}
