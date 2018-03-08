package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

public class AccountListModel extends CrudListModel {
    
    public void initColumn( def c ) {
        if ( c.name == 'sector.code' ) {
            c.visible = false; 
        } else if ( c.name == 'zone.code' ) {
            c.visible = false; 
        } 
    } 
  
}