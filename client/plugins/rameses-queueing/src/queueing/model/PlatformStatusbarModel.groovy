package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
        
class PlatformStatusbarModel {

    def terminalkey;
    
    public void init() {        
        terminalkey = OsirisContext.env.TERMINALID;
    }
    
    def getInfo() { 
        return terminalkey; 
    }
}