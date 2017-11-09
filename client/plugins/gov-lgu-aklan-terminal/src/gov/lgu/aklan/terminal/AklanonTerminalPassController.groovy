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
import java.rmi.server.UID;
import gov.lgu.aklan.terminal.*;


/**
 *
 * @author wflores 
 */
class AklanonTerminalPassController {
    
    @Service('AklanTerminalPassService') 
    def service; 
    
    @Binding 
    def binding; 
    
    def entity = [:]; 
    def mode = 'read'; 
    def title = 'Terminal Pass ( AKLANON )';
        
    void create() {
        entity = [
            objid: 'TPASS' + new UID(), 
            numadult:0, numchildren:0 
        ]; 
        mode = 'create'; 
    }
    
    void createAnother() {
        create();
        binding.focus('entity.numadult');
        binding.refresh();
    }

    def cancel() {
        if (MsgBox.confirm('Are you sure you want to close this window?')) {
            return '_close'; 
        } else {
            return null; 
        }
    }
    
    void save() {
        entity = service.create( entity );
        mode = 'read';
        binding.refresh(); 
        reprint();
    } 
    
    def close() {
        return '_close'; 
    }
        
    void reprint() {
        entity.tickets.each {o->  
            o.collector = entity.collector.name; 
            o.ordate = entity.dtfiled;  
            o.gateno = o.tag; 
        } 
        def printer = new TicketPrinter(); 
        printer.terminalname = entity.org.name; 
        printer.print( entity.thermalprintername, entity.tickets ); 
    }
}