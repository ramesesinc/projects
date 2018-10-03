package com.rameses.gov.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;

public class ChangeModeAction extends Action {
 
    private String _mode; 
    private def _handler;
    
    public ChangeModeAction( String mode, def handler ) {
        this._mode = mode; 
        this._handler = handler; 
    }
    
    public String getCaption() {
        return this._mode;
    }
        
    public Object execute() { 
        if ( _handler ) {
            _handler( _mode ); 
        }
        return null; 
    }
}