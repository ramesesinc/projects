package com.rameses.clfc.patch.encashment

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.text.*;

class ResolveEncashmentController 
{
    @Binding
    def binding;
    
    @Service('ResolveEncashmentService')
    def service;
    
    @Service('DateService')
    def dateSvc;
    
    def dateFormat = new SimpleDateFormat('yyyy-MMM-dd');
    
    String title = 'Resolve Encashments';
    
    def startdate, enddate;
    
    void init() {
        startdate = dateSvc.getServerDateAsString().split(' ')[0];
        enddate = startdate;
    }
    
    void resolve() {
        def sd = dateFormat.format(parseDate(startdate));
        def ed = dateFormat.format(parseDate(enddate));
        def msg = '<html>You are about to resolve encashments from <b>' + sd + '</b> to <b>' + ed + '</b>. Continue?</html>';
        
        if (!MsgBox.confirm(msg)) return;
        
        def params = [startdate: startdate, enddate: enddate];
        service.resolve(params);
        MsgBox.alert('Success resolved encashments!');
        binding?.refresh();
    }
    
    def parseDate( date ) {
        if (!date) return null;
        
        if (date instanceof Date) {
            return date;
        } else {
            return java.sql.Date.valueOf(date);
        }
    }
}

