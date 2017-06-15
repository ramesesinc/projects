package com.rameses.gov.etracs.obo.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.enterprise.models.*;

class OboVariablePickListModel extends CrudLookupModel  {
    
    def section;
    
    @PropertyChangeListener
    def l = [
        "section": { o->
            MsgBox.alert('changing section ' + o);
        }
    ]
    
}