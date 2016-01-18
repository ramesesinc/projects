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
            def data = svc.reprint( caller.selectedEntity ); 
            def ticket = data.ticket;
            ticket.putAll([
                collector : data.collector.name, 
                orno      : data.receiptno, 
                ordate    : data.receiptdate,  
                gateno    : ticket.tag 
            ]);  
        
            def printer = new TicketPrinter();
            printer.terminalname = data.org.name;
            printer.print( data.thermalprintername, [ticket] ); 
        }
    }
    
}