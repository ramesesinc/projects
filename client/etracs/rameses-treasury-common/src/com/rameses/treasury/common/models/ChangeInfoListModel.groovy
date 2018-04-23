package com.rameses.treasury.common.models;
 
import com.rameses.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.constant.*;
import java.rmi.server.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

/****
* This facility only extracts only a portion of the data. 
*/
public class ChangeInfoListModel extends CrudSubListModel {
   
    def onOpenHandler = { o,colName ->
        if(colName == "oldvalue") {
            MsgBox.alert( "oldvalue " + o.oldvalue );
        }
        else if( colName == "newvalue" ) {
            MsgBox.alert( "newvalue " + o.newvalue ); 
        }
    }
    
    
}
        