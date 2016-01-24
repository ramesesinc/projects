package com.rameses.etracs.shared;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

public abstract class ReportController extends com.rameses.osiris2.reports.ReportController 
{ 
    @Service('DateService')
    def dtSvc; 
        
    def init() {
        def parsedate = dtSvc.parseCurrentDate();
        entity.month = getMonthsByQtr().find{it.index == parsedate.month} 
        entity.year = parsedate.year;
        entity.qtr  = parsedate.qtr;
        entity.day = parsedate.day;
        return super.init(); 
    }
    
    List getQuarters() {
        return [1,2,3,4]; 
    }
        
    List getMonthsByQtr() {
        return dtSvc.getMonthsByQtr( entity.qtr );
    }
    
    List getMonths(){
        return getMonthsByQtr();
    }
}
