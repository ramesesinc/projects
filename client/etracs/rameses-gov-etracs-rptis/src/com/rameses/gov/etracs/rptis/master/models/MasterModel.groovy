package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;

class MasterModel extends CrudListModel
{
    @Service('RPTISMasterExportService')
    def svc
    
    def export(){
    }
}