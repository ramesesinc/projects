package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class DayOfWeekListModel extends ComponentBean {

   def dowList = [
        [key:1,title:'SUN'],
        [key:2,title:'MON'],
        [key:3,title:'TUE'],
        [key:4,title:'WED'],
        [key:5,title:'THU'],
        [key:6,title:'FRI'],
        [key:7,title:'SAT'],
    ];
    
    public int getDow() {
        def val = getValue(); 
        return (val ? val : 0); 
    }
    
    public void setDow(int s) {
        setValue( s ); 
    }
    
}