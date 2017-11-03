package gov.lgu.aklan.terminal.models

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import gov.lgu.aklan.terminal.TicketPrinter

class  CashReceiptReprintModel 
{
    @Service('AklanTerminalTicketService')
    def svc
    
    String title="Reprint Terminal Pass"
    
    def entity 
    def tickets;
            
    void init(){
        tickets = svc.getTickets([objid:entity.objid])
        selectAll()
    }
    
    def listHandler = [
        isAllowAdd : {false},
        fetchList : { tickets },
        getRows   : { tickets.size()+1 },
    ] as EditorListModel
    
    void selectAll(){
        tickets.each{
            if (it.dtused)
                it.print = false
            else 
                it.print = true 
        }
        listHandler.reload()
    }
    
    void deselectAll(){
        tickets.each{
            it.print = false 
        }
        listHandler.reload()
    }
    
    void reprintTickets() {
        def selectedTickets = tickets.findAll{it.print}
        if (!selectedTickets) 
            throw new Exception("Ticket(s) to reprint should be selected.")
            
        if (!MsgBox.confirm('Reprint selected tickets?')) return; 
        
        selectedTickets.each {o-> 
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