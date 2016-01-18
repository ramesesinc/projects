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
class AklanTerminalPassListController extends ListController {
    
    String serviceName = 'AklanTerminalTicketService';
    String entityName = 'aklanterminalpass';    
    String formName = 'aklanterminalpass';
    boolean allowOpen = false;
    boolean allowCreate = false;
}