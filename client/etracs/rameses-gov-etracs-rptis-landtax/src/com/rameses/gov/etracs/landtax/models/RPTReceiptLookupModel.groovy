package com.rameses.gov.etracs.landtax.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

class RPTReceiptLookupModel extends BasicLookupController {
    String serviceName = "CashReceiptQueryService";
    String entityName = "cashreceipt";
    String title = "Realty Tax Receipt Lookup";

    List fetchList( Map params ) { 
        params.formno = '56';
        params.receiptno = searchtext;
        return super.fetchList(params);
    }
}      