package gov.lgu.aklan.terminal.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;


class  TerminalPassVerifyModel 
{
    @Binding
    def binding;
    
    @Service('AklanTurnstileService')
    def turnstileSvc;
    
    @Service('AklanTerminalTicketService')
    def ticketSvc;
    
    String title = 'Verify Terminal Pass'
    
    def entity;
    def turnstiles;
    def msg;
    
    @PropertyChangeListener
    def listener = [
        'entity.barcode' : { msg = null; }
    ]
    
    void init(){
        entity = [:]
        turnstiles = turnstileSvc.getList([:])
        entity.turnstile = turnstiles.find{it.tags == 'TOURIST'}
    }
    
    void verify(){
        def retval = ticketSvc.verify([id:entity.turnstile.objid, barcode:entity.barcode])
        msg = null;
        if (retval.retcode != 0){
            msg = retval.message;
        }
        else {
            msg = 'Barcode is valid'
        }
        binding.refresh('msg');
    }

} 