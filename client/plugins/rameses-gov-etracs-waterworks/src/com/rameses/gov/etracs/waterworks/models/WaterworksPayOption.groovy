package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.enterprise.treasury.cashreceipt.*;
import com.rameses.util.*;

public class WaterworksPayOption  {
    
    def payOption;
    def handler;
    
    def monthList;
    
    def doOk() {
        
        handler( payOption );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
        
    }
    
}    