package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;

class CashReceiptModel extends CrudFormModel { 

    String schemaName = "cashreceipt";    
    
    boolean isAllowVoid() { 
        if ( entity.voided==true || entity.voided==1 ) return false; 
        if ( entity.remitted==true || entity.remitted==1 ) return false; 
        return true; 
    } 
    
    
} 