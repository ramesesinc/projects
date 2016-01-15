package com.rameses.clfc.loan.billing;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;

class LoanLedgerBillingController //extends CRUDController
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    @Service("DateService")
    def dateSvc;
    
    @Service("LoanLedgerBillingService")
    def service;
    
    //String serviceName = 'LoanLedgerBillingService';
    def entity;
    def mode = 'read';
    
    String entityName = 'ledgerbilling';
    def collectorLookupHandler = InvokerUtil.lookupOpener('route-collector:lookup', [:]);
    public String getTitle() {
        String text = "Collection Sheet";

        if (mode == "create")
          return text + " (New)";
        if (mode == "edit") {
          return text + " (Edit)";
        }
        return text;
    }   
    /*String createFocusComponent = 'entity.collector';
    String editFocusComponent = 'entity.collector';  
    boolean showConfirmOnSave = false;
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;*/
    
    def loadingOpener = Inv.lookupOpener("popup:loading", [:]);
    
    //def service;
    /*LoanLedgerBillingController() {
        try {
            service = InvokerProxy.instance.create('LoanLedgerBillingService');
        } catch (ConnectException ce) {
            ce.printStackTrace();
            throw ce;
        }
    }*/
    
    void create() {
        entity = createEntity();
        mode = 'create';
        listHandler?.reload();
    }
    
    void open() {
        entity = service.open(entity);
        mode = 'read';
    }
    
    Map createEntity() {
        return [ 
            objid       :'LB'+new java.rmi.server.UID(), 
            routes      :[],
            billdate    : dateSvc.getServerDateAsString().split(" ")[0]
        ];
    }    

    def prevroutes;
    def selectedItem;
    
    @ChangeLog
    def changeLog;
    
    def listHandler = [
        fetchList: { o->
            if (!entity.routes) entity.routes = [];
            return entity.routes; 
        }
    ] as BasicListModel;
    
    /*void beforeSave( data ) {
        if (!data.routes) throw new Exception('Please specify route(s) for this collector.');
        //if (mode == 'edit') allowEdit = false;
    } */       
    
    def save() {
        if (!entity.routes)
            throw new Exception("Please specify route(s) for this collector.");
                    
        def handler;
        if (!handler) {
            handler = [
                onMessage: { o->
                    //println 'onMessage '  + o;
                    //println 'EOF ' + AsyncHandler.EOF;
                    if (o == AsyncHandler.EOF) {
                        loadingOpener.handle.binding.fireNavigation("_close");
                        return;
                    }        

                    loadingOpener.handle.binding.fireNavigation("_close");
                    entity.putAll(o);
                    entity._added = [];
                    entity._removed = [];
                    def msg = "Billing created successfully!";
                    if (mode == 'edit') msg = "Billing updated successfully!";
                    mode = 'read';
                    binding?.refresh();
                    MsgBox.alert(msg);
                },
                onTimeout: {
                    handler?.retry(); 
                },
                onCancel: {
                    println 'processing cancelled.';
                    //fires when cancel() method is executed 
                }, 
                onError: { o->
                    loadingOpener.handle.binding.fireNavigation("_close");
                    MsgBox.err(o.message);
                    /*if (o instanceof java.util.concurrent.TimeoutException) {

                    } else {
                        throw new Exception(o.message);
                    }*/
                }
            ] as AbstractAsyncHandler;
        }
        if (mode == 'create') {
            service.create(entity, handler);
        } else if (mode == 'edit') {
            service.update(entity, handler);
        }
        return loadingOpener;
        //svc.rebuild(entity, handler);
    }
    
    public boolean getEditable() {
        def flag = false;
        if (mode == 'read' && entity.editable) flag = true;
        return flag;
    }  

    public boolean getResetable() {
        def flag = false;
        if (mode == 'read' && entity.resetable) flag = true;
        return flag;
    }

    def addItem() {
        def handler = {route->
            if (entity.routes.find{ it.code == route.code })
                throw new Exception('Route ' + route.description + '-' + route.area + ' already selected.');
            if (!entity._added) entity._added = [];
            entity._added.add(route);
            entity.routes.add(route);
            listHandler.reload();
        }
        return InvokerUtil.lookupOpener('route:lookup', [onselect: handler]);
    }
    
    /*def edit( data ) {
        prevroutes = [];
        prevroutes.addAll(entity.routes);
        allowEdit = true;
        super.edit();
    }*/
    
    void edit() {
        mode = 'edit';
        prevroutes = [];
        def item;
        entity.routes.each{ o->
            item = [:];
            item.putAll(o);
            prevroutes.add(item);
        }
    }
    
    def cancel() {
        if (mode == 'edit') {
            mode = 'read';
            if( changeLog.hasChanges() ) {
                changeLog.undoAll()
                changeLog.clear()
            }

            entity.routes = [];
            entity.routes.addAll(prevroutes);
            
            return null;
        }
        return close();
    }
    
    def close() {
        return "_close";
    }
    
    /*void beforeCancel() {
        allowEdit = false;
    }

    void afterCancel() {
        entity.routes.clear();
        entity.routes.addAll(prevroutes);
    }*/

    def removeItem() {
        if (selectedItem == null) return;
        if (MsgBox.confirm("You are about to remove the selected item. Continue?")) {
            if (!entity._removed) entity._removed = [];
            entity._removed.add(selectedItem);
            if (entity._added) entity._added.remove(selectedItem);
            entity.routes.remove(selectedItem);
            listHandler.reload();
        }
    }   

    def reset() {
        if (!MsgBox.confirm("You are about to reset this billing. Continue?")) return;
        
        def handler;

        if (!handler) {
            handler = [
                onMessage: { o->
                    //println 'onMessage '  + o;
                    //println 'EOF ' + AsyncHandler.EOF;
                    if (o == AsyncHandler.EOF) {
                        loadingOpener.handle.binding.fireNavigation("_close");
                        return;
                    }    

                    loadingOpener.handle.binding.fireNavigation("_close");                    
                    EventQueue.invokeLater({ caller?.reload(); });
                    MsgBox.alert("Resetting has been successfully processed.", true);
                },
                onTimeout: {
                    handler?.retry(); 
                },
                onCancel: {
                    println 'processing cancelled.';
                    //fires when cancel() method is executed 
                }, 
                onError: { o->
                    loadingOpener.handle.binding.fireNavigation("_close");
                    MsgBox.err(o.message);
                    //throw new Exception(o.message);
                    //println 'o';
                    /*if (o instanceof java.util.concurrent.TimeoutException) {

                    } else {
                        throw new Exception(o.message);
                    }*/
                }
            ] as AbstractAsyncHandler;
        } 
        service.resetBilling(entity, handler);
        return loadingOpener;
    }
}
