package ccom.rameses.enterprise.treasury.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

class FundDropdownListModel extends ComponentBean {
        
    @Service("QueryService")
    def queryService;
    
    
    def _fundList;
    
    public def getFundList() {
        if(!_fundList ) {
            def m = [ _schemaname: "fund" ];
            m.where = "1=1";
            _fundList = queryService.getList( m );
        }
        return _fundList;
    }
    
    public def getFund() {
        return getValue();
    }
    
    void setFund( def fund ) {
        setValue( fund );
    }
    
}