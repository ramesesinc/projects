package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.CrudListModel;

class AFRequestListModel extends CrudListModel {

	void removeEntity() {
        if ( !selectedItem) return; 

        if ( !isDeleteAllowed()) 
            throw new Exception("Cancel is not allowed for this transaction"); 
        if ( selectedItem?.system.toString() == 1 )
            throw new Exception("Cannot cancel system created file");

        if ( MsgBox.confirm('You are about to cancel this record. Proceed?')) { 
	        def ename = ( entitySchemaName ? entitySchemaName : schemaName);
	        def m = [_schemaname: ename];
	        m.objid = selectedItem.objid; 
	        m.state = 'CANCELLED'; 
	        getPersistenceService().update( m ); 
	        listHandler.reload(); 
        }
	}
} 