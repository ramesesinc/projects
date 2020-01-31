package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class MachSmvInfoModel extends CrudListModel implements com.rameses.gov.etracs.rptis.interfaces.SubPage {
	def callerMode;

    def getMasterEntity() {
        return caller?.entity;
    }
    
    def getCustomFilter() {
        return ["parent_objid = :objid", masterEntity];
    }    

    void modeChanged(String mode) {
    	callerMode = mode;
    	binding?.refresh();
    }

    boolean isCreateAllowed() {
        if (callerMode != 'edit') {
            return false;
        }
        return super.isCreateAllowed();
    }

    boolean isOpenAllowed() {
        if (callerMode != 'edit') {
            return false;
        }
        return super.isOpenAllowed();
    }

    boolean isDeleteAllowed() {
        if (callerMode != 'edit') {
            return false;
        }
        return super.isDeleteAllowed();
    }
}