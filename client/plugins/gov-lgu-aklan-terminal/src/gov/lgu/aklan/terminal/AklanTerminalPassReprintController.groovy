/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.lgu.aklan.terminal

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

/**
 *
 * @author wflores 
 */
class AklanTerminalPassReprintController {

    @Caller 
    def caller; 
    
    @Service('AklanTerminalTicketService') 
    def svc; 
    
    
    void reprint() {
        if (MsgBox.prompt("Please enter security code") == "etracs") {
            entity.tickets.each {o-> 
                o.collector = entity.collector.name;
                o.orno = entity.receiptno;
                o.ordate = entity.receiptdate; 
                o.gateno = o.tag;
            } 
            def printer = new TicketPrinter();
            printer.terminalname = entity.org.name;
            printer.print( entity.thermalprintername, entity.tickets ); 
        }
    }
    
}