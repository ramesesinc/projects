package com.rameses.clfc.treasury.amnesty.capture

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class XAmnestyCaptureAmnestyInfoController 
{
    def entity, mode = 'read';
    def option;
    
    @PropertyChangeListener
    def listener = [
        "option": {o ->
            entity.amnestyoption = o.caption;
            //println 'option change';
        }
    ];
    
    def options = [
        [name: 'waiver', caption: 'WAIVER'],
        [name: 'fix', caption: 'FIX']
    ];
    
    def getOpener() {
        if (entity.amnestyoption) {
            option = options.find{ it.caption == entity.amnestyoption }
        }
        if (!option) return;
        def params = [
            entity  : entity,
            mode    : mode,
        ];
        return Inv.lookupOpener(option.name + "capture:option", params);
    }
{
}

