package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import java.util.concurrent.*;
import com.rameses.treasury.common.models.*;

public class WaterworksBillingModel extends MultiBillingModel {
    
    @Service("DateService")
    def dateSvc;
    
    def list = [];
    
    int getTotalcount() {
        return list.size();
    }
    
    public def start() {
        def s = super.start();
        query.year = dateSvc.getServerYear();
        query.month = dateSvc.getServerMonth(); 
         int i = 0;  
        list << "data"+(i++);
        list << "data"+(i++);
        list << "data"+(i++);
        list << "data"+(i++);
        list << "data"+(i++);
        list << "data"+(i++);
        list << "data"+(i++);
        list << "data"+(i++);
        list << "data"+(i++);
        list << "data"+(i++);
        list << "data"+(i++);
        return s;
    }

    public def fetchList( def o ) {
        return list;
    }
    
    public void processEntry( def o ) {
        list.remove(o);
        Thread.sleep( 2000 );
    }
    
    
}