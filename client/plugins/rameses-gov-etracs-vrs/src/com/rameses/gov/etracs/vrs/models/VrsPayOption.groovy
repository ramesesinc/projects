package com.rameses.gov.etracs.vrs.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*;
import com.rameses.util.*;

public class VrsPayOption  {
    
    def payOption;
    def handler;
    
    def doOk() {
        handler( payOption );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
        
    }
    
}    