package com.rameses.clfc.billinggroup

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class BillingGroupConferenceAccountListController
{
    def listHandler = [
        fetchList: { o->
            return [];
        }
    ] as BasicListModel;
    /*
    @Service('LoanBillingGroupOptionAccountsService')
    def svc;
    
    String serviceName = 'LoanBillingGroupOptionAccountsService';
    
    boolean allowCreate = false;
    boolean allowClose = false;
    boolean allowOpen = false;
    //abstract String getType();
    
    String title = '';
    
    def selectedOption;
    def optionsModel = [
        getItems: {
            return svc.getStates();
        }, 
        onselect: { o->
            query.state = o.state;
            reloadAll();
        }
    ] as ListPaneModel;

    void beforeGetColumns(Map params) {
        params.state = selectedOption?.state;
    }
    */
}

