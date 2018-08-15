package ccom.rameses.enterprise.treasury.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

class AFDropdownListModel extends ComponentBean {
        
    @Service("QueryService")
    def queryService;
    
    
    def _afList;
    
    public def getAfList() {
        if(!_afList ) {
            def m = [ _schemaname: "af" ];
            m.where = "1=1";
            _afList = queryService.getList( m );
        }
        return _afList;
    }
    
    public def getAf() {
        return getValue();
    }
    
    void setAf( def af ) {
        setValue( af );
    }
    
}