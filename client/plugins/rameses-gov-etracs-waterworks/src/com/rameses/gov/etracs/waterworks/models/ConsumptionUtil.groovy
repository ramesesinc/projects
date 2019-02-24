package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.report.*;
import com.rameses.rcp.framework.*;
import java.text.*;


public class ConsumptionUtil  {
    
    @Service("WaterworksComputationService")
    def compSvc;

    //pass the following:
    //account, meter, currentinfo - prevreading, reading
    //METER IS ACTIVE, DISCONNECTED AND DEFECTIVE
    //IF DEFECTIVE, reading and volume is editable
    //IF ACTIVE, only volume will be editable
    public void compute( def param, def handler ) {
        def acctid = param.acctid;
        def meterid = param.meterid;
        def meterstate = param.meterstate;  
        if(handler==null) handler = {v->};
        
        if(!handler) throw new Exception("Please provide handler in ConsumptionUtil");
        
        if( meterid ) {
            def f = [:];
            f.handler = { o->
                def z = [acctid:acctid, meterid: meterid, consumptionid: param.consumptionid, meterstate: param.meterstate ];
                z.putAll(o);
                def res = compSvc.compute( z );
                o.putAll(res);
                handler(o);
            }
            f.data = [ prevreading:param.prevreading, reading:param.reading,  volume: 0]
            f.fields = [];
            f.fields << [caption:'Previous Reading', name:'prevreading', type:'integer', required:true];
            f.fields << [caption:'Current Reading', name:'reading', type:'integer', required:true];
            if ( meterstate != 'ACTIVE' ) {
                f.fields << [caption:'Volume', name:'volume', type:'integer', required:true]; 
            }
            Modal.show("dynamic:form", f, [title:'Calculate Amount']);
        }
        else {
            def z = [acctid: acctid, consumptionid: param.consumptionid, meterstate: param.meterstate ];
            def res = compSvc.compute( z );
            handler( res );
        }
    }

    
}