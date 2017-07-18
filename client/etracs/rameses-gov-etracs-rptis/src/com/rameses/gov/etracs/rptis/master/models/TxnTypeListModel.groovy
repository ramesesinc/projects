package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;


class TxnTypeListModel extends MasterListModel
{
    boolean createAllowed  = false;
    boolean deleteAllowed  = false;
    
    boolean allowExport = false;
    boolean allowImport = false;
    boolean allowSync = false;
}