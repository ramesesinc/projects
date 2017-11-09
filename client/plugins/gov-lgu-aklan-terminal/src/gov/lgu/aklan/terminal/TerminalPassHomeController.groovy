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
class TerminalPassHomeController {
    
    @Binding 
    def binding; 
    
    def options = [];
    
    void init() {
        Inv.lookup('terminalpasshome.action', [:]).each{
            options << [
                caption : it.caption, 
                invoker : it 
            ]; 
        }
    }
    
    def formhandler;    
    def listhandler = [
        getDefaultIcon: {
            //return 'Tree.closedIcon'; 
            return 'home/icons/folder.png';
        }, 
        fetchList: { 
            return options; 
        },
        onselect: {o-> 
            formhandler = Inv.createOpener(o.invoker, [:]); 
        }
    ] as ListPaneModel; 
}