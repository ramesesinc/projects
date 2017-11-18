package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
        
public class CashReceiptSelectionModel  {
        
    @Binding
    def binding;

    def clientContext = com.rameses.rcp.framework.ClientContext.currentContext;
    def session = OsirisContext.getSession();

    @Service("CashReceiptService")
    def cashReceiptSvc;

    @Service("CollectionTypeService")
    def collectionTypeSvc;

    String title = "Cash Receipt";

    def items = [];
    def formTypes;
    def formType;
    def formTitle;
    def homeicon;

    def txnmodes = ["ONLINE", "OFFLINE"];
    def txnmode = "ONLINE";
    
    @PropertyChangeListener
    def listener = [
        "formType" : { binding.refresh(); },
        "txnmode" : {binding.refresh(); }
    ];

    void init() {
        formType = null;
        formTitle = "Select accountable form";
        formTypes = collectionTypeSvc.getFormTypes();
        formType = formTypes.find{ it.objid == '51' };

        items.clear();
        def appEnv = clientContext.appEnv; 
        def customfolder = appEnv['app.custom']; 
        homeicon = 'images/' + customfolder + '/home-icon.png';  
        def custom_homeicon = clientContext.getResource(homeicon); 
        if (!custom_homeicon) homeicon = 'home/icons/folder.png';                  
        def list = [];
        Inv.lookupOpeners('cashreceipt:' + txnmode.toLowerCase(), [:]).each{
            def m = [
                caption : it.caption, 
                icon    : it.properties.icon, 
                opener  : it 
            ];
            def iicon = clientContext.getResource(m.icon);
            if (!iicon) m.icon = homeicon; 
            items << m;
        } 
    } 

    def model = [
        fetchList: {o-> 
            def xlist = [];
            xlist.addAll( items );
            def p = [formtype: formType.objid];  
            def list = null;
            if(txnmode == 'ONLINE') {
                list = collectionTypeSvc.getOnlineCollectionTypes(p);
            }
            else {
                list = collectionTypeSvc.getOfflineCollectionTypes(p);
            }
            list.each{
                it.caption = it.title;
                it.icon = homeicon;
                xlist << it;
            }
            return xlist;
        }, 
        onOpenItem: {o-> 
            if (o.opener) {
                try { 
                    def result = o.opener.handle.init(); 
                    if (result == null) throw new BreakException();
                    return result; 
                } catch(BreakException be) {
                    return null; 
                } 
            }
            else {
                def entity = [
                    txnmode         : txnmode, 
                    formno          : o.formno, 
                    formtype        : o.formtype, 
                    collectiontype  : o 
                ]; 
                return findOpener( entity ); 
            }
        }
    ] as TileViewModel;

    void askOfflineDate(def info) {
        boolean pass = false;
        Modal.show( "cashreceipt:specifydate", [ 
            entity  : [receiptdate: info.receiptdate], 
            handler : {v-> 
                info.receiptdate = v.receiptdate; 
                pass = true;
            }
        ]);
        if ( !pass ) throw new BreakException(); 
    }
    
    def findOpener( o ) {
        try { 
            def info = cashReceiptSvc.init( o );
            def openerParams = [entity: info]; 

            openerParams.createHandler = {
                def op = findOpener( o ); 
                if ( !op ) return;

                binding.fireNavigation( op ); 
            } 

            def opener = Inv.lookupOpener("cashreceipt:"+ o.collectiontype.handler, openerParams);  
            if (opener) {
                if(txnmode == 'OFFLINE') askOfflineDate(info);
                opener.target = 'self';
                return opener; 
            } else {
                throw new Exception('No available handler found');
            } 
        } catch(BreakException be) { 
            return null;
  
        } catch(Warning w) { 
            String m = "cashreceipt:" + w.message;
            return InvokerUtil.lookupOpener(m, [entity: o]);
        }
    } 
}   