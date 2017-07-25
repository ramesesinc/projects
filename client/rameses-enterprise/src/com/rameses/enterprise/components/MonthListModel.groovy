package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class MonthListModel extends ComponentBean {

   def monthList = [
        [key:1,title:'JAN'],
        [key:2,title:'FEB'],
        [key:3,title:'MAR'],
        [key:4,title:'APR'],
        [key:5,title:'MAY'],
        [key:6,title:'JUN'],
        [key:7,title:'JUL'],
        [key:8,title:'AUG'],
        [key:9,title:'SEP'],
        [key:10,title:'OCT'],
        [key:11,title:'NOV'],
        [key:12,title:'DEC'],
    ];
    
    public int getMonth() {
        def val = getValue(); 
        return (val ? val : 0); 
    }
    
    public void setMonth(int s) {
        setValue( s ); 
    }
    
}