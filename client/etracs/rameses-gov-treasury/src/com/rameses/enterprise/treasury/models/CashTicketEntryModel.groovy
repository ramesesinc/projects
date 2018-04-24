package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

class CashTicketEntryModel  {

    @Script("User")
    def user;
    
    void init() {
        //MsgBox.alert( 'entry ' + user.userid + ' ' + user.name );
    }

     def listModel = [
       fetchList : { o->
           return [];
       }
    ] as EditorListModel;
    
}    