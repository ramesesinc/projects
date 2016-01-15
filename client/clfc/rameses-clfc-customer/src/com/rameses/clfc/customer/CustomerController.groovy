package com.rameses.clfc.customer;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class CustomerController 
{
    @Binding
    def binding;
    
    @Service('')
    
    //feed by the caller
    def callerContext = [:];
    
    def entity = [:];
    def mode = 'read';
    def allowCreate = true;
    def allowEdit = true;
    def saveHandlers = [:];
    def optiontype;
    
        
    def optionList = [
        [caption: 'Individual', value: 'INDIVIDUAL'],
        [caption: 'Juridical', value: 'JURIDICAL']
    ];
    
    def optionListHandler = [
        fetchList: { o->
            return optionList;
        }
    ] as BasicListModel;
    
    final void init() {
        optiontype = 'INDIVIDUAL';
        mode = 'init';
        menuItems = null;    
        selectedMenu = null;
    } 
    
    def getTitle() { 
        return '<font color="#808080" size="5"><b>Customer</b></font><br>'; 
    } 

    boolean isAllowSelect() {
        return (callerContext?.selectHandler != null);
    }
    
    def select() {
        if (callerContext.selectHandler) 
            callerContext.selectHandler(entity);
        
        return '_close';
    }
    
    @Close
    void closing() { 
        if (callerContext?.closeHandler) 
            callerContext.closeHandler(); 
    } 
    
    def close() {
        closing();
        return '_close';
    }
    
    def create() { 
        //mode = 'create';
        init();
        entity = [objid: 'CUST'+new UID(), address:[:]]; 
        return 'default';
    } 
    
    def next() {
        entity.type = optiontype;
        mode = 'create';
        binding?.refresh();
        listHandler?.reload();
        return 'main';
    }

    def open() {
        mode = 'read'; 
        entity = callerContext.service.open([objid: entity.objid]); 
        if (!entity.address) entity.address = [:];
        return 'main';
    } 
    
    def oldentity = [:];
    
    void edit() {
        mode = 'edit';
        oldentity.clear();
        oldentity.putAll(entity);
    } 
    
    def cancelCreate() {
        if (MsgBox.confirm('Are you sure you want to cancel any changes made?')) {
            return '_close';
        } else {
            return null;
        }
    }
    
    void saveCreate() { 
        def data = callerContext.service.create(entity);  
        if (data != null) entity.putAll(data);
        
        mode = 'read'; 
    } 
    
    def cancelUpdate() {
        if (MsgBox.confirm('Are you sure you want to cancel any changes made?')) {
            mode = 'read';
            entity.clear();
            entity.putAll(oldentity); 
            oldentity.clear();
        } else { 
            return null;
        }
    }    

    void saveUpdate() {
        def data = callerContext.service.update(entity);
        if (data != null) entity.putAll(data);
        
        mode = 'read';
        oldentity.clear();
    }
    
    def createOpenerParams() {
        return [
            callerContext: new CustomerControllerContext(this, callerContext.service), 
            entity: entity 
        ]; 
    } 
    
    def menuItems;    
    def selectedMenu;
    def listHandler = [
        getDefaultIcon: {
            return 'Tree.closedIcon'; 
        },         
        getItems: { 
            if (menuItems == null) {
                def entitytype = entity?.type;
                def invtype = 'customer';
                if (entitytype == 'INDIVIDUAL') {
                    invtype = 'customerindividual';
                } else if (entitytype == 'JURIDICAL') {
                    invtype = 'customerjuridical';
                }
                def invokers = InvokerUtil.lookup(invtype + ':plugin');
                menuItems = [];
                for (inv in invokers) {
                    menuItems.add([caption:inv.caption, invoker:inv]); 
                } 
            }
            return menuItems;
        },
        beforeSelect: {o-> 
            return (mode == 'read');
        }
    ] as ListPaneModel;  
    
    def subFormHandler = [
        getOpener: {
            if (selectedMenu == null) {
                return new Opener(outcome:'blankpage'); 
            }
            
            def op = selectedMenu.opener;
            if (op == null) {
                op = InvokerUtil.createOpener(selectedMenu.invoker, createOpenerParams()); 
                selectedMenu.opener = op;
            } 
            return op; 
        } 
    ] as SubFormPanelModel;    
} 