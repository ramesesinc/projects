package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;

public class YearMonthPayOptionModel  {

    @Service("DateService")
    def dateSvc;
    
    int year;
    def month;
    def onselect;
    
    void init() {
        year = dateSvc.getServerYear();
        month = dateSvc.getServerMonth();
    }
    
    def doOk() {
        onselect( [ year: year, month: month ] );
        return "_close";
    }

    def doCancel() {
        return "_close";
    }
    
}