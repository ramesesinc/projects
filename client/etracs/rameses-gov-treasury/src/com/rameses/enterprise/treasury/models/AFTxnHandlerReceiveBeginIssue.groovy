package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;
import com.rameses.rcp.framework.ValidatorException;


class AFTxnHandlerReceiveBeginIssue extends AFTxnHandler {

    void initBeginBalance() {
        title = "Accountable Form Begin Balance";
    }
    
    void initPurchaseReceipt() {
        title = "Accountable Form Purchase Receipt";
    }

    void initIssue() {
        title = "Accountable Form Issue";
    }

}    