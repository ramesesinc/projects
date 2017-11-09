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
class AklanSpecialTerminalPassPrintController {

    @Caller 
    def caller; 
    
    @Service('AklanSpecialPassAcctService') 
    def svc; 
        
    void print() {
        if (!MsgBox.confirm('You are about to print a terminal pass for this account. Continue?')) return; 
        
        def data = svc.printTicket( caller.selectedEntity ); 
        def ticket = data.ticket;
        ticket.putAll([
            collector : data.collector.name, 
            ordate    : data.dtfiled, 
            gateno    : ticket.tag 
        ]); 

        def printer = new TicketPrinter();
        printer.terminalname = data.org.name;
        printer.print( data.thermalprintername, [ticket] ); 
    }
    
}